import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HangmanTest
{
    Hangman hangman;

    @BeforeEach
    void setUp()
    {
        hangman = new Hangman("test");
    }

    @Test
    void getAnswer()
    {
        assertEquals(hangman.getAnswer(), "test");
    }

    @Test
    void getAttempts()
    {
        assertEquals(hangman.getAttempts(), 0);
    }

    @Test
    void testInput()
    {
        hangman.testInput("t");
        assertTrue(hangman.getRightguesses().contains("t"));
        assertFalse(hangman.getWrongguesses().contains("t"));

        hangman.testInput("w");
        assertTrue(hangman.getWrongguesses().contains("w"));
        assertFalse(hangman.getRightguesses().contains("w"));

        assertEquals(hangman.getAnswer(), "test");
    }

    @Test
    void testCheckWin()
    {
        String answer[] = {"t", "e", "s", "t"};
        List<String> guesses = new ArrayList<>();
        guesses.add("t");
        assertFalse(hangman.testCheckWin(answer, guesses));
        guesses.add("e");
        guesses.add("s");
        guesses.add("t");
        assertTrue(hangman.testCheckWin(answer, guesses));
    }

    @AfterEach
    void tearDown()
    {

    }
}