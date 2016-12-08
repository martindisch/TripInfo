package com.martindisch.tripinfo.otdwrapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class OTDHandler {

    private String apiKey;
    private ArrayList<Station> stations;

    /**
     * Constructor reads API key and station list from file for later use.
     *
     * @throws IOException if one of the files doesn't exist or something else went wrong while reading
     */
    public OTDHandler() throws IOException {
        try {
            apiKey = Util.readFirstLine("apikey.txt");
            stations = Util.readStationList("bahnhof.csv");
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage() + " not found. This file is necessary to make requests.");
        }
    }

    /**
     * Looks through the stations and returns a list of those containing name as a substring.
     *
     * @param name the name of the station
     * @return a list of stations containing name as a substring
     */
    public ArrayList<Station> searchStation(String name) {
        ArrayList<Station> matches = new ArrayList<>(20);
        matches.addAll(stations.stream().filter(s -> s.getName().contains(name)).collect(Collectors.toList()));
        return matches;
    }
}
