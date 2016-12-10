package com.martindisch.tripinfo.interaction;

import com.martindisch.tripinfo.otdwrapper.HandlerException;
import com.martindisch.tripinfo.otdwrapper.Journey;
import com.martindisch.tripinfo.otdwrapper.OTDHandler;
import com.martindisch.tripinfo.otdwrapper.Trip;

/**
 * Shows live journey information to the user.
 */
public class LiveView {

    /**
     * Shows the progress of the trip's journey.
     *
     * @param handler instance of {@link OTDHandler} to make requests
     * @param trip    the trip that is part of the journey
     */
    public static void showJourney(OTDHandler handler, Trip trip) throws HandlerException {
        Journey journey = handler.getJourney(trip.getJourneyRef());
        System.out.println(journey.toString());
    }
}
