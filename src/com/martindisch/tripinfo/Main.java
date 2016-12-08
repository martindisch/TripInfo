package com.martindisch.tripinfo;

import com.martindisch.tripinfo.interaction.StationSelection;
import com.martindisch.tripinfo.otdwrapper.OTDHandler;
import com.martindisch.tripinfo.otdwrapper.Station;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.print("Initializing...");
            OTDHandler handler = new OTDHandler();
            System.out.println("Done.");
            Station[] stations = StationSelection.selectStations(handler);
            System.out.printf("Selected %s and %s.\n", stations[0].getName(), stations[1].getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
