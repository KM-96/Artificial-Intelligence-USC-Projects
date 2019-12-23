package model;

import java.util.*;

public class KnowledgeBase {
    private List<Sentence> sentences;

    public KnowledgeBase() {
        this.sentences = new LinkedList<>();
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public void addAtomicSentenceToKB(AtomicSentence atomicSentence) {
        this.getSentences().add(0, atomicSentence);
    }

    /**
     * The input sentences can only be in one of the two formats
     * 1) An implication of the form p1 ∧ p2 ∧ ... ∧ pm ⇒ q, where its premise is a conjunction of
     * literals and its conclusion is a single literal. Remember that a literal is an atomic sentence
     * or a negated atomic sentence.
     * 2) A single literal: q or ~q
     */
    public void buildKB(Input input) {
        //Iterate through all the sentences in the input and add each sentence to the KB in the CNF form
        for (int i = 0; i < input.getN(); i++) {
            //Check if the sentence contains an implication
            if (containsImplication(input.getSentences().get(i))) {
                //If the sentence has an implication, then we need to convert it into CNF form
                ComplexSentence complexSentence = convertStringToCnfForm(input.getSentences().get(i));
                standardizeVariables(complexSentence, i);
                this.getSentences().add(complexSentence);
            } else {
                //Otherwise create an atomic sentence and add to the KB.
                AtomicSentence atomicSentence = createAtomicSentence(input.getSentences().get(i));
                standardizeVariables(atomicSentence, i);
                this.getSentences().add(0, atomicSentence);
            }
        }

        //Using sorting algorithm to sort all the sentences in the KB based on the number of atomic sentences in a given complex sentence
        //Using generic Bubble sort algorithm as the size of the KB is maximum 100
        //Sorting Begin
        int n = this.sentences.size();
        int k = 0;
        for (int i = 0; i < n - 1; i++) {
            if (this.getSentences().get(i) instanceof AtomicSentence) {
                k++;
                continue;
            }
            boolean isSwapOperationPerformed = false;
            for (int j = k; j < n + k - i - 1; j++) {
                if (((ComplexSentence) (this.getSentences().get(j))).getAtomicSentences().size() > ((ComplexSentence) (this.getSentences().get(j + 1))).getAtomicSentences().size()) {
                    ComplexSentence sentence = (ComplexSentence) ((ComplexSentence) (this.getSentences().get(j))).clone();
                    this.getSentences().set(j, (this.getSentences().get(j + 1)));
                    this.getSentences().set(j + 1, sentence);
                    isSwapOperationPerformed = true;
                }
            }
            if (!isSwapOperationPerformed) {
                break;
            }
        }
        //Sorting End
    }

    /**
     * For each sentence in the KB, we standardize the arguments if the term is a variable.
     * This method appends the KB sentence number and the index of the term in the argument to the variable name.
     *
     * @param sentence
     * @param value
     */
    private void standardizeVariables(Sentence sentence, int value) {
        if (sentence instanceof AtomicSentence) {
            List<Term> terms = ((AtomicSentence) sentence).getPredicate().getTerms();
            for (int i = 0; i < terms.size(); i++) {
                if (terms.get(i) instanceof Variable) {
                    terms.set(i, new Variable(terms.get(i).getName() + value + i));
                }
            }
        } else {
            Map<String, String> map = new HashMap<>();
            for (AtomicSentence atomicSentence : ((ComplexSentence) sentence).getAtomicSentences()) {
                List<Term> terms = atomicSentence.getPredicate().getTerms();
                for (int i = 0; i < terms.size(); i++) {
                    if (terms.get(i) instanceof Variable) {
                        if (map.containsKey(terms.get(i).getName())) {
                            terms.set(i, new Variable(map.get(terms.get(i).getName())));
                        } else {
                            StringBuilder variableName = new StringBuilder();
                            variableName.append(terms.get(i).getName());
                            variableName.append(value);
                            variableName.append(i);
                            map.put(terms.get(i).getName(), variableName.toString());
                            terms.set(i, new Variable(variableName.toString()));
                        }
                    }
                }
            }
        }
    }

    /**
     * Given an input string that has an implication, the method will convert the string to a CNF form
     *
     * @param input
     * @return
     */
    private ComplexSentence convertStringToCnfForm(String input) {
        String afterSplit[] = input.split(Operators.IMPLICATION);
        String premise = afterSplit[0];
        String conclusion = afterSplit[1];

        ComplexSentence complexSentence = new ComplexSentence();
        List<AtomicSentence> atomicSentences = new ArrayList<>();
        //An example input: Migraine(x) & HighBP(x) => Take(x,Timolol)
        //Split the premise
        String premiseAfterSplit[] = premise.split(Operators.CONJUNCTION);
        for (int i = 0; i < premiseAfterSplit.length; i++) {
            AtomicSentence atomicSentence = createAtomicSentence(premiseAfterSplit[i]);
            if (atomicSentence.isNegated()) {
                atomicSentence.setNegated(false);
            } else {
                atomicSentence.setNegated(true);
            }
            atomicSentences.add(atomicSentence);
        }
        //Add the conclusion to the complex sentence
        atomicSentences.add(createAtomicSentence(conclusion));
        complexSentence.setAtomicSentences(atomicSentences);
        return complexSentence;
    }

    /**
     * Check if a given input string contains an implication operator or not!
     *
     * @param input
     * @return
     */
    private boolean containsImplication(String input) {
        return input.contains(Operators.IMPLICATION);
    }

    /**
     * Check if an input string contains the negation operation in it
     *
     * @param input
     * @return
     */
    private boolean containsNegation(String input) {
        return input.contains(Operators.NEGATION);
    }

    public AtomicSentence createAtomicSentence(String input) {
        //Trim the input sentence
        input = input.trim();

        //Create a new atomic sentence
        AtomicSentence atomicSentence = new AtomicSentence();

        //Check if the predicate is negated
        atomicSentence.setNegated(containsNegation(input));

        //An atomic sentence has a predicate
        Predicate predicate = new Predicate();
        if (atomicSentence.isNegated()) {
            predicate.setName(input.substring(1, input.indexOf("(")).trim());
        } else {
            predicate.setName(input.substring(0, input.indexOf("(")).trim());
        }

        //Retrieve the arguments string from the input.
        //For TAKE(Manoj, x) Predicate name = TAKE, argument string = Manoj, x
        String argumentsString = input.substring(input.indexOf("(") + 1, input.indexOf(")"));

        //Get individual arguments from the argument list
        String arguments[] = argumentsString.trim().replaceAll(" ", "").split(",");

        //Create a list of all the terms ( constants and variables that belong to the predicate)
        List<Term> terms = new ArrayList<>();

        //Keeping track of all the Predicates that only take constants
        for (int i = 0; i < arguments.length; i++) {
            Term term;
            //If a term starts with an uppercase letter then it is a Constant else it is a Variable
            if (arguments[i].charAt(0) >= 65 && arguments[i].charAt(0) <= 90) {
                term = new Constant(arguments[i].trim());
            } else {
                term = new Variable(arguments[i].trim());
            }
            terms.add(term);
        }
        predicate.setTerms(terms);
        atomicSentence.setPredicate(predicate);
        return atomicSentence;
    }

    /**
     * Method to print all the facts/sentences in the KB.
     */
    public void printSentencesInKB() {
        System.out.println();
        System.out.println("Printing Sentences in KB");
        System.out.println("=========================");
        for (Sentence sentence : this.sentences) {
            if (sentence instanceof AtomicSentence) {
                System.out.println(((AtomicSentence) sentence).buildAtomicSentence());
            } else {
                System.out.println(((ComplexSentence) sentence).buildComplexSentence());
            }
        }
        System.out.println("End of Sentences in KB");
        System.out.println("=======================");
    }
}