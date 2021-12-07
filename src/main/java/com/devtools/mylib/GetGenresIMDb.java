package com.devtools.mylib;
import lombok.SneakyThrows;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
        Map<String,String> movies = new HashMap<>();
        ArrayList<String> titles = new ArrayList<String>();
        String regexTitle = "\s<a href=\"/title/(tt[0-9]+).+";
        String regexMovie = ">([а-яА-я 0-9:?!]+)</a>";
        String regexYear = "\s+<span class=\"lister-item-year text-muted unbold\">([I0-9–()]+)</span>";
        String lastMovie = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            Matcher matcher = Pattern.compile(regexMovie).matcher(inputLine);
            if (matcher.find()) {
                inputLine = in.readLine();
                Matcher matcherYear = Pattern.compile(regexYear).matcher(inputLine);
                if(matcherYear.find()){
                    movies.put(lastMovie,matcher.group(1) + ' ' + matcherYear.group(1));
                    titles.add(lastMovie);
                }
            }
            matcher = Pattern.compile(regexTitle).matcher(inputLine);
            if (matcher.find())
            {
                lastMovie = matcher.group(1);
            }
        }
        String randMovie = titles.get(random.nextInt(titles.size() - 1) + 1);
        URL oracle = new URL("https://imdb-api.com/ru/API/Title/k_87qqc5tk/"+randMovie+"/FullActor,FullCast,");
        BufferedReader movieIn = new BufferedReader(new InputStreamReader(oracle.openStream()));
        String m = movieIn.readLine();
        String image = m.substring(m.indexOf("image\":")+("image\":").length()+1,m.indexOf("\",\"releaseDate\""));
        String plotLocal = m.substring(m.indexOf("\"plotLocal\":\"")+("\"plotLocal\":\"").length(),m.indexOf("\",\"plotLocalIsRtl"));
        String plot = m.substring(m.indexOf("\"plot\":\"")+("\"plot\":\"").length(),m.indexOf("\"plotLocal\":\""));
        String director = m.substring(m.indexOf("\"directors\":\"")+("\"directors\":\"").length(),m.indexOf("\",\"directorList"));
        String stars = m.substring(m.indexOf(",\"stars\":\"")+(",\"stars\":\"").length(),m.indexOf("\",\"starList\":"));
        if (plotLocal == "")
        {
            return "\n"+movies.get(randMovie)+"\nРежиссер(ы): "+director+"\n\nАктеры: "+stars+"\n\nСюжет: "+plot+ "\n\nПостер: "+ image;
        }
        else
        {
            return "\n"+movies.get(randMovie)+"\nРежиссер(ы): "+director+"\n\nАктеры: "+stars+"\n\nСюжет: "+plotLocal + "\n\nПостер: "+ image;
        }
    }
}
