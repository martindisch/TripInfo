package com.martindisch.tripinfo.otdwrapper;

/**
 * Class representing a station, with name and station code.
 */
public class Station {
    private String name, code;

    public Station(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return String.format("Station instance\ncode: %-10sname: %s", code, name);
    }
}
