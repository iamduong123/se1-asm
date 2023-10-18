package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Doc extends File {
    private List<Word> title = new ArrayList<>();
    private List<Word> body = new ArrayList<>();

    public Doc(String content) throws FileNotFoundException {
        super(content);

        String[] rawContent = content.split("\n");
        String rawTitle = rawContent.length > 0 ? rawContent[0].trim() : "";
        String rawBody = rawContent.length > 1 ? rawContent[1].trim() : "";

        populateWordList(title, rawTitle);
        populateWordList(body, rawBody);
    }

    private void populateWordList(List<Word> wordList, String content) {
        for (String s : content.split("\\s+")) {
            wordList.add(Word.createWord(s));
        }
    }

    public List<Word> getTitle() {
        return this.title;
    }

    public List<Word> getBody() {
        return this.body;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Doc otherDoc = (Doc) obj;
        return title.equals(otherDoc.title) && body.equals(otherDoc.body);
    }
}
