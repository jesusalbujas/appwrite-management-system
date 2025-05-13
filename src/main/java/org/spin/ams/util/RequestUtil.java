package org.spin.ams.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestUtil {

    private static final String BASE_URL = "http://api.adempiere.io:2021";

    // Fetch data specifically from the /threads endpoint
    public static String fetchThreads() {
        return fetchFromEndpoint("/threads");
    }

    // Fetch data from the /tipologies endpoint
    public static String fetchTipologies() {
        return fetchFromEndpoint("/tipology");
    }

    // Fetch data from the /topics endpoint
    public static String fetchTopics() {
        return fetchFromEndpoint("/topics");
    }

    // Fetch data from the /clients endpoint
    public static String fetchClients() {
        return fetchFromEndpoint("/clients");
    }

    /**
     * Generic method to fetch data based on the entity name
     * @param entity Name of the entity (e.g., "threads", "tipologies")
     * @return JSON string response from the corresponding endpoint
     */
    public static String fetchDataFor(String entity) {
        switch (entity) {
            case "Threads":
                return fetchThreads();
            case "Tipology":
                return fetchTipologies();
            case "Topics":
                return fetchTopics();
            case "Clients":
                return fetchClients();
            default:
                throw new IllegalArgumentException("Entity not supported: " + entity);
        }
    }

    /**
     * Reusable method to send an HTTP GET request to the specified endpoint
     * @param endpoint Endpoint path (e.g., "/threads")
     * @return JSON response as a string
     */
    private static String fetchFromEndpoint(String endpoint) {
        try {
            URL url = new URL(BASE_URL + endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300
                    ? conn.getInputStream()
                    : conn.getErrorStream()
            ));

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    // Simple test method to fetch data for "threads"
    public static void main(String[] args) {
        String response = fetchDataFor("threads");
        System.out.println("Threads response: " + response);
    }
}
