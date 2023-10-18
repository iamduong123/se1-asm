package engine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Engine {
    private List<Doc> docs = new ArrayList<>();

    public int loadDocs(String directoryName) {
        File directory = new File(directoryName);
        int numberOfDocs = 0;

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();

            if (files != null) {
                for (File file : files) {
                    try {
                        System.out.println(file.getPath());
                        String content = new String(Files.readAllBytes(file.toPath()));
                        docs.add(new Doc(content));
                        numberOfDocs++;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(numberOfDocs);
            }
        }
        return numberOfDocs;
    }

    public Doc[] getDocs() {
        return docs.toArray(new Doc[0]);
    }

    public List<Result> search(Query query) {
        List<Result> results = new ArrayList<>();
        for (Doc doc : docs) {
            List<Match> matches = query.matchAgainst(doc);
            if (!matches.isEmpty()) {
                results.add(new Result(doc, matches));
            }
        }
        Collections.sort(results);
        return results;
    }

    public String htmlResult(List<Result> results) {
        StringBuilder formattedResult = new StringBuilder();
        for (Result result : results) {
            formattedResult.append(result.htmlHighlight());
        }
        return formattedResult.toString();
    }
}
