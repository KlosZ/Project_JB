import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.devtools.mylib.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TestingBotClass {

    @Test
    void testGenerateStartCommandInput() {
        assertEquals("""
                            Здравствуй, уважаемый пользователь! Добро пожаловать в K&K's CinemaBot!
                            Этот бот поможет вам с выбором фильма на вечер.
                            Для начала следующим сообщением напишите, из какого города вы будете смотреть кино?
                            
                            P.S. В любой момент, вы можете обратиться по команде '/help', если не знаешь что и как. Действуй =)
                            """,
                new CinemaBotClass().executeMessageByKey("startCommand", -1));
    }

    @Test
    void testGenerateHelpCommandInput() {
        assertEquals("""
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
                            """, new CinemaBotClass().executeMessageByKey("helpCommand", -1));
    }

    @Test
    void testGenerateAboutDevsCommandInput() {
        assertEquals("""
                            K&K's CinemaBot...
                            Что за K&K? Всё просто - это ники разработчиков. Встречайте бурными овациями =)
                            
                            kretatusha - Стас Михайлов, TG: @mikhstas
                            KlosZ - Зыков Егор, TG: @EgorZykov
                            На момент 2021 - 2022 учебного года оба студенты 2 курса МатМеха (ИЕНиМ) УрФУ.
                            """, new CinemaBotClass().executeMessageByKey("aboutDevsCommand", -1));
    }

    @Test
    void testGenerateTextInput() {
        assertEquals("Вы отправили: ",
                new CinemaBotClass().executeMessageByKey("randomTextInputCommand", -1));
    }

    @Test
    void testGenerateNonTextInput() {
        assertEquals("Вы отправили не текстовое сообщение. " +
                        "Я же бот, а не нейросеть, чтобы распознавать, что вы мне отправили. =)",
                new CinemaBotClass().executeMessageByKey("randomInputCommand", -1));
    }

    @Test
    void testGenerateNonRealizedCommandInput() {
        assertEquals("Команда еще не реализована! Попробуйте позже!",
                new CinemaBotClass().executeMessageByKey("notRealizedCommand", -1));
    }

    @Test
    void testGenerateWrongCommandInput() {
        assertEquals("Команда введена не верно. Попробуйте снова. ",
                new CinemaBotClass().executeMessageByKey("errorInputCommand", -1));
    }

    @Test
    void testGenerateChoosingPlaceCommandInput() {
        assertEquals("Ну и где же вы хотите посмотреть фильмец?",
                new CinemaBotClass().executeMessageByKey("сhoosingPlaceCommand", -1));
    }

    @Test
    void testGenerateChoosingGenreCommandInput() {
        assertEquals("Пожалуйста, выберите жанр фильма, который хотите посмотреть. ",
                new CinemaBotClass().executeMessageByKey("choosingGenreCommand", -1));
    }

    @Test
    void testGenerateChoosingCityCommandInput() {
        assertEquals("Введите название города (с большой буквы, без спец. символов и цифр и желательно без синтаксических ошибок). Пример ввода:\nЕкатеринбург",
                new CinemaBotClass().executeMessageByKey("choosingCityCommand", -1));
    }

    @Test
    void testGenerateChoosingFilmCommandInput() {
        assertEquals("Итак, какой же вы фильм хотите посмотреть?\nВведите его название полностью без ошибок =)",
                new CinemaBotClass().executeMessageByKey("choosingFilmCommand", -1));
    }

    @Test
    void testGenerateWrongFilmInput() {
        assertEquals("Хмм. Я такого фильма не нашел. Попробуйте снова!",
                new CinemaBotClass().executeMessageByKey("wrongFilmInputCommand", -1));
    }

    @Test
    @SneakyThrows
    void testGenerateGetAllCities() {
        Map<String, String> rightMap = new HashMap<>();
        rightMap.put("Москва", "msk");
        rightMap.put("Екатеринбург", "ekaterinburg");
        rightMap.put("Великий Новгород", "velikiy-novgorod");
        rightMap.put("Санкт-Петербург", "spb");
        File file = new File("src/main/resources/for_GetAllCities.htm");
        BufferedReader in = new BufferedReader(new FileReader(file));
        Map<String, String> actualMap = GetAllCities.analyzingInputData(in);
        assertEquals(actualMap.size(), rightMap.size());
        assertEquals(actualMap.get("Москва"), rightMap.get("Москва"));
    }

    @Test
    @SneakyThrows
    void testGenerateGetCinemas() {
        Map<String, String> rightMap = new HashMap<>();
        rightMap.put("Гринвич Синема", "/ekaterinburg/cinema/titanik-sinema-361");
        rightMap.put("Пассаж Синема", "/ekaterinburg/cinema/passazh-sinema-56070");
        rightMap.put("Дом кино", "/ekaterinburg/cinema/dom-kino-3633");
        rightMap.put("Синема Парк Алатырь", "/ekaterinburg/cinema/sinema-park-starlayt-1869");
        File file = new File("src/main/resources/for_GetCinemas.htm");
        BufferedReader in = new BufferedReader(new FileReader(file));
        Map<String, String> actualMap = GetCinemas.analyzingInputData(in, "ekaterinburg");
        assertEquals(actualMap.size(), rightMap.size());
        assertEquals(actualMap.get("Гринвич Синема"), rightMap.get("Гринвич Синема"));
    }

    @Test
    @SneakyThrows
    void testGenerateGetCinemasByMovie() {
        String rightString = """
                ближайшие сеансы есть 2021.11.30 в этих кинотеатрах:

                Гринвич Синема
                - Сеансы 2D от 300\s
                19:30
                - Сеансы Dolby Atmos от 300\s
                20:30

                Пассаж Синема
                - Сеансы 2D от 300\s
                19:10
                - Сеансы Dolby Atmos от 300\s
                20:10

                Дом кино
                - Сеансы 2D от 260\s
                17:40

                Синема Парк Алатырь
                - Сеансы IMAX 2D от 370\s
                15:30, 20:25

                Премьер Зал Фан Фан
                - Сеансы 2D от 190\s
                17:40
                - Сеансы 2D от 250\s
                17:00, 19:40

                Премьер Зал Парк Хаус
                - Сеансы 2D от 220\s
                15:05, 19:55

                Континент Синема
                - Сеансы 2D от 160\s
                17:15, 19:45

                Киноплекс на Халтурина
                - Сеансы 2D от 200\s
                13:10, 18:35

                Премьер Зал Гранат
                - Сеансы 2D от 190\s
                14:20, 19:35

                Кинодом
                - Сеансы 2D от 220\s
                16:55, 18:20
                - Сеансы 2D от 220\s
                16:05, 18:35

                Премьер Зал Омега
                - Сеансы 2D от 220\s
                15:20, 20:05

                Киноград
                - Сеансы 2D от 160\s
                19:30""";
        File file = new File("src/main/resources/for_GetCinemasByMovies.htm");
        BufferedReader in = new BufferedReader(new FileReader(file));
        String actualString = GetCinemasByMovie.analyzingInputData(in);
        assertEquals(actualString, rightString);
    }

    @Test
    @SneakyThrows
    void testGenerateGetMoviesFromCinema() {
        String rightString = """
                'Энканто' (Мультфильм, Приключение, Фэнтези) (6+)
                - Сеансы 2D от 160\s
                10:45, 12:25, 12:55, 13:25, 14:35, 15:05, 15:35, 16:45, 17:15, 17:45, 19:25, 19:55, 20:25
                - Сеансы 3D от 160\s
                11:15, 18:55, 14:00, 16:10
                - Сеансы Dolby Atmos от 160\s
                11:50, 18:20

                'Обитель Зла: Раккун Сити' (Детектив, Боевик, Фантастика) (18+)
                - Сеансы 2D от 160\s
                10:30, 11:35, 13:45, 14:40, 15:55, 18:00, 19:30, 21:40, 22:35, 00:05, 00:50, 02:00

                'Прошлой ночью в Сохо' (Триллер, Ужасы, Драма) (18+)
                - Сеансы 2D от 220\s
                12:20, 16:50, 18:05, 19:10, 20:10, 22:05, 22:55, 01:15, 02:25

                'Вечные' (Боевик, Приключение, Драма) (18+)
                - Сеансы 2D от 160\s
                12:40, 13:45, 16:30, 19:00, 21:05, 00:00, 00:45

                'Французский вестник. Приложение к газете "Либерти. Канзас ивнинг сан"' (Комедия, Мелодрама, Трагикомедия) (18+)
                - Сеансы 2D от 220\s
                12:45, 14:20, 15:40, 17:50, 19:20, 21:30, 22:30, 23:50

                'Последняя дуэль' (Драма) (18+)
                - Сеансы 2D от 300\s
                13:30, 16:25, 20:00, 21:55
                - Сеансы Dolby Atmos от 220\s
                22:55, 01:50

                'Король Ричард' (Биография, Драма) (12+)
                - Сеансы 2D от 220\s
                16:45, 21:30, 00:15

                'Небо' (Драма, Военный) (12+)
                - Сеансы 2D от 160\s
                11:50, 23:40, 00:40, 02:10

                'Не время умирать' (Боевик, Приключение, Триллер) (12+)
                - Сеансы 2D от 160\s
                14:55, 21:35, 00:40

                'Веном 2' (Боевик, Триллер, Фантастика) (16+)
                - Сеансы 2D от 160\s
                22:00, 00:25, 02:15

                'Кощей. Начало' (Мультфильм, Приключение, Семейный) (6+)
                - Сеансы 2D от 160\s
                11:55

                'Семейка Аддамс: Горящий тур' (Мультфильм, Приключение, Фэнтези) (12+)
                - Сеансы 2D от 300\s
                11:35

                'Купе номер 6' (Мелодрама, Драма) (16+)
                - Сеансы 2D от 350\s
                14:40, 16:50

                'Охотники за привидениями: Наследники' (Боевик, Фантастика, Комедия) (12+)
                - Сеансы 2D от 300\s
                19:30
                - Сеансы Dolby Atmos от 300\s
                20:30

                'Неисправимый Рон' (Мультфильм, Приключение, Комедия) (6+)
                - Сеансы 2D от 350\s
                12:30""";
        File file = new File("src/main/resources/for_GetMoviesFromCinema.htm");
        BufferedReader in = new BufferedReader(new FileReader(file));
        String actualString = GetMoviesFromCinema.analyzingInputData(in);
        assertEquals(actualString, rightString);
    }

    @Test
    @SneakyThrows
    void testGenerateGetGenresIMDb() {
        String regexString = "[a-zA-Zа-яА-Я0-9:\\-. ]+\\([\\d]{4}\\)";
        //for (int i = 0; i < 100; i++) {
            String movie = GetGenresIMDb.findMovie("action");
            assertTrue(Pattern.compile(regexString).matcher(movie).find());
        //}
    }
}
