package engine;

import java.util.List;

public class Result implements Comparable<Result> {

    private final Doc document;
    private final List<Match> matches;

    public Result(Doc document, List<Match> matches) {
        this.document = document;
        this.matches = matches;
    }

    public Doc getDoc() {
        return document;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public int getTotalFrequency() {
        return matches.stream().mapToInt(Match::getFreq).sum();
    }

    public double getAverageIndex() {
        return matches.stream().mapToInt(Match::getFirstIndex).average().orElse(0.0);
    }

    private String buildHighlightedSection(List<Word> wordList, String startTag, String endTag) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);
            boolean isHighlighted = matches.stream().anyMatch(match -> word.equals(match.getWord()));

            if (isHighlighted) {
                result.append(word.getPrefix()).append(startTag).append(word.getText()).append(endTag).append(word.getSuffix());
            } else {
                result.append(word);
            }

            if (i < wordList.size() - 1) {
                result.append(" ");
            }
        }

        return result.toString();
    }

    public String htmlHighlight() {
        return "<h3>" + buildHighlightedSection(document.getTitle(), "<u>", "</u>") +
                "</h3><p>" + buildHighlightedSection(document.getBody(), "<b>", "</b>") +
                "</p>";
    }

    @Override
    public int compareTo(Result other) {
        int matchCountComparison = Integer.compare(matches.size(), other.matches.size());
        if (matchCountComparison != 0) {
            return -matchCountComparison;
        }

        int totalFrequencyComparison = Integer.compare(getTotalFrequency(), other.getTotalFrequency());
        if (totalFrequencyComparison != 0) {
            return -totalFrequencyComparison;
        }

        return Double.compare(getAverageIndex(), other.getAverageIndex());
    }
}
