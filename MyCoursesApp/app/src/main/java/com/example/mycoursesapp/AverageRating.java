package com.example.mycoursesapp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AverageRating {
    private Double averageRating;

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
                        Pattern regex = Pattern.compile("Average Rating: ([0-5].?[0-5]*)");
                        while (in.hasNext()) {
                            String line = in.nextLine();
//                            String pattern = "Average Rating: ";
//                            Pattern p = Pattern.compile(pattern);
//                            Matcher m = p.matcher(line);
                            Matcher matcher = regex.matcher(line);

                            if(matcher.find()){
                                try {
                                    averageRating = Double.parseDouble(matcher.group(1))*1.0;
                                    System.out.println(averageRating);
                                } catch (NumberFormatException nfe) {
                                    nfe.printStackTrace();
                                }
                            }
//                            Integer index = null;
//                            while (m.find()) {
//                                index = m.start() + pattern.length();
//                            }
//                            if (index != null) {
//                                String r = line.substring(index);
//                                String result = r.substring(0, 1);
//                                try {
//                                    averageRating = Integer.parseInt(result)*1.0;
//                                } catch (NumberFormatException nfe) {
//                                    nfe.printStackTrace();
//                                }
//                            }
                        }
                    }

                    if (conn != null) {
                        conn.disconnect();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            // wait for thread to return with updated average value
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch(Exception ee) {
            ee.printStackTrace();
        }
        System.out.println("returning averageRating: " + averageRating);
        return averageRating;
    }

}
