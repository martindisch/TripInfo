package com.martindisch.tripinfo.otdwrapper;

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
}
