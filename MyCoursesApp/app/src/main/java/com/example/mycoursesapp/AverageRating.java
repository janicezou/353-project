package com.example.mycoursesapp;

import android.os.Bundle;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AverageRating {
    Double averageRating;

    protected Double getAverageRating(String courseNum) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    Scanner in;
                    URL url = new URL("http://10.0.2.2:3000/viewComments/" + courseNum);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    int responseCode = conn.getResponseCode();
                    if (responseCode != 200) {
                        System.out.println("Unexpected status code: " + responseCode);
                    } else {
                        in = new Scanner(url.openStream());
                        while (in.hasNext()) {
                            String line = in.nextLine();
                            String pattern = "Average Rating: ";
                            Pattern p = Pattern.compile(pattern);
                            Matcher m = p.matcher(line);
                            Integer index = null;
                            while (m.find()) {
                                index = m.start() + pattern.length() + 2;
                            }
                            if (index != null) {
                                String result = line.substring(index, index + 4);
                                try {
                                    averageRating = Double.parseDouble(result);
                                } catch (NumberFormatException nfe) {
                                    nfe.printStackTrace();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch(Exception ee) {
            ee.printStackTrace();
        }
        return averageRating;
    }
}
