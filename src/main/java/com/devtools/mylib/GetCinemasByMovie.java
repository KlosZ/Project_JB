package com.devtools.mylib;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetCinemasByMovie {

    public static void main(String[] args) {
        System.out.println(findCinemasByMovie("ekaterinburg","100291"));
    }

    @SneakyThrows
    public static String findCinemasByMovie(String cityURL, String movieURL) {
        URL oracle = new URL("https://kassa.rambler.ru/" + cityURL + "/movie/" + movieURL);
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
        return analyzingInputData(in);
    }

    public static Matcher getMatcher(String regex, String line){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(line);
    }

    @SneakyThrows
    public static String analyzingInputData(BufferedReader in) {
        StringBuilder result = new StringBuilder();
        String regexCinema = "\s<a href=\"[/a-z0-9-]+\" itemprop=\"url\"><span itemprop=\"name\" class=\"s-name\">([\sа-яА-яA-za-z]+)</span></a>";
        String regexSeance = "\s<div class=\"rasp_type\">([\sa-яА-яA-Za-z0-9]+)<i class=\"ruble\"></i></div>";
        String regexDate ="<span class=\"date_link\" data-url=\"[a-яА-яA-Za-z0-9/]+\\?date=([0-9\\.]+)\">";
        String inputLine;
        StringBuilder cinema = new StringBuilder();
        boolean checkSeance = false;
        boolean checkTime = true;
        boolean checkDate = true;
        while ((inputLine = in.readLine()) != null) {
            Matcher matcherCinema = getMatcher(regexCinema, inputLine);
            Matcher matcherSeance = getMatcher(regexSeance, inputLine);
            Matcher matcherTime = getMatcher("\s([0-9]{2}:[0-9]{2})", inputLine);
            Matcher matcherDate = getMatcher(regexDate, inputLine);
            if(matcherDate.find())
            {
                if(checkDate) {
                    result.append("ближайшие сеансы есть ").append(matcherDate.group(1)).append(" в этих кинотеатрах:");
                    checkDate = false;
                }
            }
            if (matcherCinema.find()) {
                checkTime = false;
                if(checkSeance) {
                    if (!result.toString().equals(""))
                        result.append("\n\n");
                    result.append(cinema);
                    checkSeance = false;
                }
                cinema = new StringBuilder(matcherCinema.group(1) + "\n");
                continue;
            }
            if (matcherSeance.find()) {
                if(checkTime)
                {
                    cinema.append("\n");
                    checkTime = false;
                }
                cinema.append("- ").append(matcherSeance.group(1)).append("\n");
                checkSeance = true;
                continue;
            }
            if (matcherTime.find()) {
                if(checkTime)
                    cinema.append(", ");
                cinema.append(matcherTime.group(1));
                checkTime = true;
            }
        }
        if(checkSeance) {
            if (!result.toString().equals(""))
                result.append("\n\n");
            result.append(cinema);
        }
        if(result.toString().equals(""))
            result.append("боюсь, ни в одном кинотеатре этот фильм не показывают..."); // не достигается?
        return result.toString();
    }
}
