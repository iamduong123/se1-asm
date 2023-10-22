package engine;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class Word {

    public static final Set<String> stopWords = new HashSet<>();

// Somewhere in the code (maybe during initialization or in a static block), you should add "of" to the set:

    static {
        stopWords.add("of");
        stopWords.add("the");
        // add other stop words as necessary
    }


    private final String prefix;
    private final String text;
    private final String suffix;

    private Word(String prefix, String text, String suffix) {
        this.prefix = prefix;
        this.text = text;
        this.suffix = suffix;
    }
    public static boolean isStopWord(String word) {
        return stopWords.contains(word.toLowerCase());
    }
    public boolean isKeyword() {
        // Check if the word matches the desired pattern and is not a stop word.
        if (!Pattern.compile("[a-zA-Z'-]+").matcher(text).matches() || text.isEmpty()) {
            return false;
        }

        return !stopWords.contains(text.toLowerCase());
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word word = (Word) o;
        return text.equalsIgnoreCase(word.text);
    }

    @Override
    public String toString() {
        return prefix + text + suffix;
    }

    public static Word createWord(String rawText) {
        int textStart = 0;
        while (textStart < rawText.length() && !Character.isLetterOrDigit(rawText.charAt(textStart))) {
            textStart++;
        }

        int textEnd = textStart;
        while (textEnd < rawText.length() && (Character.isLetterOrDigit(rawText.charAt(textEnd)) || rawText.charAt(textEnd) == '-' || rawText.charAt(textEnd) == '\'')) {
            textEnd++;
        }

        String prefixWord = rawText.substring(0, textStart);
        String textWord = rawText.substring(textStart, textEnd);
        String suffixWord = rawText.substring(textEnd);

        if (textWord.endsWith("'s")) {
            suffixWord = "'s" + suffixWord;
            textWord = textWord.substring(0, textWord.length() - 2);
        }

        // Use the validWord function to check validity
        if (!validWord(textWord)) {
            prefixWord = "";
            textWord = rawText;
            suffixWord = "";
        }

        return new Word(prefixWord, textWord, suffixWord);
    }

    private static boolean validWord(String word) {
        return word.matches("^[a-zA-Z'-]+$") && !word.isEmpty();
    }

    public static boolean loadStopWords(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopWords.add(line.toLowerCase().trim());
            }
            return true;

        } catch (IOException e) {
            return false;
        }
    }

}

