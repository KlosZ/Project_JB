package com.devtools.mylib;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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

public class CinemaBotClass extends TelegramLongPollingBot {

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
        CinemaBotClass bot = new CinemaBotClass();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        }
        else if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    @SneakyThrows
    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        int data = Integer.parseInt(callbackQuery.getData());
        execute(SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text(executeMessageByKey("callbackChoosingGenreCommand", data))
                .build());
    }

    public static ArrayList<String> getAllGenres() {
        ArrayList<String> genres = new ArrayList<>();
        genres.add("Comedy");
        genres.add("Animation");
        genres.add("Horror");
        genres.add("Sci-Fi");
        genres.add("Thriller");
        genres.add("Action");
        genres.add("Film-Noir");
        genres.add("Drama");
        genres.add("War");
        genres.add("Adventure");
        genres.add("Fantasy");
        genres.add("Romance");
        genres.add("History");
        genres.add("Western");
        genres.add("Mystery");
        genres.add("Documentary");
        genres.add("Biography");
        genres.add("Family");
        genres.add("Crime");
        genres.add("Sport");
        genres.add("Short");
        genres.add("Musical");
        return genres;
    }

    @SneakyThrows
    public static String executeMessageByKey(String key, int data) {
        String movieGenre = "";
        if (data != -1) {
            ArrayList<String> genres = getAllGenres();
            movieGenre = genres.get(data);
        }
        String message = "";
        switch (key) {
            case "startCommand" ->
                    message = "Добро пожаловать в K&K's CinemaBot!\nЭтот бот поможет вам с выбором фильма на вечер.\nОбращайтесь по '/help', если не знаешь что и как. =)";
            case "choosingGenreCommand" ->
                    message = "Пожалуйста, выберите жанр фильма, который хотите посмотреть. ";
            case "callbackChoosingGenreCommand" ->
                    message = "Отличный выбор! Вот фильм жанра '" + movieGenre +
                            "', который вы можете посмотреть:\n" + ReadFromSite.findMovie(movieGenre);
            case "errorInputCommand" ->
                    message = "Команда введена не верно. Попробуйте снова. ";
            case "randomTextInputCommand" ->
                    message = "Вы отправили: ";
            case "randomInputCommand" ->
                    message = "Вы отправили не текстовое сообщение. " +
                            "Я же бот, а не нейросеть, чтобы распознавать, что вы мне отправили. =)";
            case "helpCommand" ->
                    message = """
                            Итак, что же может этот бот (то есть, я)?
                            1. Понятное дело, если забыли конкретную команду - тыкайте '/help'.
                            2. Могу предложить вам рандомный фильм по выбранному жанру (используйте команду '/choose_genre').
                            3. ...

                            Это пока всё, что я могу сделать. Но вы не расстраивайтесь! =)
                            Я нахожусь в стадии почти-ежедневного обновления, и в будущем у меня будет гораздо больше команд.
                            Итак, что же вы хотите?
                            """;
        }
        return message;
    }

    @SneakyThrows
    private void handleMessage(Message message) {
        if (message.hasText() && message.hasEntities()){
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command =
                        message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command) {
                    case "/start" -> execute(SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text(executeMessageByKey("startCommand", -1))
                            .build());
                    case "/choose_genre" -> {
                        ArrayList<String> genres = getAllGenres();
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        for (int i = 0; i < genres.size(); i += 2) {
                            buttons.add(Arrays.asList(
                                    InlineKeyboardButton.builder()
                                            .text(genres.get(i)).callbackData(Integer.toString(i)).build(),
                                    InlineKeyboardButton.builder()
                                            .text(genres.get(i + 1)).callbackData(Integer.toString(i + 1)).build()
                            ));
                        }
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text(executeMessageByKey("choosingGenreCommand", -1))
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                .build());
                    }
                    case "/help" -> execute(SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text(executeMessageByKey("helpCommand", -1))
                            .build());
                    default -> execute(SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text(executeMessageByKey("errorInputCommand", -1))
                            .build());
                }
            }

        }
        else {
            if (message.hasText())
                execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(executeMessageByKey("randomTextInputCommand", -1) + message.getText())
                    .build());
            else
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text(executeMessageByKey("randomInputCommand", -1))
                        .build());
        }
    }
}
