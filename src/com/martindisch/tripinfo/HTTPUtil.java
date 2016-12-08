package com.martindisch.tripinfo;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * A wrapper handling POST requests. This was done to keep {@link com.martindisch.tripinfo.otdwrapper.OTDHandler}
 * free from utility code.
 */
public class HTTPUtil {

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
