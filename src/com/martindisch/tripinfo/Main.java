package com.martindisch.tripinfo;

import com.martindisch.tripinfo.interaction.JourneySelection;
import com.martindisch.tripinfo.interaction.StationSelection;
import com.martindisch.tripinfo.otdwrapper.HandlerException;
import com.martindisch.tripinfo.otdwrapper.OTDHandler;
import com.martindisch.tripinfo.otdwrapper.Station;
import com.martindisch.tripinfo.otdwrapper.Trip;

import java.time.ZoneId;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Initializing...");
            OTDHandler handler = new OTDHandler();
            Station[] stations = StationSelection.selectStations(handler);
            Trip trip = JourneySelection.selectTrip(handler, stations[0], stations[1]);
            System.out.printf("Selected journey %s  departing %s at %s\n", trip.getJourneyRef(), trip.getDeparture().getName(), trip.getDepartureTime().atZone(ZoneId.systemDefault()).toString());
        } catch (HandlerException e) {
            e.printStackTrace();
        }
    }
}
