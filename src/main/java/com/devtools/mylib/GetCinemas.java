package com.devtools.mylib;
import lombok.SneakyThrows;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;


public class GetCinemas {

    @SneakyThrows
    public static Map<String,String> findCinemas(String city) {
        URL oracle = new URL("https://kassa.rambler.ru/" + city + "/movie");
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
        return analyzingInputData(in, city);
    }

    @SneakyThrows
    public static Map<String, String> analyzingInputData(BufferedReader in, String city) {
        Map<String,String> dictionary = new HashMap<>();
        String regexCinema = "\s<a href=\"(/" + city + "/cinema/[-a-z0-9]+)\">([\sа-яА-я]+)</a>";
        Pattern patternCinema = Pattern.compile(regexCinema);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            Matcher matcher = patternCinema.matcher(inputLine);
            if (matcher.find())
                dictionary.put(matcher.group(2), matcher.group(1));
        }
        return dictionary;
    }
}