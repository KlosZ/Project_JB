import static org.junit.jupiter.api.Assertions.assertEquals;
import com.devtools.mylib.TestBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class TestingBotClass {
    TestBot bot;
    String message;

    @BeforeEach
    void setUp() {
        bot = new TestBot();
        // ?
    }

    @Test
    void testGenerateWrongCommand() {
        assertEquals("Команда введена не верно. Попробуйте снова. ", message);
    }
}
