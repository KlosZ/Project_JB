import static org.junit.jupiter.api.Assertions.assertEquals;
import com.devtools.mylib.CinemaBotClass;
import com.devtools.mylib.GetAllCities;
import com.devtools.mylib.GetCinemas;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class TestingBotClass {

    @BeforeEach
    void setUp() { // ?
    }

    @Test
    void testGenerateWrongCommandInput() {
        assertEquals("Команда введена не верно. Попробуйте снова. ",
                new CinemaBotClass().executeMessageByKey("errorInputCommand", -1));
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
    void testGenerateChoosingGenreCommandInput() {
        assertEquals("Пожалуйста, выберите жанр фильма, который хотите посмотреть. ",
                new CinemaBotClass().executeMessageByKey("choosingGenreCommand", -1));
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
    void testGenerateNonRealizedCommandInput() {
        assertEquals("Команда еще не реализована! Попробуйте позже!",
                new CinemaBotClass().executeMessageByKey("notRealizedCommand", -1));
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
    void testGenerateChoosingPlaceCommandInput() {
        assertEquals("Ну и где же вы хотите посмотреть фильмец?",
                new CinemaBotClass().executeMessageByKey("сhoosingPlaceCommand", -1));
    }

    @Test
    @SneakyThrows
    void testGenerateGetAllCities() {
        Map<String, String> rightMap = new HashMap<>();
        rightMap.put("Москва", "msk");
        rightMap.put("Екатеринбург", "ekaterinburg");
        rightMap.put("Великий Новгород", "velikiy-novgorod");
        rightMap.put("Санкт-Петербург", "spb");
        // File file = new File("C:/Users/Егор/IdeaProjects/CBotJava/src/main/resources/for_GetAllCities.htm");
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
}
