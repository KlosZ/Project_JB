package com.devtools.mylib;
import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;


public class GetMoviesFromCinema {

    public static void main(String[] args) throws Exception {
        String engRus = findMovies("/ekaterinburg/cinema/titanik-sinema-361");
        System.out.println(engRus);
    }

    public static Matcher getMatcher(String regex, String line){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(line);
    }
    public static String findMovies(String cinema) throws Exception {
        URL oracle = new URL("https://kassa.rambler.ru"+cinema);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        Map<String,String> dictionary = new HashMap<String,String>();
        String result ="";
        String regexMovie = "\s<div class=\"rasp_title\"><a href=\"[/a-z0-9]+\" itemprop=\"url\"><span itemprop=\"name\" class=\"s-name\">([\sa-zA-zа-яА-я0-9.:,]+)</span></a></div>";
        String regexGenre = "\s<div class=\"rasp_place\" itemprop=\"genre\">([а-яА-я,\s]+)</div>";
        String regexAge = "\s<div class=\"rasp_audience_movie\" itemprop=\"typicalAgeRange\">([0-9+]+)</div>";
        String regexSeance = "\s<div class=\"rasp_type\">([\sa-яА-яA-Za-z0-9]+)<i class=\"ruble\"></i></div>";
        String inputLine;
        String movie = "";
        boolean checkSeance = false;
        while ((inputLine = in.readLine()) != null) {
            Matcher matcherMovie = getMatcher(regexMovie,inputLine);
            Matcher matcherGenre = getMatcher(regexGenre,inputLine);
            Matcher matcherAge = getMatcher(regexAge,inputLine);
            Matcher matcherSeance = getMatcher(regexSeance,inputLine);
            Matcher matcherTime = getMatcher("\s([0-9]{2}:[0-9]{2})",inputLine);
            if (matcherMovie.find()) {
                if(checkSeance) {
                    if (!result.equals(""))
                    {
                        result+="\n";
                    }
                    result += movie;
                    checkSeance = false;
                }
                movie = matcherMovie.group(1)+"\n";
                continue;
            }
            if (matcherGenre.find()) {
                movie += matcherGenre.group(1) + "\n";
                continue;
            }
            if (matcherAge.find()) {
                movie+=matcherAge.group(1)+"\n";
                continue;
            }
            if (matcherSeance.find()) {
                movie+=matcherSeance.group(1)+"\n";
                checkSeance = true;
                continue;
            }
            if (matcherTime.find()) {
                movie+=matcherTime.group(1)+"\n";
                continue;
            }
        }
        if(checkSeance) {
            if (!result.equals(""))
            {
                result+="\n";
            }
            result += movie;
            checkSeance = false;
        }
        if(result.equals(""))
        {
            return "В этом кинотеатре сегодня нет сеансов. Попробуйте другой";
        }
        return result;
    }
}