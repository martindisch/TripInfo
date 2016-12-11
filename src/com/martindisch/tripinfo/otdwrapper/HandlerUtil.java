package com.martindisch.tripinfo.otdwrapper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Platform-independent utility functions for {@link OTDHandler}.
 */
public class HandlerUtil {

    /**
     * Reads a line (from the station list CSV) and returns the station object.
     *
     * @param line the original line from CSV
     * @return the station object
     */
    public static Station parseCSVStation(String line) {
        // return null if given the last line (strange format)
        if (line.contentEquals(",")) return null;
        String[] stationAndCode;
        // if the station name contains a comma, it has been put inside parentheses and requires special care
        if (line.endsWith("\"")) {
            // split at the comma with parentheses, removing the leading parentheses from the station name
            stationAndCode = line.split(",\"");
            // shorten the string, thus removing the ending parentheses
            stationAndCode[1] = stationAndCode[1].subSequence(0, stationAndCode[1].length() - 1).toString();
        } else {
            stationAndCode = line.split(",");
        }
        // remove extra information from the station name
        stationAndCode[1] = stationAndCode[1].split("\\$")[0];
        return new Station(stationAndCode[1], stationAndCode[0]);
    }

    /**
     * Returns null if input is empty string, otherwise returns {@link Instant} instance from input.
     *
     * @param input the string to check
     * @return if input is empty string null, otherwise input as Instant instance
     */
    public static Instant emptyAsNull(String input) {
        return (input.contentEquals("") ? null : Instant.parse(input));
    }

    /**
     * Returns the two current (last previous and first onward) calls of the journey.
     *
     * @param journey the journey to look at
     * @return array with the last previous and first onward call of the journey
     */
    public static JourneyCall[] getCurrentCalls(Journey journey) {
        JourneyCall lastPrevious = null, firstOnward = null;
        for (JourneyCall call : journey.getCalls()) {
            if (call.getType() == JourneyCall.PREVIOUS) {
                lastPrevious = call;
            } else {
                firstOnward = call;
                break;
            }
        }
        return new JourneyCall[]{lastPrevious, firstOnward};
    }

    /**
     * Returns the instant formatted as only time using the local timezone.
     *
     * @param instant the instant to format
     * @return the instant's time in the local timezone
     */
    public static String formatTimeLocal(Instant instant) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }
}
