package com.martindisch.tripinfo.otdwrapper;

import java.io.*;
import java.util.ArrayList;

/**
 * Util class with static helper functions.
 */
public class Util {

    /**
     * Reads and returns the first line of a text file. Useful for reading the API key from file.
     *
     * @param filename      path to the file
     * @return              the first line from the file
     * @throws IOException  in case the file doesn't exist, the message contains the filename
     */
    public static String readFirstLine(String filename) throws IOException {
        try {
            FileInputStream is = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String buf = br.readLine();
            br.close();
            return buf;
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new FileNotFoundException(filename);
            } else {
                throw e;
            }
        }
    }

    /**
     * Reads a station list CSV and returns a List of Station objects.
     *
     * @param filename      path to the file
     * @return              the list of stations in the file
     * @throws IOException  in case the file doesn't exist, the message contains the filename
     */
    public static ArrayList<Station> readStationList(String filename) throws IOException {
        try {
            FileInputStream is = new FileInputStream(filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            ArrayList<Station> stations = new ArrayList<>(2600);
            // discard first line (CSV column header)
            br.readLine();
            Station tmp;
            while ((line = br.readLine()) != null) {
                tmp = parseCSVStation(line);
                if (tmp != null) stations.add(parseCSVStation(line));
            }
            br.close();
            return stations;
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                throw new FileNotFoundException(filename);
            } else {
                throw e;
            }
        }
    }

    /**
     * Reads a line (from the station list CSV) and returns the station object.
     *
     * @param line  the original line from CSV
     * @return      the station object
     */
    private static Station parseCSVStation(String line) {
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
