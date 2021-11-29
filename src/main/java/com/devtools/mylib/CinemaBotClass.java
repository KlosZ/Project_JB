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

import java.util.*;

public class CinemaBotClass extends TelegramLongPollingBot {

    private String flagButton = "";
    private Boolean flagIsClicked = false;
    String chosenCity = "";
    private Boolean flagAccessToWriteCity = false;

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
        if (update.hasCallbackQuery() && flagIsClicked) {
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
        switch (flagButton) {
            case "choiceGenre" -> execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(executeMessageByKey("callbackChoosingGenreCommand", data))
                    .build());
            case "choiceHomeCinema" -> {
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text(executeMessageByKey("callbackChoosingPlaceCommand", data))
                        .build());
                flagIsClicked = false;
            }
            case "choiceCinema" -> execute(SendMessage.builder()
                    .chatId(message.getChatId().toString())
                    .text(executeMessageByKey("callbackChoosingCinemaCommand", data))
                    .build());
        }
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

    public static String getPlace(Integer i) {
        return (i == 0) ? "дома" : "в кинотеатре";
    }

    @SneakyThrows
    public String executeMessageByKey(String key, int data) {
        String message = "";
        switch (key) {
            case "startCommand" ->
                    message = """
                            Здравствуй, уважаемый пользователь! Добро пожаловать в K&K's CinemaBot!
                            Этот бот поможет вам с выбором фильма на вечер.
                            Для начала следующим сообщением напишите, из какого города вы будете смотреть кино?
                            
                            P.S. В любой момент, вы можете обратиться по команде '/help', если не знаешь что и как. Действуй =)
                            """;
            case "choosingGenreCommand" ->
                    message = "Пожалуйста, выберите жанр фильма, который хотите посмотреть. ";
            case "callbackChoosingGenreCommand" -> {
                ArrayList<String> genres = getAllGenres();
                String movieGenre = genres.get(data);
                message = "Отличный выбор! Вот фильм жанра '" + movieGenre +
                        "', который вы можете посмотреть:\n" + ReadFromSite.findMovie(movieGenre);
            }
            case "сhoosingPlaceCommand" ->
                    message = "Ну и где же вы хотите посмотреть фильмец?";
            case "callbackChoosingPlaceCommand" -> {
                if (getPlace(data).equals("дома"))
                    message = "Итак вы выбрали смотреть фильм дома. Тык сюда -> '/choose_genre'";
                else
                    message = "Отлично! Вы выбрали смотреть фильм в кинотеатре. Давайте выберем кинотеатр...\nТык сюда -> '/choose_cinema'";
            }
            case "choosingCinemaCommand" ->
                    message = "Океееей, в г. " + chosenCity + " вот такие кинотеатры, в какой пойдете?";
            case "callbackChoosingCinemaCommand" -> {
                Map<String,String> cinemasAndURLMap = GetCinemas.findCinemas(GetAllCities.getCityURLByCity(chosenCity));
                String cinemaSelected = new ArrayList<>(cinemasAndURLMap.keySet()).get(data);
                String urlSelected = cinemasAndURLMap.get(cinemaSelected);
                message = GetMoviesFromCinema.findMoviesFromCinema(urlSelected);
            }
            case "choosingCityCommand" ->
                    message = "Введите название города (с большой буквы, без спец. символов и цифр и желательно без синтаксических ошибок). Пример ввода:\nЕкатеринбург";
            case "cityInputCommand" ->
                    message = "Вы будете лицезреть фильм из города ";
            case "wrongCityInputCommand" ->
                    message = "Либо в этом городе нет зарегистрированных кинотеатров, либо вы неправильно его ввели. Попробуйте снова!";
            case "errorInputCommand" ->
                    message = "Команда введена не верно. Попробуйте снова. ";
            case "randomTextInputCommand" ->
                    message = "Вы отправили: ";
            case "randomInputCommand" ->
                    message = "Вы отправили не текстовое сообщение. Я же бот, а не нейросеть, чтобы распознавать, что вы мне отправили. =)";
            case "notRealizedCommand" ->
                    message = "Команда еще не реализована! Попробуйте позже!";
            case "helpCommand" ->
                    message = """
                            Итак, что же может этот ваш бот (то есть, я)?
                            
                            0. /help
                            Команда, которая должна быть в каждом боте. Помогает с навигацией по боту.
                            
                            1. /choose_city
                            Необходима в путешествиях по стране, если вы хотите посмотреть фильм, скажем из Костромы. Понятное дело, команда меняет город для поиска кинотеатров в нем.
                            
                            2. /choose_place
                            Чтобы мне знать, что предложить, я должен понять, где вы хотите посмотреть фильм: дома или в кинотеатре.
                            
                            3. /choose_genre
                            Если вы хотите посмотреть фильм дома, то могу предложить вам рандомный фильм по выбранному жанру.
                            
                            4. /choose_cinema
                            Если вы желаете пойти в кинотеатр, используйте эту команду. Она поможет расписанием сеансов на оставшийся день в выбранном вами кинотеатре.
                            
                            5. /about_devs
                            Тыкайте сюда, если хотите узнать чутка о разработчиках этого бота.
                            
                            Это пока всё, что я могу сделать. Но вы не расстраивайтесь! =)
                            Я нахожусь в стадии почти-ежедневного обновления, и в будущем у меня будет гораздо больше команд.
                            Итак, что же вы хотите?
                            """;
            case "aboutDevsCommand" ->
                    message = """
                            K&K's CinemaBot...
                            Что за K&K? Всё просто - это ники разработчиков. Встречайте бурными овациями =)
                            
                            kretatusha - Стас Михайлов, TG: @mikhstas
                            KlosZ - Зыков Егор, TG: @EgorZykov
                            На момент 2021 - 2022 учебного года оба студенты 2 курса МатМеха (ИЕНиМ) УрФУ.
                            """;
        }
        return message;
    }

    private static List<List<InlineKeyboardButton>> getHomeCinemaButtons() {
        List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();
        inlineKeyboardButtons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text("Дома").callbackData(Integer.toString(0)).build(),
                InlineKeyboardButton.builder()
                        .text("В кинотеатре").callbackData(Integer.toString(1)).build()));
        return inlineKeyboardButtons;
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
                    case "/start" -> {
                        flagButton = "choiceHomeCinema";
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text(executeMessageByKey("startCommand", -1))
                                .build());
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text(executeMessageByKey("choosingCityCommand", -1))
                                .build());
                        flagAccessToWriteCity = true;
                    }
                    case "/choose_city" -> {
                        flagAccessToWriteCity = true;
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text(executeMessageByKey("choosingCityCommand", -1))
                                .build());
                    }
                    case "/choose_genre" -> {
                        flagButton = "choiceGenre";
                        ArrayList<String> genres = getAllGenres();
                        List<List<InlineKeyboardButton>> buttonsOfGenres = new ArrayList<>();
                        for (int i = 0; i < genres.size(); i += 2) {
                            buttonsOfGenres.add(Arrays.asList(
                                    InlineKeyboardButton.builder()
                                            .text(genres.get(i)).callbackData(Integer.toString(i)).build(),
                                    InlineKeyboardButton.builder()
                                            .text(genres.get(i + 1)).callbackData(Integer.toString(i + 1)).build()
                            ));
                        }
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text(executeMessageByKey("choosingGenreCommand", -1))
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttonsOfGenres).build())
                                .build());
                        flagIsClicked = true;
                    }
                    case "/choose_cinema" -> {
                        flagButton = "choiceCinema";
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text("Напомню. " + executeMessageByKey("cityInputCommand", -1) + chosenCity + '!')
                                .build());
                        Map<String,String> cinemasAndURLMap = GetCinemas.findCinemas(GetAllCities.getCityURLByCity(chosenCity));
                        ArrayList<String> cinemasList = new ArrayList<>(cinemasAndURLMap.keySet());
                        List<List<InlineKeyboardButton>> buttonsOfCinemas = new ArrayList<>();
                        int n = cinemasList.size();
                        for (int i = 0; i < n; i += 2) {
                            buttonsOfCinemas.add(Arrays.asList(
                                    InlineKeyboardButton.builder()
                                            .text(cinemasList.get(i)).callbackData(Integer.toString(i)).build(),
                                    InlineKeyboardButton.builder()
                                            .text(cinemasList.get(i + 1)).callbackData(Integer.toString(i + 1)).build()
                            ));
                        }
                        if (n % 2 == 1)
                            buttonsOfCinemas.add(List.of(InlineKeyboardButton.builder().text(cinemasList.get(n - 1)).callbackData(Integer.toString(n - 1)).build()));
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text(executeMessageByKey("choosingCinemaCommand", -1))
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttonsOfCinemas).build())
                                .build());
                        flagIsClicked = true;
                    }
                    case "/choose_place" -> {
                        flagButton = "choiceHomeCinema";
                        List<List<InlineKeyboardButton>> buttonsOfPlaces = getHomeCinemaButtons();
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text(executeMessageByKey("сhoosingPlaceCommand", -1))
                                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttonsOfPlaces).build())
                                .build());
                        flagIsClicked = true;
                    }
                    case "/help" -> execute(SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text(executeMessageByKey("helpCommand", -1))
                            .build());
                    case "/about_devs" -> execute(SendMessage.builder()
                            .chatId(message.getChatId().toString())
                            .text(executeMessageByKey("aboutDevsCommand", -1))
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
                if (flagAccessToWriteCity) {
                    if (GetAllCities.findCities().containsKey(message.getText())) {
                        chosenCity = message.getText();
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text(executeMessageByKey("cityInputCommand", -1) + chosenCity + '!')
                                .build());
                        flagAccessToWriteCity = false;
                    }
                    else
                        execute(SendMessage.builder()
                                .chatId(message.getChatId().toString())
                                .text(executeMessageByKey("wrongCityInputCommand", -1))
                                .build());
                }
                else
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
