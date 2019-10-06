import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TheFirstExampleTests {

    @Test
    @DisplayName("Simple assertion test example")
    void myFirstTest() {
        assertEquals(2, 1 + 1);
        System.out.println("=====================");
        System.out.println("The simple test is finished");
        System.out.println("=====================");
    }
}
