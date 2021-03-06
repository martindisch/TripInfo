package com.martindisch.tripinfo.otdwrapper;

import com.martindisch.tripinfo.Util.PlatformUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * An instance of OTDHandler is capable of making requests to the https://opentransportdata.swiss API.
 */
public class OTDHandler {

    private String apiKey;
    private ArrayList<Station> stations;

    private static final String TRIP_REQUEST = "<Trias version=\"1.1\" xmlns=\"http://www.vdv.de/trias\" xmlns:siri=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ServiceRequest><RequestPayload><TripRequest><Origin><LocationRef><StopPointRef>%s</StopPointRef></LocationRef></Origin><Destination><LocationRef><StopPointRef>%s</StopPointRef></LocationRef></Destination><Params><IncludeTrackSections>false</IncludeTrackSections><IncludeLegProjection>false</IncludeLegProjection><IncludeIntermediateStops>false</IncludeIntermediateStops></Params></TripRequest></RequestPayload></ServiceRequest></Trias>";
    private static final String TRIP_INFO_REQUEST = "<?xml version=\"1.0\" encoding=\"utf-16\"?><Trias version=\"1.1\" xmlns=\"http://www.vdv.de/trias\" xmlns:siri=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ServiceRequest><RequestPayload><TripInfoRequest><JourneyRef>%s</JourneyRef><OperatingDayRef>%s</OperatingDayRef><Params><UseTimetabledDataOnly>false</UseTimetabledDataOnly><IncludeCalls>true</IncludeCalls><IncludePosition>false</IncludePosition><IncludeService>true</IncludeService></Params></TripInfoRequest></RequestPayload></ServiceRequest></Trias>";
    private static final String REQUEST_URL = "https://api.opentransportdata.swiss/trias";

    /**
     * Constructor obtains API key and station list for later use.
     *
     * @throws HandlerException in case the API key or station list could not be obtained
     */
    public OTDHandler() throws HandlerException {
        apiKey = PlatformUtil.getApiKey();
        stations = PlatformUtil.getStationList();
    }

    /**
     * Looks through the stations and returns a list of those containing name as a substring.
     *
     * @param name the name of the station
     * @return a list of stations containing name as a substring
     */
    public ArrayList<Station> searchStation(String name) {
        ArrayList<Station> matches = new ArrayList<>(20);
        matches.addAll(stations.stream().filter(s -> s.getName().contains(name)).collect(Collectors.toList()));
        return matches;
    }

    /**
     * Returns a list of trips between the given stations around the current time.
     *
     * @param departure the departure station
     * @param arrival   the arrival station
     * @return a list of trips between the stations
     * @throws HandlerException in case anything went wrong with obtaining or parsing the request
     */
    public ArrayList<Trip> getTrips(Station departure, Station arrival) throws HandlerException {
        try {
            ArrayList<Trip> trips = new ArrayList<>(3);

            String answer = PlatformUtil.doPostRequest(REQUEST_URL, apiKey, String.format(TRIP_REQUEST, departure.getCode(), arrival.getCode()));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(answer.getBytes()));
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList tripNodes = (NodeList) xPath.evaluate("//Trip", doc, XPathConstants.NODESET);
            for (int i = 0; i < tripNodes.getLength(); i++) {
                Node trip = tripNodes.item(i);
                String departureTime = xPath.evaluate(".//LegBoard/ServiceDeparture/TimetabledTime", trip);
                String journeyRef = xPath.evaluate(".//Service/JourneyRef", trip);
                trips.add(new Trip(departure, arrival, journeyRef, Instant.parse(departureTime)));
            }
            return trips;
        } catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException e) {
            throw new HandlerException(e.getMessage());
        }
    }

    public Journey getJourney(String journeyRef) throws HandlerException {
        try {
            String date = Instant.now().toString().split("T")[0] + "T";
            String answer = PlatformUtil.doPostRequest(REQUEST_URL, apiKey, String.format(TRIP_INFO_REQUEST, journeyRef, date));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new ByteArrayInputStream(answer.getBytes()));
            XPath xPath = XPathFactory.newInstance().newXPath();

            ArrayList<JourneyCall> calls = new ArrayList<>(30);
            NodeList children = (NodeList) xPath.evaluate("//TripInfoResult/child::node()", doc, XPathConstants.NODESET);
            for (int i = 0; i < children.getLength(); i++) {
                Node call = children.item(i);
                String elementName = xPath.evaluate("name(.)", call);
                if (elementName.contentEquals("PreviousCall") || elementName.contentEquals("OnwardCall")) {
                    String name = xPath.evaluate("./StopPointName/Text", call);
                    int type = elementName.contentEquals("PreviousCall") ? JourneyCall.PREVIOUS : JourneyCall.ONWARD;
                    String arrivalTimetabled = xPath.evaluate("./ServiceArrival/TimetabledTime", call);
                    String arrivalEstimated = xPath.evaluate("./ServiceArrival/EstimatedTime", call);
                    String departureTimetabled = xPath.evaluate("./ServiceDeparture/TimetabledTime", call);
                    String departureEstimated = xPath.evaluate("./ServiceDeparture/EstimatedTime", call);
                    JourneyCall journeyCall = new JourneyCall(
                            name, type,
                            HandlerUtil.emptyAsNull(arrivalTimetabled),
                            HandlerUtil.emptyAsNull(arrivalEstimated),
                            HandlerUtil.emptyAsNull(departureTimetabled),
                            HandlerUtil.emptyAsNull(departureEstimated)
                    );
                    calls.add(journeyCall);
                }
            }
            return new Journey(journeyRef, calls);
        } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            throw new HandlerException(e.getMessage());
        }
    }
}
