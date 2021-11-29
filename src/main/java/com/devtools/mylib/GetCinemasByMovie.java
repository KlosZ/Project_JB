package com.devtools.mylib;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class GetCinemasByMovie {

    @SneakyThrows
    public static String findCinemasByMovie(String cityURL, String movieURL) {
        URL oracle = new URL("https://kassa.rambler.ru/" + cityURL + "/movie/" + movieURL);
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
        return analyzingInputData(in);
    }

    @SneakyThrows
    public static String analyzingInputData(BufferedReader in) {
        StringBuilder result = new StringBuilder();
        return result.toString();
    }
}
