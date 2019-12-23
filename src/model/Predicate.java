package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Predicate implements Cloneable {
    private String name;

    private List<Term> terms;

    public Predicate() {
        this.terms = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Predicate)) return false;

        Predicate predicate = (Predicate) o;

        if (!getName().equals(predicate.getName())) return false;
        return getTerms().equals(predicate.getTerms());
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (terms != null ? terms.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Predicate{" +
                "name='" + name + '\'' +
                ", terms=" + terms +
                '}';
    }

    @Override
    protected Object clone() {
        Predicate predicate = null;
        try {
            predicate = (Predicate) super.clone();
            List<Term> terms = new ArrayList<>();
            for (Term term : this.terms) {
                terms.add((Term) term.clone());
            }
            predicate.setTerms(terms);
        } catch (CloneNotSupportedException e) {
            predicate = new Predicate();
        }
        return predicate;
    }
}
