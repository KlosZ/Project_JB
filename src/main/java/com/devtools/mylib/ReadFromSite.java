package com.devtools.mylib;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.*;


public class ReadFromSite {

    public static void main(String[] args) throws Exception {
        System.out.println(findMovie("Thriller"));
        System.out.println(findMovie("Adventure"));
    }

    public static String findMovie(String genre) throws Exception {
        URL oracle = new URL("https://www.imdb.com/search/title/?genres="+genre+"&explore=title_type,genres&ref_=tt_ov_inf");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(oracle.openStream()));
        Random random = new Random();
        ArrayList<String> movies = new ArrayList<>();
        String regex = ">([а-яА-я ]+)</a>";
        String regexYear = "\s+<span class=\"lister-item-year text-muted unbold\">([I0-9–()]+)</span>";
        Pattern patternYear = Pattern.compile(regexYear);
        Pattern pattern = Pattern.compile(regex);
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            Matcher matcher = pattern.matcher(inputLine);
            if (matcher.find()) {
                inputLine = in.readLine();
                Matcher matcherYear = patternYear.matcher(inputLine);
                if(matcherYear.find())
                    movies.add(matcher.group(1)+' '+matcherYear.group(1));
            }
        }
        return movies.get(random.nextInt(movies.size() - 1) + 1);
    }
}
