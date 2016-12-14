package com.martindisch.tripinfo.interaction;

import com.martindisch.tripinfo.otdwrapper.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Shows live journey information to the user.
 */
public class LiveView {

    private static final int STATION_DELAY = 20;

    /**
     * Shows the progress of the trip's journey between the two current stations.
     *
     * @param handler instance of {@link OTDHandler} to make requests
     * @param trip    the trip that is part of the journey
     * @throws HandlerException
     */
    public static void showJourney(OTDHandler handler, Trip trip) throws HandlerException {
        JourneyCall[] lastPair = null;
        try {
            while (true) {
                Journey journey = handler.getJourney(trip.getJourneyRef());
                JourneyCall[] currentCalls = HandlerUtil.getCurrentCalls(journey);
                if (currentCalls == null) {
                    System.out.println("\nThis service is not currently active");
                    break;
                }
                if (!(lastPair != null && lastPair[0].equals(currentCalls[0]) && lastPair[1].equals(currentCalls[1]))) {
                    printHeader(currentCalls[0], currentCalls[1]);
                }
                printStatus(currentCalls[0], currentCalls[1]);
                lastPair = currentCalls;
                TimeUnit.SECONDS.sleep(5);
            }
        } catch (InterruptedException e) {
            throw new HandlerException(e.getMessage());
        }
    }

    /**
     * Prints the current leg's header, including station names and timetabled and (if available) estimated times.
     *
     * @param previous the previous call
     * @param onward the onward call
     */
    private static void printHeader(JourneyCall previous, JourneyCall onward) {
        System.out.println();
        System.out.println();
        System.out.printf("%-40s%40s\n", previous.getName(), onward.getName());
        System.out.printf("%-40s%40s\n",
                HandlerUtil.formatTimeLocal(previous.getDepartureTimetabled()),
                HandlerUtil.formatTimeLocal(onward.getArrivalTimetabled())
        );
        if (previous.getDepartureEstimated() != null && onward.getArrivalEstimated() != null) {
            System.out.printf("%-40s%40s\n",
                    HandlerUtil.formatTimeLocal(previous.getDepartureEstimated()),
                    HandlerUtil.formatTimeLocal(onward.getArrivalEstimated())
            );
        }
    }

    /**
     * Continually prints a representation of the vehicle's position between the two current calls.
     *
     * @param previous the previous call
     * @param onward the current call
     */
    private static void printStatus(JourneyCall previous, JourneyCall onward) {
        Instant departure, arrival;
        if (previous.getDepartureTimetabled() != null && onward.getArrivalEstimated() != null) {
            departure = previous.getDepartureEstimated();
            arrival = onward.getArrivalEstimated();
        } else {
            departure = previous.getDepartureTimetabled();
            arrival = onward.getArrivalTimetabled();
        }
        long timeTotal = departure.plus(STATION_DELAY, ChronoUnit.SECONDS).until(arrival, ChronoUnit.SECONDS);
        long timeTravelled = departure.plus(STATION_DELAY, ChronoUnit.SECONDS).until(Instant.now(), ChronoUnit.SECONDS);
        double percentTravelled = (double) timeTravelled / timeTotal;
        if (percentTravelled < 0) percentTravelled = 0;
        else if (percentTravelled > 1) percentTravelled = 1;
        int currentPos = (int) (80 * percentTravelled);
        if (currentPos <= 1) currentPos = 2;
        if (currentPos >= 80) currentPos = 79;
        System.out.print("\r");
        for (int i = 1; i <= 80; i++) {
            if (currentPos - 1 == i || currentPos == i) {
                System.out.print("■");
            } else if (currentPos + 1 == i) {
                System.out.print(">");
            } else if (i == 1 || i == 80) {
                System.out.print("#");
            } else {
                System.out.print("=");
            }
        }
    }
}
