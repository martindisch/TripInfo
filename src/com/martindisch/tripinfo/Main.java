package com.martindisch.tripinfo;

import com.martindisch.tripinfo.otdwrapper.OTDHandler;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            OTDHandler handler = new OTDHandler();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
