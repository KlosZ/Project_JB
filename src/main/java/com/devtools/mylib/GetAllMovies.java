package com.devtools.mylib;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;


public class GetAllMovies {

    public static void main(String[] args) {
        System.out.println(findMovies("velikiy-novgorod"));
    }

    @SneakyThrows
    public static Map<String,String> findMovies(String city) {
        ArrayList<String> pages = new ArrayList<>();
        Map<String, String> dictionary = new HashMap<>();
        pages.add("1");
        String page = "1";
        while (Integer.parseInt(page) <= Integer.parseInt(pages.get(pages.size() - 1))) {
            URL oracle = new URL("https://kassa.rambler.ru/" + city + "/movie/page-" + page);
            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
            String regexCinema = "\s<h3 class=\"mb_item__title\" itemprop=\"name\">([\sa-zA-zа-яА-я0-9.:;!?,&#]+)</h3>";
            String regexId = "\s<a href=\"/" + city + "/movie/([0-9]+)\">";
            String regexPage = "\s<li class=\"mb-pagination-list-item\"><a class=\"mb-pagination-list-item-txt\" href=\"/" + city + "/movie/page-([0-9]+)\">[0-9]+</a></li>";
            Pattern patternCinema = Pattern.compile(regexCinema);
            Pattern patternId = Pattern.compile(regexId);
            Pattern patternPage = Pattern.compile(regexPage);
            String inputLine;
            String Id = "";
            while ((inputLine = in.readLine()) != null) {
                Matcher matcherCinema = patternCinema.matcher(inputLine);
                Matcher matcherId = patternId.matcher(inputLine);
                Matcher matcherPage = patternPage.matcher(inputLine);
                if (matcherCinema.find())
                    dictionary.put(matcherCinema.group(1), Id);
                if (matcherId.find())
                    Id = matcherId.group(1);
                if (matcherPage.find()) {
                    if (!pages.contains(matcherPage.group(1)))
                        pages.add(matcherPage.group(1));
                }
            }
            page = Integer.toString(Integer.parseInt(page) + 1);
        }
        System.out.println(pages);
        return dictionary;
    }
}