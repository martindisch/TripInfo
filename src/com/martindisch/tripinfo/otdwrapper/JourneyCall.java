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

    @Override
    public String toString() {
        return String.format(
                "JourneyCall instance\ntype: %10s name: %s\nstation type timetabled            estimated\narrival      %-22s%s\ndeparture    %-22s%s",
                type == JourneyCall.PREVIOUS ? "previous" : "onward", name,
                arrivalTimetabled != null ? arrivalTimetabled : "-", arrivalEstimated != null ? arrivalEstimated : "-",
                departureTimetabled != null ? departureTimetabled : "-", departureEstimated != null ? departureEstimated : "-"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof JourneyCall) {
            if (name.contentEquals(((JourneyCall) o).getName()) && type == ((JourneyCall) o).getType()) {
                if (arrivalTimetabled != null && ((JourneyCall) o).getArrivalTimetabled() != null) {
                    if (!arrivalTimetabled.equals(((JourneyCall) o).getArrivalTimetabled())) return false;
                }
                if (arrivalEstimated != null && ((JourneyCall) o).getArrivalEstimated() != null) {
                    if (!arrivalEstimated.equals(((JourneyCall) o).getArrivalEstimated())) return false;
                }
                if (departureTimetabled != null && ((JourneyCall) o).getDepartureTimetabled() != null) {
                    if (!departureTimetabled.equals(((JourneyCall) o).getDepartureTimetabled())) return false;
                }
                if (departureEstimated != null && ((JourneyCall) o).getDepartureEstimated() != null) {
                    if (!departureEstimated.equals(((JourneyCall) o).getDepartureEstimated())) return false;
                }
                return true;
            }
        }
        return false;
    }
}
