import java.io.*;
import java.util.*;

public class Wordle {
    // Declaring variables and arrayLists
    protected String secretWordListFileName;
    protected String secretWord;
    protected List<String> secretWordList;
    protected List<String> greenLetters = new ArrayList<>();
    protected List<String> yellowLetters = new ArrayList<>();
    protected List<String> purpleLetters = new ArrayList<>();
    protected List<String> userGuesses = new ArrayList<>();
    protected String youWonMessage;
    protected String youLostMessage;
    
    // Declaring the background colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001b[45m";
    public static final String ANSI_CLEAR_SCREEN = "\033[H\033[2J";

    /**
     * Default constructor that sets simple local variables
     */
    public Wordle() {
        secretWordListFileName = "dictionary.txt"; // DON'T CHANGE THIS
        youWonMessage = "Congratulations! You won! :)"; // You can change this
        youLostMessage = "You Lost and you are bad :( The word was: "; // you can change this
    }

    /**
     * Gameplay method that controls the flow of the game.
     */
    public void play () {
        // Reading the dictionary file and saving it to an ArrayList
        secretWordList =  readDictionary();
        // Selecting a random word from the dictionary
        secretWord = getRandomWord(secretWordList);
        // Instructions to the game
        this.printInstructions();
        // ask the user for the first guess
        this.askForFirstGuess();
        // main gameplay loop
        this.loopThroughSixGuesses(secretWordList);
    }

    /**
     * Prints basic instructions for the user once at the beginning of the game.
     */
    public void printInstructions() {
        System.out.println("The game has chosen a 5-letter word for you to guess.");
        System.out.println("You have 6 tries. In each guess, the game will confirm which letters the chosen word and the guessed word have in common:");
        System.out.println("- Letters highlighted in " + ANSI_GREEN_BACKGROUND + "green" + ANSI_RESET + " are in the correct place.");
        System.out.println("- Letters highlighted in " + ANSI_YELLOW_BACKGROUND + "yellow" + ANSI_RESET + " appear in the chosen word but in a different spot.");
        System.out.println("- Letters highlighted in " + ANSI_PURPLE_BACKGROUND + "purple" + ANSI_RESET + " do not appear in the chosen word.");
    }

    // ask the user for their first guess
    public void askForFirstGuess() {
        System.out.println();
        System.out.println("Please write down your first guess:");
    }

    /***********************************************************
     *  Begin Assignment
     */

    /**
     * Main gameplay loop that asks for the six guesses from the user
     * @param wordList the list of words with the possible answer
     */
    public void loopThroughSixGuesses(List<String> wordList) {
        // Loop through the six guesses
            // print the guess number with ')'
            // obtain a valid user guess and save it
            // analyze the valid users guess and add it to userGuesses
            // print the keyboard and guesses
            // check if the user won: the userWord is the same as secretWord (case?)
                // print the winning message and definition link
                // exit the program with System.exit(0);
        // print the loosing message and the definition link 
        for (int tries = 1; tries <= 6; tries++) {
          String guess = obtainValidUserWord(wordList, tries);
          guess = guess.toUpperCase();
          System.out.println(tries + ") " + guess);
          userGuesses.add(guess);
          printGuesses();
          printColoredKeyboard();
          if (guess.equals(secretWord.toUpperCase())) {
            System.out.println(youWonMessage);
            printDefinitionLink(secretWord);
            System.out.println("Thanks for playing!");
            System.exit(0);
          }
        }
        System.out.println(youLostMessage + secretWord.toUpperCase());
        printDefinitionLink(secretWord);
        System.out.println("Thanks for playing!");
    }

    /**
     * Obtains the users guess and validates it against 2 criteria
     * 1) The user's guess has exactly 5 characters
     * 2) The user's guess is within the wordList provided
     * For each failure scenario, this method prints a unique error message
     * and prompts the user to input another guess.
     * @param wordList the list of words that the user's word must be in
     * @param index the guess index of the user's input
     * @return a valid user guess in all capital letters
     */
    public String obtainValidUserWord (List<String> wordList, int index) {
      // Create a Scanner object
      // Read user input as UpperCase
      // check the length of the word and if it exists
        // hint, a while loop will help
      // return the valid guess
      Scanner scan = new Scanner(System.in);
      String word = "";
      while (true) {
        if (index <= 6) {
          word = scan.nextLine();
          word = word.toUpperCase();
        }
        if (word.length() == 5) {
          for(int idx = 0; idx < wordList.size(); idx++) { 
            if (wordList.get(idx).toUpperCase().equals(word)) {
              return word;
            }
          }
          System.out.println("That word isn't in our dictionary. Try again.");
        }
        else if (word.length() < 5) {
          System.out.println("That word is too short. Please input a 5 letter word.");
        }
        else if (word.length() > 5) {
          System.out.println("That word is too long. Please input a 5 letter word.");
        }
      }
    }
    

    /**
     * Retrieves a random word from the word list
     * @param wordList the list of possible answer words
     * @return a random string from the list
     */
    public String getRandomWord(List<String> wordList) {
      return wordList.get((int)(Math.random() * 2519) + 1);
    }

    /**
     * End Assignment
     *********************************************************************/

    /**
     * Converts the user's guessed word into a word with appropriate colors,
     * and adds the letters to the appropriate world list
     * @param secretWord secret word to compare against
     * @param userGuess the user's word to be analyed
     * @return the user's word with the correct collors in the background
     */
    public String analyzeUserGuess(String secretWord, String userGuess) {
        StringBuilder reColoredWord = new StringBuilder();
        ArrayList<String> secretWordAsList = wordToList(secretWord);
        ArrayList<String> userWordAsList = wordToList(userGuess);
        for (int i = 0; i < userWordAsList.size(); i++) {
            String letter = userWordAsList.get(i);
            int letterIndex = secretWordAsList.indexOf(letter.toLowerCase());
            if(letterIndex >= 0) {
                if (letterIndex == i) {
                    reColoredWord.append(ANSI_GREEN_BACKGROUND).append(letter).append(ANSI_RESET);
                    greenLetters.add(letter.toUpperCase());
                    yellowLetters.removeIf(s -> greenLetters.contains(s));
                    secretWordAsList.set(letterIndex, "");
                } else {
                    reColoredWord.append(ANSI_YELLOW_BACKGROUND).append(letter).append(ANSI_RESET);
                    yellowLetters.add(letter.toUpperCase());
                    secretWordAsList.set(letterIndex, "");
                }
            } else {
                reColoredWord.append(ANSI_PURPLE_BACKGROUND).append(letter).append(ANSI_RESET);
                purpleLetters.add(letter.toUpperCase());
                purpleLetters.removeIf(c -> greenLetters.contains(c));
                purpleLetters.removeIf(c -> yellowLetters.contains(c));
            }
        }
        return reColoredWord.toString();
    }

    /**
     * Converts a String to an ArrayList of single character Strings
     * @param word word to be converted
     * @return an ArrayList with each character from word as it's own
     *          String in the list
     */
    public ArrayList<String> wordToList(String word) {
        ArrayList<String> characterList = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            characterList.add(word.substring(i,i+1));
        }
        return characterList;
    }

    /**
     * Prints the list of recolored user guesses to the screen
     */
    public void printGuesses() {
        System.out.println(ANSI_CLEAR_SCREEN);
        System.out.flush();
        this.printInstructions(); //Cecilia added this so that the user can see the instructions throughout the game.
        System.out.println();
        int i = 1;
        for (String guess : userGuesses) {
            System.out.println( i++ + ") " + analyzeUserGuess(secretWord, guess)/*Cecilia changed this. I want it to still display the green, yellow, and gray colors on each word when it reprints, just like wordle. */);
        }
    }

    /**
     * Prints a facsimile keyboard with the letters appropriately colored based on user guesses
     */
    public void printColoredKeyboard() {
        String qwerty =
                "Q W E R T Y U I O P\n" +
                " A S D F G H J K L\n" +
                "  Z X C V B N M";
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < qwerty.length(); i++) {
            String letter = qwerty.substring(i,i +1);
            if (greenLetters.contains(letter)) {
                output.append(ANSI_GREEN_BACKGROUND).append(letter).append(ANSI_RESET);
            } else if (yellowLetters.contains(letter)) {
                output.append(ANSI_YELLOW_BACKGROUND).append(letter).append(ANSI_RESET);
            } else if (purpleLetters.contains(letter)) {
                output.append(ANSI_PURPLE_BACKGROUND).append(letter).append(ANSI_RESET);
            } else {
                output.append(letter);
            }
        }
        System.out.println("\n\n===================");
        System.out.println(output);
        System.out.println("===================");
    }

    /**
     * Prints ths dictionary.com link to the definition of the random secrete word
     * at the end of the round
     * @param randomChosenWord the secret word
     */
    public void printDefinitionLink (String randomChosenWord) { // prints the link to the dictionary definition of the chosen word
        System.out.println("The word's definition: https://www.merriam-webster.com/dictionary/" + randomChosenWord);
    }

    /**
     * Reads the dictionary.txt file and adds all the words to a list
     * @return a list of words that the answer could come from
     */
    public List<String> readDictionary() {
        List<String> wordList = new ArrayList<>();
        try {
            File file = new File(secretWordListFileName);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                wordList.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading in the dictionary.");
            e.printStackTrace();
        }
        return wordList;
    }
}