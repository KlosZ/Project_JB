package com.devtools.mylib;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetAllCities {

    public static String getCityURLByCity(String city) {
        return findCities().get(city);
    }

    public static void main(String[] args) {
        Map<String, String> d = findCities();
        System.out.println(d);
        System.out.println("d.size() = " + d.size());
    }

    @SneakyThrows
    public static Map<String,String> findCities() {
        URL oracle = new URL("https://kassa.rambler.ru/");
        Map<String, String> dictionary = new HashMap<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
        String regexCity = "<li><a class=\"drop_menu__i\" data-target=\"[0-9]+\" href=\"/([a-z-]+)\" data-location=\"[a-z-]+\">([а-яА-я-\s]+)</a></li>";
        Pattern patternCity = Pattern.compile(regexCity);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            Matcher matcherCity = patternCity.matcher(inputLine);
            if (matcherCity.find())
                dictionary.put(matcherCity.group(2), matcherCity.group(1));
        }
        return dictionary;
    }
}
