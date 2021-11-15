package com.devtools.mylib;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;


public class GetCinemas {

    public static void main(String[] args) throws Exception {
        Map<String,String> engRus = findCinemas("ekaterinburg");
        System.out.println(engRus.keySet());
        System.out.println(engRus.values());
    }

    public static Map<String,String> findCinemas(String city) throws Exception {
        URL oracle = new URL("https://kassa.rambler.ru/" + city + "/movie");
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
        Map<String, String> dictionary = new HashMap<>();
        String regexCinema = "\s<a href=\"(/"+city+"/cinema/[-a-z0-9]+)\">([\sа-яА-я]+)</a>";
        Pattern patternCinema = Pattern.compile(regexCinema);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            Matcher matcher = patternCinema.matcher(inputLine);
            if (matcher.find()) {
                dictionary.put(matcher.group(2), matcher.group(1));
            }
        }
        return dictionary;
    }
}
