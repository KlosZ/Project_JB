import static org.junit.jupiter.api.Assertions.assertEquals;
import com.devtools.mylib.CinemaBotClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestingBotClass {

    @BeforeEach
    void setUp() { // ?
    }

    @Test
    void testGenerateWrongCommandInput() {
        assertEquals("Команда введена не верно. Попробуйте снова. ",
                CinemaBotClass.executeMessageByKey("errorInputCommand", -1));
    }

    @Test
    void testGenerateTextInput() {
        assertEquals("Вы отправили: ",
                CinemaBotClass.executeMessageByKey("randomTextInputCommand", -1));
    }

    @Test
    void testGenerateNonTextInput() {
        assertEquals("Вы отправили не текстовое сообщение. " +
                        "Я же бот, а не нейросеть, чтобы распознавать, что вы мне отправили. =)",
                CinemaBotClass.executeMessageByKey("randomInputCommand", -1));
    }

    @Test
    void testGenerateStartCommandInput() {
        assertEquals("""
                            Здравствуй, уважаемый пользователь! Добро пожаловать в K&K's CinemaBot!
                            Этот бот поможет вам с выбором фильма на вечер.
                            Чуть ниже, выберите, где вы будете смотреть кинцо?
                            
                            В любой момент, вы можете обратиться по команде '/help',
                            если не знаешь что и как. Действуй =)
                            """,
                CinemaBotClass.executeMessageByKey("startCommand", -1));
    }

    @Test
    void testGenerateChoosingGenreCommandInput() {
        assertEquals("Пожалуйста, выберите жанр фильма, который хотите посмотреть. ",
                CinemaBotClass.executeMessageByKey("choosingGenreCommand", -1));
    }

    @Test
    void testGenerateHelpCommandInput() {
        assertEquals("""
                            Итак, что же может этот ваш бот (то есть, я)?
                            
                            1. /help
                            Команда, которая должна быть в каждом боте. Помогает с навигацией по боту.
                            
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
                            """, CinemaBotClass.executeMessageByKey("helpCommand", -1));
    }

    @Test
    void testGenerateNonRealizedCommandInput() {
        assertEquals("Команда еще не реализована! Попробуйте позже!",
                CinemaBotClass.executeMessageByKey("notRealizedCommand", -1));
    }

    @Test
    void testGenerateAboutDevsCommandInput() {
        assertEquals("""
                            K&K's CinemaBot...
                            Что за K&K? Всё просто - это ники разработчиков. Встречайте бурными овациями =)
                            
                            kretatusha - Стас Михайлов, TG: @mikhstas
                            KlosZ - Зыков Егор, TG: @EgorZykov
                            На момент 2021 - 2022 учебного года оба студенты 2 курса МатМеха (ИЕНиМ) УрФУ.
                            """, CinemaBotClass.executeMessageByKey("aboutDevsCommand", -1));
    }

    @Test
    void testGenerateChoosingPlaceCommandInput() {
        assertEquals("Ну и где же вы хотите посмотреть фильмец?",
                CinemaBotClass.executeMessageByKey("сhoosingPlaceCommand", -1));
    }
}
