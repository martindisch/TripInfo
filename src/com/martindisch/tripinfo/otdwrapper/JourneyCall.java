package com.martindisch.tripinfo.otdwrapper;

import java.time.Instant;

/**
 * Represents a call (stop) of a {@link Journey}.
 */
public class JourneyCall {
    public static final int PREVIOUS = 1;
    public static final int ONWARD = 2;

    private String name;
    private int type;
    private Instant arrivalTimetabled, arrivalEstimated, departureTimetabled, departureEstimated;

    /**
     * Constructor for timetabled and estimated arrival/departure.
     *
     * @param name                the station's name
     * @param type                if this is a previous or onward call
     * @param arrivalTimetabled   the planned arrival at this stop
     * @param arrivalEstimated    the estimated (or recorded if type) arrival at this stop
     * @param departureTimetabled the planned departure from this stop
     * @param departureEstimated  the estimated (or recorded if type) departure from this stop
     */
    public JourneyCall(String name, int type, Instant arrivalTimetabled, Instant arrivalEstimated, Instant departureTimetabled, Instant departureEstimated) {
        this.name = name;
        this.type = type;
        this.arrivalTimetabled = arrivalTimetabled;
        this.arrivalEstimated = arrivalEstimated;
        this.departureTimetabled = departureTimetabled;
        this.departureEstimated = departureEstimated;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public Instant getArrivalTimetabled() {
        return arrivalTimetabled;
    }

    public Instant getArrivalEstimated() {
        return arrivalEstimated;
    }

    public Instant getDepartureTimetabled() {
        return departureTimetabled;
    }

    public Instant getDepartureEstimated() {
        return departureEstimated;
    }
}