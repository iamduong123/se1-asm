package engine;

import java.util.*;

public class Query {
    private List<Word> keywords = new ArrayList<>();

    public Query(String searchPhrase) {
        searchPhrase = searchPhrase.replaceAll("[^a-zA-Z0-9\\s]", "");
        Arrays.stream(searchPhrase.split("\\s+"))
                .map(Word::createWord)
                .filter(Word::isKeyword)
                .distinct()
                .forEach(keywords::add);
    }

    public List<Word> getKeywords() {
        return keywords;
    }

    public List<Match> matchAgainst(Doc document) {
        List<Match> matches = new ArrayList<>();
        List<Word> allWords = new ArrayList<>();
        allWords.addAll(document.getTitle());
        allWords.addAll(document.getBody());

        for (Word keyword : keywords) {
            int frequency = 0;
            int firstOccurrence = -1;

            for (int i = 0; i < allWords.size(); i++) {
                if (keyword.equals(allWords.get(i))) {
                    frequency++;
                    if (firstOccurrence == -1) {
                        firstOccurrence = i;
                    }
                }
            }

            if (frequency > 0) {
                matches.add(new Match(document, keyword, frequency, firstOccurrence));
            }
        }

        Collections.sort(matches);
        return matches;
    }



    private int countFrequencyInList(List<Word> words, Word keyword) {
        int frequency = 0;
        for (Word word : words) {
            if (keyword.equals(word)) {
                frequency++;
            }
        }
        return frequency;
    }

    private int findInitialPositionInList(List<Word> words, Word keyword) {
        for (int i = 0; i < words.size(); i++) {
            if (keyword.equals(words.get(i))) {
                return i;
            }
        }
        return -1;
    }



    private int countFrequency(Doc document, Word keyword) {
        int frequency = 0;

        for (Word titleWord : document.getTitle()) {
            if (keyword.equals(titleWord)) {
                frequency++;
            }
        }

        for (Word bodyWord : document.getBody()) {
            if (keyword.equals(bodyWord)) {
                frequency++;
            }
        }

        return frequency;
    }

    private int findInitialPosition(Doc document, Word keyword) {
        for (Word titleWord : document.getTitle()) {
            if (keyword.equals(titleWord)) {
                return document.getTitle().indexOf(keyword);
            }
        }

        for (Word bodyWord : document.getBody()) {
            if (keyword.equals(bodyWord)) {
                return document.getTitle().size() + document.getBody().indexOf(keyword);
            }
        }

        return -1;
    }
}
