package com.martindisch.tripinfo.otdwrapper;

import java.time.Instant;

/**
 * Represents a trip from one station to another.
 */
public class Trip {
    private Station departure, arrival;
    private String journeyRef;
    private Instant departureTime;

    public Trip(Station departure, Station arrival, String journeyRef, Instant departureTime) {
        this.departure = departure;
        this.arrival = arrival;
        this.journeyRef = journeyRef;
        this.departureTime = departureTime;
    }

    public Station getDeparture() {
        return departure;
    }

    public Station getArrival() {
        return arrival;
    }

    public String getJourneyRef() {
        return journeyRef;
    }

    public Instant getDepartureTime() {
        return departureTime;
    }

    @Override
    public String toString() {
        return String.format("Trip instance\njourneyRef : %-30sdepartureTime: %s\ndeparture: %s\narrival: %s",
                journeyRef, departureTime.toString(), departure.toString(), arrival.toString());
    }
}
