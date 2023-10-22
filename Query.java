package engine;

import java.util.*;

public class Query {
    private List<Word> keywords = new ArrayList<>();

    public Query(String searchPhrase) {
        Arrays.stream(searchPhrase.split("\\s+"))
                .map(Word::createWord)
                .filter(word -> !Word.isStopWord(word.getText()))  // filter out stop words
                .filter(Word::isKeyword)
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
                if (keyword.getText().equalsIgnoreCase(allWords.get(i).getText())) {
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

        // Sort matches by firstOccurrence
        matches.sort(Comparator.comparingInt(Match::getFirstIndex));

        return matches;
    }

}
