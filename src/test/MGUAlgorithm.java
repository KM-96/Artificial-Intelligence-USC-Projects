package test;

import model.Predicate;
import model.Term;

import java.util.HashMap;
import java.util.Map;

public class MGUAlgorithm {
    private Map<String, String> substitution = new HashMap<>();

    public static void main(String[] args) {

    }

    public boolean unify(Predicate predicate1, Predicate predicate2) {

        if (!predicate1.getName().equals(predicate2.getName()) || predicate1.getTerms().size() != predicate2.getTerms().size()) {
            return false;
        }
        for (int i = 0; i < predicate1.getTerms().size(); i++) {
            Term term1 = predicate1.getTerms().get(i);
            Term term2 = predicate2.getTerms().get(i);
        }
        return false;
    }
}
