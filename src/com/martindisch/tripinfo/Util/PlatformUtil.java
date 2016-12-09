package com.martindisch.tripinfo.Util;

import com.martindisch.tripinfo.otdwrapper.HandlerException;
import com.martindisch.tripinfo.otdwrapper.HandlerUtil;
import com.martindisch.tripinfo.otdwrapper.Station;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * PlatformUtil class with static helper functions.
 */
public class PlatformUtil {

    /**
     * Reads and returns the first line of a text file. Useful for reading the API key from file.
     *
     * @return the first line from the file
     */
    public static String getApiKey() throws HandlerException {
        try {
            FileInputStream is = new FileInputStream("apikey.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String buf = br.readLine();
            br.close();
            return buf;
        } catch (IOException e) {
            HandlerException exception;
            if (e instanceof FileNotFoundException) {
                exception = new HandlerException("apikey.txt not found. The API key is necessary to make requests.");
            } else {
                exception = new HandlerException(e.getMessage());
            }
            throw exception;
        }
    }

    /**
     * Reads a station list CSV and returns a List of Station objects.
     *
     * @return the list of stations in the file
     */
    public static ArrayList<Station> getStationList() throws HandlerException {
        try {
            FileInputStream is = new FileInputStream("bahnhof.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            // discard first line (CSV column header)
            br.readLine();
            Station tmp;
            ArrayList<Station> stations = new ArrayList<>(2600);
            String line;
            while ((line = br.readLine()) != null) {
                tmp = HandlerUtil.parseCSVStation(line);
                if (tmp != null) stations.add(tmp);
            }
            br.close();
            return stations;
        } catch (IOException e) {
            HandlerException exception;
            if (e instanceof FileNotFoundException) {
                exception = new HandlerException("bahnhof.csv not found. The station list is required for execution.");
            } else {
                exception = new HandlerException(e.getMessage());
            }
            throw exception;
        }
    }

    /**
     * Makes a POST request to the given url using the apiKey and requestBody and returns the result.
     *
     * @param url         the url to post to
     * @param apiKey      the API key to use
     * @param requestBody the request body
     * @return the answer
     * @throws IOException
     */
    public static String doPostRequest(String url, String apiKey, String requestBody) throws IOException {
        URL service = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) service.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", apiKey);
        connection.addRequestProperty("Content-Type", "application/XML");
        connection.setRequestProperty("Content-Length", Integer.toString(requestBody.length()));
        connection.setDoOutput(true);
        connection.getOutputStream().write(requestBody.getBytes("UTF8"));

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
