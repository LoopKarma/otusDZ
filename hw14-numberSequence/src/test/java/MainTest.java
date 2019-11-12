import org.junit.jupiter.api.RepeatedTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainTest {

    @RepeatedTest(10)
    void run() {
        Main main = new Main();
        Map<Integer, StringBuilder> result = main.run();

        String expected = "1 2 3 4 5 6 7 8 9 10 9 8 7 6 5 4 3 2 1 2 3 4";
        int length = expected.length();

        assertEquals(result.size(), 2);
        assertEquals(result.get(1).substring(0, length), expected);
        assertEquals(result.get(2).substring(0, length), " " + expected.substring(0, length - 1));
    }
}