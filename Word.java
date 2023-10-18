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

    private final String prefix;
    private final String text;
    private final String suffix;

    private Word(String prefix, String text, String suffix) {
        this.prefix = prefix;
        this.text = text;
        this.suffix = suffix;
    }

    public boolean isKeyword() {
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
        int startIdx = 0, endIdx = 0;

        while (startIdx < rawText.length() && !Character.isLetter(rawText.charAt(startIdx))) {
            startIdx++;
        }

        endIdx = startIdx;
        while (endIdx < rawText.length() && (Character.isLetter(rawText.charAt(endIdx)) ||
                rawText.charAt(endIdx) == '-' || rawText.charAt(endIdx) == '\'')) {
            endIdx++;
        }

        String prefix = rawText.substring(0, startIdx);
        String text = rawText.substring(startIdx, endIdx);
        String suffix = rawText.substring(endIdx);

        return new Word(prefix, text, suffix);
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

