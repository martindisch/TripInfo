package com.martindisch.tripinfo.interaction;

import com.martindisch.tripinfo.otdwrapper.*;

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
        for (JourneyCall call : journey.getCalls()) {
            displayCall(call);
        }
    }

    /**
     * Temporary method displaying all information of the given JourneyCall.
     *
     * @param call the {@link JourneyCall} instance to print
     */
    private static void displayCall(JourneyCall call) {
        System.out.printf("%s as %s: ", call.getName(), call.getType() == 1 ? "previous" : "onward");
        System.out.printf("%s", call.getArrivalTimetabled() != null ? call.getArrivalTimetabled().toString() + " " : "");
        System.out.printf("%s", call.getArrivalEstimated() != null ? call.getArrivalEstimated().toString() + " " : "");
        System.out.printf("%s", call.getDepartureTimetabled() != null ? call.getDepartureTimetabled().toString() + " " : "");
        System.out.printf("%s", call.getDepartureEstimated() != null ? call.getDepartureEstimated().toString() + " " : "");
        System.out.printf("\n");
    }
}
