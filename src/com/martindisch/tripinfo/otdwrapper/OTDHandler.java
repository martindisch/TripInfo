package com.martindisch.tripinfo.otdwrapper;

import com.martindisch.tripinfo.HTTPUtil;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class OTDHandler {

    private String apiKey;
    private ArrayList<Station> stations;

    private static final String TRIP_REQUEST = "<Trias version=\"1.1\" xmlns=\"http://www.vdv.de/trias\" xmlns:siri=\"http://www.siri.org.uk/siri\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><ServiceRequest><RequestPayload><TripRequest><Origin><LocationRef><StopPointRef>%s</StopPointRef></LocationRef></Origin><Destination><LocationRef><StopPointRef>%s</StopPointRef></LocationRef></Destination><Params><IncludeTrackSections>false</IncludeTrackSections><IncludeLegProjection>false</IncludeLegProjection><IncludeIntermediateStops>false</IncludeIntermediateStops></Params></TripRequest></RequestPayload></ServiceRequest></Trias>";
    private static final String REQUEST_URL = "https://api.opentransportdata.swiss/trias";

    /**
     * Constructor reads API key and station list from file for later use.
     *
     * @throws IOException if one of the files doesn't exist or something else went wrong while reading
     */
    public OTDHandler() throws IOException {
        try {
            apiKey = Util.readFirstLine("apikey.txt");
            stations = Util.readStationList("bahnhof.csv");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage() + " not found. This file is necessary to make requests.");
        }
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
     * @param arrival the arrival station
     * @return a list of trips between the stations
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     */
    public ArrayList<Trip> getTrips(Station departure, Station arrival) throws IOException, ParserConfigurationException, SAXException, XPathExpressionException {
        ArrayList<Trip> trips = new ArrayList<>(3);

        String answer = HTTPUtil.doPostRequest(REQUEST_URL, apiKey, String.format(TRIP_REQUEST, departure.getCode(), arrival.getCode()));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new ByteArrayInputStream(answer.getBytes()));
        XPath xPath = XPathFactory.newInstance().newXPath();

        NodeList tripNodes = (NodeList) xPath.evaluate(".//Trip", doc, XPathConstants.NODESET);
        for (int i = 0; i < tripNodes.getLength(); i++) {
            Node currentTrip = tripNodes.item(i);
            String departureTime = xPath.evaluate(".//LegBoard/ServiceDeparture/TimetabledTime", currentTrip);
            String journeyRef = xPath.evaluate(".//Service/JourneyRef", currentTrip);
            trips.add(new Trip(departure, arrival, journeyRef, Instant.parse(departureTime)));
        }
        return trips;
    }
}
