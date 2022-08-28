/*

 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Hangman
{
    private String name;
    private ArrayList<String> wordlist;
    private ArrayList<String> art;
    private List<String> scores;
    private ArrayList<String> wrongguesses;
    private ArrayList<String> rightguesses;
    private String answer;
    private String[] answerArray;
    private int attempts;
    private Scanner in;


    public String getAnswer()
    {
        return answer;
    }

    public ArrayList<String> getRightguesses()
    {
        return rightguesses;
    }

    public ArrayList<String> getWrongguesses()
    {
        return wrongguesses;
    }

    public int getAttempts()
    {
        return attempts;
    }


    /**
     * Default hangman constructor. Will randomly select the secret word.
     */
    public Hangman()
    {
        this.in = new Scanner(System.in);
        this.rightguesses = new ArrayList<>();
        this.wrongguesses = new ArrayList<>();
        this.wordlist = addWords();
        this.answer = wordlist.get(new Random().nextInt(wordlist.size() - 1));
        this.answerArray = answer.split("");
        this.art = new ArrayList<>();
        try
        {
            URL resource = getClass().getResource("art.txt");
            Scanner input = new Scanner(Paths.get(resource.toURI()).toFile());
            input.useDelimiter("#");
            addArt(input);

            resource = getClass().getResource("scores.txt");
            this.scores = Files.readAllLines(Paths.get(resource.toURI()));
            input.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public Hangman(String answer)
    {
        this.in = new Scanner(System.in);
        this.rightguesses = new ArrayList<>();
        this.wrongguesses = new ArrayList<>();
        this.wordlist = addWords();
        this.answer = answer;
        this.answerArray = answer.split("");
        this.art = new ArrayList<>();
        try
        {
            URL resource = getClass().getResource("art.txt");
            Scanner input = new Scanner(Paths.get(resource.toURI()).toFile());
            input.useDelimiter("#");
            addArt(input);

            resource = getClass().getResource("scores.txt");
            this.scores = Files.readAllLines(Paths.get(resource.toURI()));
            input.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     *  Starts the hangman game.
     */
    public void start()
    {
        attempts = 0;
        System.out.println("HANGMAN \n");
        try
        {
            System.out.println("Enter a name: ");
            this.name = in.nextLine().trim();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        while (attempts < 7)
        {
            System.out.println(gameState(attempts));
            System.out.println(wordState(answerArray, rightguesses));
            checkGuess(getInput());

            if (checkWin(answerArray, rightguesses))
            {
                System.out.println(gameState(attempts));
                System.out.println(wordState(answerArray, rightguesses));
                System.out.println("Yes! The secret word is \"" + answer + "\"! You have won!");
                break;
            }
        }
        if (attempts == 7)
        {
            System.out.println(gameState(attempts));
            System.out.println("Aw... You lost.. Better luck next time!");
        }
        if (scores.isEmpty() || Integer.parseInt(getHighScore().split(" ")[0]) < getScore() )
        {
            System.out.println(name + " you got a new high score of " + getScore());
        }

        try
        {
            URL resource = getClass().getResource("scores.txt");
            FileWriter writer = new FileWriter(Paths.get(resource.toURI()).toFile(), true);
            writer.write(getScore() + " " + name);
            writer.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        in.close();
    }

    /**
     *
     * @param state the current number of attempted guesses
     *              that were wrong
     * @return the string value of the current state.
     */
    private String gameState(int state)
    {
        StringBuilder result = new StringBuilder();
        switch (state)
        {
            case 1 -> result.append(art.get(1));
            case 2 -> result.append(art.get(2));
            case 3 -> result.append(art.get(3));
            case 4 -> result.append(art.get(4));
            case 5 -> result.append(art.get(5));
            case 6 -> result.append(art.get(6));
            case 7 -> result.append(art.get(7));
            default -> result.append(art.get(0));
        }
        return result.toString();
    }

    /**
     *
     * @param answer the correct word as an array of strings
     * @param guesses the list of correct guesses
     * @return returns a string that lists the
     */
    private String wordState(String[] answer, List<String> guesses)
    {
        StringBuilder result = new StringBuilder();

        result.append(
                Arrays.stream(answer).map(s ->
                {
                    if (guesses.contains(s))
                    {
                        return s;
                    }
                    else
                    {
                        return " _ ";
                    }
                }).collect(Collectors.joining()));

        result.append("\n\nMissed Letters: ");
        result.append(wrongguesses);

        return result.toString();
    }

    /**
     *
     * @return returns the user input as a string.
     */
    private String getInput()
    {
        String result = "";
        try
        {
            System.out.println("Please enter your next guess: ");
            result = in.nextLine().toLowerCase().trim();
            if (result.length() > 1 || rightguesses.contains(result) || wrongguesses.contains(result)
                    || !result.matches("[a-z]+"))
            {
                System.out.println("Invalid input.");
                result = getInput();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return result;
    }

    /**
     *
     * @param str character as a string to be checked against the correct word
     */
    private void checkGuess(String str)
    {
        int count = (int)Arrays.stream(answerArray).filter(s -> s.contains(str)).count();
        switch (count) {
            case 0 ->
            {
                wrongguesses.add(str);
                attempts++;
            }
            default -> rightguesses.add(str);

        }
    }

    /**
     *
     * @param answer the correct word as an array of characters
     * @param guesses list of correct guesses that have been made
     * @return
     */
    private boolean checkWin(String[] answer, List<String> guesses)
    {
        return Arrays.stream(answer).allMatch(guesses::contains);
    }

    /**
     *
     * @param input scanner that is being used to read the art file
     */
    private void addArt(Scanner input)
    {
        art.add(input.next());
        if (input.hasNext())
        {
            addArt(input);
        }
    }

    /**
     * method to add words so adding words from another location would
     * be easier to implement in the future.
     * @return an arraylist with words that can be used as the secret word.
     */
    private ArrayList<String> addWords()
    {
        ArrayList<String> result = new ArrayList<>();
        result.add("test");
        result.add("blue");
        result.add("key");
        result.add("canon");
        result.add("theory");
        result.add("valve");
        result.add("botany");
        result.add("electricity");
        result.add("element");
        result.add("gravity");
        result.add("mass");
        result.add("bed");
        result.add("coffee");
        result.add("camera");
        result.add("girl");
        result.add("boy");
        result.add("history");
        result.add("information");
        result.add("include");
        result.add("snow");
        return result;
    }

    /**
     *
     * @return returns the users score during the game.
     */
    private int getScore()
    {
        if (attempts == 7)
        {
            return rightguesses.size();
        }
        return rightguesses.size() + 10;
    }

    /**
     *
     * @return returns the previous high score.
     */
    private String getHighScore()
    {
        return scores.stream().max(Comparator.comparing(String::valueOf)).get();
    }

    /**
     * Method used for testing to bypass the normal user input method.
     * @param str character to be tested against the secret word.
     */
    public void testInput(String str)
    {
        checkGuess(str);
    }

    /**
     * Method for testing the win state method.
     * @param answer the answer as an array of string characters.
     * @param guesses list of the correct guesses.
     * @return will return true if the win conditions are met.
     */
    public boolean testCheckWin(String[] answer, List<String> guesses)
    {
        return checkWin(answer, guesses);
    }
}