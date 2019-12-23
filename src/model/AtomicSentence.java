package model;

public class AtomicSentence extends Sentence implements Cloneable {
    private boolean canResolve = false;

    private boolean isNegated;

    private Predicate predicate;

    public Predicate getPredicate() {
        return predicate;
    }

    public void setPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public boolean isNegated() {
        return isNegated;
    }

    public void setNegated(boolean negated) {
        isNegated = negated;
    }

    public boolean canResolve() {
        return canResolve;
    }

    public void setCanResolve(boolean canResolve) {
        this.canResolve = canResolve;
    }

    @Override
    public String toString() {
        return "AtomicSentence{" +
                "isNegated=" + isNegated +
                ", predicate=" + predicate +
                '}';
    }


    public String buildAtomicSentence() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.isNegated()) {
            stringBuilder.append(Operators.NEGATION);
        }
        stringBuilder.append(this.getPredicate().getName() + "(");
        for (Term term : this.getPredicate().getTerms()) {
            stringBuilder.append(term + ",");
        }
        //Delete the extra , that is added after the end of the last term.
        stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    @Override
    public Object clone() {
        AtomicSentence atomicSentence = null;
        try {
            atomicSentence = (AtomicSentence) super.clone();
            atomicSentence.setCanResolve(this.canResolve);
            atomicSentence.setPredicate((Predicate) predicate.clone());
        } catch (CloneNotSupportedException e) {
            atomicSentence = new AtomicSentence();
        }

        return atomicSentence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AtomicSentence)) return false;

        AtomicSentence that = (AtomicSentence) o;

        if (isNegated() != that.isNegated()) return false;
        return getPredicate().equals(that.getPredicate());
    }

    @Override
    public int hashCode() {
        int result = (isNegated() ? 1 : 0);
        result = 31 * result + getPredicate().hashCode();
        return result;
    }
}