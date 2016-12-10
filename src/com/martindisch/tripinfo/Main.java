package com.martindisch.tripinfo;

import com.martindisch.tripinfo.interaction.JourneySelection;
import com.martindisch.tripinfo.interaction.LiveView;
import com.martindisch.tripinfo.interaction.StationSelection;
import com.martindisch.tripinfo.otdwrapper.HandlerException;
import com.martindisch.tripinfo.otdwrapper.OTDHandler;
import com.martindisch.tripinfo.otdwrapper.Station;
import com.martindisch.tripinfo.otdwrapper.Trip;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Initializing...");
            OTDHandler handler = new OTDHandler();
            Station[] stations = StationSelection.selectStations(handler);
            Trip trip = JourneySelection.selectTrip(handler, stations[0], stations[1]);
            LiveView.showJourney(handler, trip);
        } catch (HandlerException e) {
            e.printStackTrace();
        }
    }
}
