package com.martindisch.tripinfo.interaction;

import com.martindisch.tripinfo.otdwrapper.*;

import java.util.ArrayList;

/**
 * Handles the user interaction for selecting a trip.
 */
public class JourneySelection {

    /**
     * Presents the user with a list of possible connections and returns the selected one.
     *
     * @param handler   {@link OTDHandler} instance for making requests
     * @param departure the departure station
     * @param arrival   the arrival station
     * @return the selected trip
     * @throws HandlerException
     */
    public static Trip selectTrip(OTDHandler handler, Station departure, Station arrival) throws HandlerException {
        System.out.println("Contacting server for trips...");
        ArrayList<Trip> trips = handler.getTrips(departure, arrival);
        for (int i = 0; i < trips.size(); i++) {
            System.out.printf("%4d: departing %s at %s\n", i, trips.get(i).getDeparture().getName(), HandlerUtil.formatTimeLocal(trips.get(i).getDepartureTime()));
        }
        System.out.print("Select a connection: ");
        int tripIndex = Integer.parseInt(System.console().readLine());
        return trips.get(tripIndex);
    }
}
