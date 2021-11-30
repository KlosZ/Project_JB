package com.devtools.mylib;
import lombok.SneakyThrows;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.*;

public class GetGenresIMDb {

    public static void main(String[] args) {
        System.out.println(findMovie("action"));
    }

    @SneakyThrows
    public static String findMovie(String genre) {
        URL oracle = new URL("https://www.imdb.com/search/title/?genres=" + genre + "&sort=boxoffice_gross_us,desc");
        BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
        return analyzingInputData(in);
    }

    @SneakyThrows
    public static String analyzingInputData(BufferedReader in) {
        Random random = new Random();
        ArrayList<String> movies = new ArrayList<>();
        String regexMovie = ">([а-яА-я ]+)</a>";
        String regexYear = "\s+<span class=\"lister-item-year text-muted unbold\">([I0-9–()]+)</span>";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            Matcher matcher = Pattern.compile(regexMovie).matcher(inputLine);
            if (matcher.find()) {
                inputLine = in.readLine();
                Matcher matcherYear = Pattern.compile(regexYear).matcher(inputLine);
                if(matcherYear.find())
                    movies.add(matcher.group(1) + ' ' + matcherYear.group(1));
            }
        }
        return movies.get(random.nextInt(movies.size() - 1) + 1);
    }
}
