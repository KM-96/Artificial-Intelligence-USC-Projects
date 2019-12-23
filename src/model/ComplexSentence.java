package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComplexSentence extends Sentence implements Cloneable {
    private List<AtomicSentence> atomicSentences;

    public List<AtomicSentence> getAtomicSentences() {
        return atomicSentences;
    }

    public void setAtomicSentences(List<AtomicSentence> atomicSentences) {
        this.atomicSentences = atomicSentences;
    }

    public ComplexSentence() {
        atomicSentences = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "ComplexSentence{" +
                "atomicSentences=" + atomicSentences +
                '}';
    }

    @Override
    public Object clone() {
        ComplexSentence complexSentence;
        try {
            complexSentence = (ComplexSentence) super.clone();
            List<AtomicSentence> atomicSentences = new ArrayList<>();
            for (AtomicSentence atomicSentence : this.atomicSentences) {
                atomicSentences.add((AtomicSentence) atomicSentence.clone());
            }
            complexSentence.setAtomicSentences(atomicSentences);
        } catch (CloneNotSupportedException e) {
            complexSentence = new ComplexSentence();
        }
        return complexSentence;
    }

    public String buildComplexSentence() {
        StringBuilder stringBuilder = new StringBuilder();
        if (atomicSentences.isEmpty()) {
            return "";
        }
        for (AtomicSentence atomicSentence : atomicSentences) {
            if (atomicSentence.isNegated()) {
                stringBuilder.append(Operators.NEGATION);
            }
            stringBuilder.append(atomicSentence.getPredicate().getName() + "(");
            for (Term term : atomicSentence.getPredicate().getTerms()) {
                stringBuilder.append(term + ",");
            }
            stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);
            stringBuilder.append(")");
            stringBuilder.append(Operators.DISJUNCTION);
        }
        return stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1).toString();
    }
}