package com.martindisch.tripinfo;

import com.martindisch.tripinfo.interaction.JourneySelection;
import com.martindisch.tripinfo.interaction.StationSelection;
import com.martindisch.tripinfo.otdwrapper.OTDHandler;
import com.martindisch.tripinfo.otdwrapper.Station;
import com.martindisch.tripinfo.otdwrapper.Trip;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Initializing...");
            OTDHandler handler = new OTDHandler();
            Station[] stations = StationSelection.selectStations(handler);
            Trip trip = JourneySelection.selectTrip(handler, stations[0], stations[1]);
            System.out.printf("Selected the one departing %s at %s\n", trip.getDeparture().getName(), trip.getDepartureTime().toString());
        } catch (IOException | ParserConfigurationException | XPathExpressionException | SAXException e) {
            e.printStackTrace();
        }
    }
}
