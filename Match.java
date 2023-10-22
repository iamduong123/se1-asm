package a1_2101040066;

public class Match implements Comparable<Match> {
    private final Doc document;
    private final Word word;
    private final int frequency;
    private final int firstIndex;

    public Match(Doc document, Word word, int frequency, int firstIndex) {
        this.document = document;
        this.word = word;
        this.frequency = frequency;
        this.firstIndex = firstIndex;
    }

    public Doc getDoc() {
        return document;
    }

    public Word getWord() {
        return word;
    }

    public int getFreq() {
        return frequency;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    @Override
    public int compareTo(Match other) {
        return Integer.compare(this.firstIndex, other.firstIndex);
    }
}
