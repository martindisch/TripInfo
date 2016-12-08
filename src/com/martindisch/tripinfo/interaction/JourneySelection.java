package com.martindisch.tripinfo.interaction;

import com.martindisch.tripinfo.otdwrapper.OTDHandler;
import com.martindisch.tripinfo.otdwrapper.Station;
import com.martindisch.tripinfo.otdwrapper.Trip;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles the user interaction for selecting a trip.
 */
public class JourneySelection {

    /**
     * Presents the user with a list of possible connections and returns the selected one.
     *
     * @param handler handler instance for making requests
     * @param departure the departure station
     * @param arrival the arrival station
     * @return the selected trip
     * @throws IOException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static Trip selectTrip(OTDHandler handler, Station departure, Station arrival) throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
        System.out.println("Contacting server for trips...");
        ArrayList<Trip> trips = handler.getTrips(departure, arrival);
        for (int i = 0; i < trips.size(); i++) {
            System.out.printf("%4d: departing %s at %s", i, trips.get(i).getDeparture().getName(), trips.get(i).getDepartureTime().toString());
        }
        System.out.println("Select a connection: ");
        int tripIndex = Integer.parseInt(System.console().readLine());
        return trips.get(tripIndex);
    }
}
