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
                            Добро пожаловать в K&K's CinemaBot!
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
                            1. Понятное дело, если забыли конкретную команду - тыкайте '/help'.
                            2. Если вы хотите посмотреть фильм дома, то могу предложить вам рандомный
                               фильм по выбранному жанру (используйте команду '/choose_genre').
                            3. Если вы желаете пойти в кинотеатр, предлагаю вам использовать команду '/choose_cinema',
                               она поможет расписанием сеансов на оставшийся день в выбранном ввами кинотеатре.
                            4. ...
                            
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


}
