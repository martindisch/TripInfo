package com.martindisch.tripinfo.otdwrapper;

import java.util.ArrayList;

/**
 * Represents a journey with previous and onward calls.
 */
public class Journey {
    private String journeyRef;
    private ArrayList<JourneyCall> calls;

    public Journey(String journeyRef, ArrayList<JourneyCall> calls) {
        this.journeyRef = journeyRef;
        this.calls = calls;
    }

    public String getJourneyRef() {
        return journeyRef;
    }

    public ArrayList<JourneyCall> getCalls() {
        return calls;
    }
}
