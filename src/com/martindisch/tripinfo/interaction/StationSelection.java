package com.martindisch.tripinfo.interaction;

import com.martindisch.tripinfo.otdwrapper.OTDHandler;
import com.martindisch.tripinfo.otdwrapper.Station;

import java.util.ArrayList;

/**
 * Handles user interaction for selecting departure and arrival stations.
 */
public class StationSelection {

    /**
     * Asks the user for departure and arrival station and returns them in an array.
     *
     * @param handler instance of {@link OTDHandler} that has all the stations
     * @return an array containing the departure and arrival station
     */
    public static Station[] selectStations(OTDHandler handler) {
        System.out.print("Enter your departure station: ");
        String departureInput = System.console().readLine();
        ArrayList<Station> departureStations = handler.searchStation(departureInput);
        for (int i = 0; i < departureStations.size(); i++) {
            System.out.printf("%4s: %s\n", i, departureStations.get(i).getName());
        }
        System.out.print("Select a station: ");
        int departureIndex = Integer.parseInt(System.console().readLine());

        System.out.print("Enter your arrival station: ");
        String arrivalInput = System.console().readLine();
        ArrayList<Station> arrivalStations = handler.searchStation(arrivalInput);
        for (int i = 0; i < arrivalStations.size(); i++) {
            System.out.printf("%4d: %s\n", i, arrivalStations.get(i).getName());
        }
        System.out.print("Select a station: ");
        int arrivalIndex = Integer.parseInt(System.console().readLine());

        return new Station[]{departureStations.get(departureIndex), arrivalStations.get(arrivalIndex)};
    }
}
