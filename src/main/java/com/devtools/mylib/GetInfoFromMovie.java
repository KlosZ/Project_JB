package com.devtools.mylib;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetInfoFromMovie {

    public static void main(String[] args) {
        System.out.println(findInfoByMovie("ekaterinburg","100291"));
    }

    @SneakyThrows
    public static String findInfoByMovie(String cityURL, String movieURL) {
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
        String regexTitle = "\t\t\t<h1 class=\"item_title\" itemprop=\"name\">(.+)</h1>";
        String regexImage = "\t\t\t\t<img src=\"(.+)\" alt=\"\" title=\"\" itemprop=\"image\" />\t</div>";
        String regexPlot = "\t\t\t\t\t\t<span class=\"item_desc__text item_desc__text-full\">(.+)</span>";
        String regexDirector = "\t\t\t\t<span class=\"dd\" itemprop=\"name\">([\sa-zA-zа-яА-я0-9,:;!?,&#]+)</span>";
        String regexActors = "\t\t\t\t\t\t\t<span class=\"item_peop__actors item_desc__text.+\">([\sa-zA-zа-яА-я0-9.,:;!?,&#]+)</span>";
        String inputLine;
        String poster = "";
        StringBuilder cinema = new StringBuilder();
        boolean check = true;
        String title = "";
        while ((inputLine = in.readLine()) != null) {
            Matcher matcherMovie = getMatcher(regexTitle, inputLine);
            Matcher matcherImage = getMatcher(regexImage, inputLine);
            Matcher matcherPlot = getMatcher(regexPlot, inputLine);
            Matcher matcherDirector = getMatcher(regexDirector, inputLine);
            Matcher matcherActors = getMatcher(regexActors, inputLine);
            if (matcherMovie.find()) {
                result.append(matcherMovie.group(1)).append("\n");
                title = matcherMovie.group(1);
            }
            if (matcherImage.find()) {
                poster = "Постер: "+matcherImage.group(1)+"\n";
            }
            if (matcherPlot.find()) {
                result.append("Сюжет:\n").append(matcherPlot.group(1)).append("\n");
            }
            if (matcherDirector.find()) {
                result.append("Режиссер(ы): ").append(matcherDirector.group(1)).append("\n");
            }
            if (matcherActors.find() && check) {
                check = false;
                result.append("Актеры: ").append(matcherActors.group(1)).append("\n");
            }
        }
        URL oracle = new URL("https://imdb-api.com/ru/API/SearchMovie/k_87qqc5tk/"+title);
        BufferedReader movieIn = new BufferedReader(new InputStreamReader(oracle.openStream()));
        String m = movieIn.readLine();
        String image = m.substring(m.indexOf("image\":")+("image\":").length()+1,m.indexOf("\",\"title\""));
        if (image == "")
        {
            result.append("Постер: ").append(poster);
        }
        else{
            result.append("Постер: ").append(image);
        }
        if(result.toString().equals(""))
            result.append("Что-то пошло не так..."); // не достигается?
        return result.toString().replaceAll("&#xAB;","\"").replaceAll("&#xBB;","\"").replaceAll("&#x2116;","№").replaceAll("&#x2014;","-");
    }
}
