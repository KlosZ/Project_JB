import static org.junit.jupiter.api.Assertions.assertEquals;
import com.devtools.mylib.CinemaBotClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestingBotClass {
    CinemaBotClass bot;
    String message;

    @BeforeEach
    void setUp() {
        bot = new CinemaBotClass();
        // ?
    }

    @Test
    void testGenerateWrongCommand() {
        assertEquals("Команда введена не верно. Попробуйте снова. ", message);
    }
}
