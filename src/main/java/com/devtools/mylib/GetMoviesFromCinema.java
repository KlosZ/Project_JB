package com.devtools.mylib;
import lombok.SneakyThrows;

import java.net.*;
import java.io.*;
import java.util.regex.*;


public class GetMoviesFromCinema {

    public static void main(String[] args) {
        String engRus = findMovies("/ekaterinburg/cinema/titanik-sinema-361");
        System.out.println(engRus);
    }

    public static Matcher getMatcher(String regex, String line){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(line);
    }

    @SneakyThrows
    public static String findMovies(String cinema) {
        URL oracle = new URL("https://kassa.rambler.ru" + cinema);
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
        StringBuilder result = new StringBuilder();
        String regexMovie = "\s<div class=\"rasp_title\"><a href=\"[/a-z0-9]+\" itemprop=\"url\"><span itemprop=\"name\" class=\"s-name\">([\sa-zA-zа-яА-я0-9.:,]+)</span></a></div>";
        String regexAge = "\s<div class=\"rasp_audience_movie\" itemprop=\"typicalAgeRange\">([0-9+]+)</div>";
        String regexGenre = "\s<div class=\"rasp_place\" itemprop=\"genre\">([а-яА-я,\s]+)</div>";
        String regexSeance = "\s<div class=\"rasp_type\">([\sa-яА-яA-Za-z0-9]+)<i class=\"ruble\"></i></div>";
        String inputLine;
        StringBuilder movie = new StringBuilder();
        boolean checkSeance = false;
        boolean checkTime = true;
        while ((inputLine = in.readLine()) != null) {
            Matcher matcherMovie = getMatcher(regexMovie, inputLine);
            Matcher matcherAge = getMatcher(regexAge, inputLine);
            Matcher matcherGenre = getMatcher(regexGenre, inputLine);
            Matcher matcherSeance = getMatcher(regexSeance, inputLine);
            Matcher matcherTime = getMatcher("\s([0-9]{2}:[0-9]{2})", inputLine);
            if (matcherMovie.find()) {
                checkTime = false;
                if(checkSeance) {
                    if (!result.toString().equals(""))
                        result.append("\n\n");
                    result.append(movie);
                    checkSeance = false;
                }
                movie = new StringBuilder("'" + matcherMovie.group(1) + "'" + " (");
                continue;
            }
            if (matcherAge.find()) {
                movie.append(matcherAge.group(1)).append(")\n");
                continue;
            }
            if (matcherGenre.find()) {
                movie.append(matcherGenre.group(1)).append(") (");
                continue;
            }
            if (matcherSeance.find()) {
                if(checkTime)
                {
                    movie.append("\n");
                    checkTime = false;
                }
                movie.append("- ").append(matcherSeance.group(1)).append("\n");
                checkSeance = true;
                continue;
            }
            if (matcherTime.find()) {
                if(checkTime)
                    movie.append(", ");
                movie.append(matcherTime.group(1));
                checkTime = true;
            }
        }
        if(checkSeance) {
            if (!result.toString().equals(""))
                result.append("\n\n");
            result.append(movie);
        }
        if(result.toString().equals(""))
            return "В этом кинотеатре сегодня нет сеансов. Попробуйте другой";
        return result.toString();
    }
}