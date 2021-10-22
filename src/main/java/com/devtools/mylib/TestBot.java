package com.devtools.mylib;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TestBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "@KKsCinemaBot";
    }

    @Override
    public String getBotToken(){
        return System.getenv("ACCESS_BOT_TOKEN");
    }

    @SneakyThrows
    public static void main(String[] args) {
        TestBot bot = new TestBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            handleMessage(update.getMessage());
            }
        }

    public static ArrayList<String> getAllGenres() {
        ArrayList<String> genres = new ArrayList<>();
        genres.add("Комедии");
        genres.add("Мультфильмы");
        genres.add("Ужасы");
        genres.add("Фантастика");
        genres.add("Триллеры");
        genres.add("Боевики");
        genres.add("Детективы");
        genres.add("Мелодрамы");
        genres.add("Аниме");
        genres.add("Военные");
        genres.add("Приключения");
        genres.add("Фэнтези");
        genres.add("Исторические");
        genres.add("Семейные");
        genres.add("Драмы");
        genres.add("Документальные");
        genres.add("Биографии");
        genres.add("Детские");
        genres.add("Криминал");
        genres.add("Вестерны");
        genres.add("Спортивные");
        genres.add("Короткометражки");
        genres.add("Концерты");
        genres.add("Мюзиклы");
        return genres;
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()){
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/choose_genre":
                        ArrayList<String> genres = getAllGenres();
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        for (int i = 0; i < genres.size(); i += 2){
                            buttons.add(Arrays.asList(
                                    InlineKeyboardButton.builder().text(genres.get(i)).callbackData("what?").build(),
                                    InlineKeyboardButton.builder().text(genres.get(i + 1)).callbackData("what?").build()
                            ));
                        }
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text("Please, choose a genre of film you want to watch. ")
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                .build());
                }
            }

        }
    }
}
