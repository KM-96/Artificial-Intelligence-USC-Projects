package model;

import java.util.ArrayList;
import java.util.List;

public class Input {
    //Number of queries
    private int queryCount;

    //Number of sentences in the KB
    private int n;

    //All the query strings
    private List<String> queries;

    //All the sentences in the KB
    private List<String> sentences;

    public Input() {
        queries = new ArrayList<>();
        sentences = new ArrayList<>();
    }

    public int getQueryCount() {
        return queryCount;
    }

    public void setQueryCount(int queryCount) {
        this.queryCount = queryCount;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public List<String> getQueries() {
        return queries;
    }

    public List<String> getSentences() {
        return sentences;
    }

    @Override
    public String toString() {
        return "Input{" +
                "queryCount=" + queryCount +
                ", n=" + n +
                ", queries=" + queries +
                ", sentences=" + sentences +
                '}';
    }
}
