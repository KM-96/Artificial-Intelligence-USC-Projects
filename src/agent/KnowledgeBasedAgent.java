package agent;

import model.*;

import java.util.*;

public class KnowledgeBasedAgent {
    //The static counter is used to maintain the count of number of resolutions for a given query
    private static int counter = 0;

    public List<String> resolution(Input input) {
        //Step 1 - Construct the KB in the CNF form with the given facts
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.buildKB(input);

        //Step 2 - For each query in the input, perform resolution and check if the query is TRUE or FALSE
        List<String> output = new ArrayList<>();
        for (int i = 0; i < input.getQueryCount(); i++) {
            counter = 0;
            output.add(resolution(knowledgeBase, input.getQueries().get(i)));
        }
        return output;
    }

    /**
     * Query format: Each query will be a single literal of the form Predicate(Constant_Arguments) or
     * ~Predicate(Constant_Arguments) and will not contain any variables. Each predicate will have
     * between 1 and 25 constant arguments. Two or more arguments will be separated by commas.
     *
     * @param knowledgeBase
     * @param query
     * @return
     */
    private String resolution(KnowledgeBase knowledgeBase, String query) {
        ResolutionState state = null;
        AtomicSentence querySentence = null;
        try {
            System.out.println("\n===================================================");
            System.out.println("Input Query: " + query);

            //Step 1: Negate the given query
            String negatedQuery = negate(query);

            //Step 2: Convert the negated query to an atomic / complex sentence
            querySentence = knowledgeBase.createAtomicSentence(negatedQuery);
            System.out.println("Negated Query: " + querySentence.buildAtomicSentence());

            //Step 3: Add the negated query sentence to the KB
            knowledgeBase.addAtomicSentenceToKB(querySentence);
            System.out.println("Before Resolution - The sentences in the KB are: " + knowledgeBase.getSentences().size());
            knowledgeBase.printSentencesInKB();

            //Step 4: Create a complex sentence that will keep the state of resolution tree
            ComplexSentence complexQuerySentence = new ComplexSentence();
            complexQuerySentence.getAtomicSentences().add(querySentence);

            //Step 5: Build resolution
            state = resolve(knowledgeBase, complexQuerySentence, new HashSet<>());
            System.out.println("Final result: " + state.getStatus());
            System.out.println("=================================================================");
        } catch (StackOverflowError e) {
            //Sometimes a StackOverflowError occurs when there is no resolution for a given problem and
            //the program tries to resolve it endlessly eventually throwing a StackOverflowError.
            //In situations like these we are setting the output to a query as 'FALSE'.
            System.out.println("Inside stackoverflow error!!");
            state = new ResolutionState();
            state.setStatus(LogicConstants.RESOLUTION_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Catching Generic exception");
            state = new ResolutionState();
            state.setStatus(LogicConstants.RESOLUTION_FAIL);
        } finally {
            //Step 6: Remove the negated query sentence from the KB
            knowledgeBase.getSentences().remove(querySentence);
            System.out.println("After Resolution - The sentences in the KB are: " + knowledgeBase.getSentences().size());
            //knowledgeBase.printSentencesInKB();

        }
        return state.getStatus();
    }

    /**
     * This is the actual recursive method that does the resolution of a given Query.
     * It takes as input the KB and a negated query (which is a complex sentence)
     * If all the literals in the sentence can be resolved then we get a contradiction and we proved the original query.
     *
     * @param knowledgeBase
     * @param complexQuerySentence
     * @param atomicSentencesWithoutResolution
     * @return
     */
    private ResolutionState resolve(KnowledgeBase knowledgeBase, ComplexSentence complexQuerySentence, Set<AtomicSentence> atomicSentencesWithoutResolution) {
        counter++;
        System.out.println("\n\nAt the beginning of resolution: " + complexQuerySentence.buildComplexSentence());
        //The result of each resolution is stored in the ResolutionState object
        ResolutionState result = new ResolutionState();
        result.setComplexSentence((ComplexSentence) complexQuerySentence.clone());
        Collections.shuffle(result.getComplexSentence().getAtomicSentences());

        //Base case: if there is no sentence to resolve then return the result as 'TRUE'
        if (result.getComplexSentence().getAtomicSentences().isEmpty()) {
            result.setStatus(LogicConstants.RESOLUTION_PASSED);
            return result;
        }

        //Base case: if threshold number of resolutions is reached then return 'FALSE'
        if (counter >= LogicConstants.MAXIMUM_RESOLUTIONS) {
            System.out.println("Returing FALSE as maximum resolutions have reached!!");
            result.setStatus(LogicConstants.RESOLUTION_FAIL);
            return result;
        }

        //The iterator runs over each atomic sentence in the given query.
        Iterator<AtomicSentence> iterator = complexQuerySentence.getAtomicSentences().iterator();
        while (iterator.hasNext()) {
            //Take a particular atomic sentence say 'A'.
            AtomicSentence atomicQuerySentence = iterator.next();

            //If it already checked before and is figured out that there is no resolution for 'A', then return result.
            if (atomicSentencesWithoutResolution.contains(atomicQuerySentence)) {
                return result;
            }

            //Iterate through all the sentences in the KB and check if 'A' can be resolved.
            for (Sentence sentence : knowledgeBase.getSentences()) {
                //ToDo: Remove the below lines
                if (sentence instanceof AtomicSentence) {
                    System.out.println("Comparing " + atomicQuerySentence.buildAtomicSentence() + " with atomic sentence: " + ((AtomicSentence) sentence).buildAtomicSentence());
                } else {
                    System.out.println("Comparing " + atomicQuerySentence.buildAtomicSentence() + "with Complex sentence: " + ((ComplexSentence) sentence).buildComplexSentence());
                }
                //ToDo: End
                //A sentence in the KB can either be an atomic sentence or a complex sentence.
                if (sentence instanceof AtomicSentence) {
                    //Unification step
                    Map<String, String> substitution = unify(((AtomicSentence) ((AtomicSentence) sentence).clone()).getPredicate(), (atomicQuerySentence).getPredicate());
                    if (checkIfOneAtomicSentenceIsNegatedAndTheOtherIsNot(atomicQuerySentence, (AtomicSentence) sentence)
                            && !substitution.isEmpty()) {
                        atomicQuerySentence.setCanResolve(true);
                        //Store a copy of the complex sentence if we want to undo the substitution
                        ComplexSentence copyOfResultComplexSentence = (ComplexSentence) result.getComplexSentence().clone();
                        System.out.println("Match found with the atomic sentence: " + ((AtomicSentence) sentence).buildAtomicSentence());
                        result.setStatus(LogicConstants.RESOLVED_STATUS);
                        result.getComplexSentence().getAtomicSentences().remove(atomicQuerySentence);
                        //Apply the result of the unification to the complex sentence.
                        applyUnification(result.getComplexSentence(), substitution);
                        result = resolve(knowledgeBase, result.getComplexSentence(), atomicSentencesWithoutResolution);
                        System.out.println("result after resoution --> " + result.getComplexSentence().buildComplexSentence());
                        if (LogicConstants.RESOLUTION_FAIL.equals(result.getStatus())) {
                            result.setComplexSentence(copyOfResultComplexSentence);
                            System.out.println("After resetting back to the original complex sentence: " + result.getComplexSentence().buildComplexSentence());
                        }
                    }
                } else {
                    //sentence is an instanceof ComplexSentence
                    //Iterate through each atomic sentence in a complex sentence and check if 'A' can be resolved.
                    for (AtomicSentence atomicInComplexSentence : ((ComplexSentence) sentence).getAtomicSentences()) {
                        //Unification step
                        Map<String, String> substitution = unify(((AtomicSentence) atomicInComplexSentence.clone()).getPredicate(), (atomicQuerySentence).getPredicate());
                        if (checkIfOneAtomicSentenceIsNegatedAndTheOtherIsNot(atomicQuerySentence, atomicInComplexSentence)
                                && !substitution.isEmpty()) {
                            atomicQuerySentence.setCanResolve(true);
                            //Store a copy of the complex sentence if we want to undo the substitution
                            ComplexSentence copyOfResultComplexSentence = (ComplexSentence) result.getComplexSentence().clone();
                            System.out.println("Match found with complex sentence!" + ((ComplexSentence) sentence).buildComplexSentence());
                            System.out.println("Unifying with the atomic sentence: " + atomicInComplexSentence.buildAtomicSentence());
                            result.setStatus(LogicConstants.RESOLVED_STATUS);
                            result.getComplexSentence().getAtomicSentences().remove(atomicQuerySentence);
                            //Apply unification for each of the atomic sentence that is part of the complex sentence
                            applyUnification(result.getComplexSentence(), substitution);
                            applyUnification(atomicQuerySentence, substitution);
                            for (AtomicSentence atomicSentence : ((ComplexSentence) sentence).getAtomicSentences()) {
                                AtomicSentence temp = (AtomicSentence) atomicSentence.clone();
                                if (temp.equals(atomicInComplexSentence)) {
                                    continue;
                                }
                                applyUnification(temp, substitution);
                                result.getComplexSentence().getAtomicSentences().add(temp);
                            }
                            result = resolve(knowledgeBase, result.getComplexSentence(), atomicSentencesWithoutResolution);
                            System.out.println("result after resoution --> " + result.getComplexSentence().buildComplexSentence());
                            if (LogicConstants.RESOLUTION_FAIL.equals(result.getStatus())) {
                                result.setComplexSentence(copyOfResultComplexSentence);
                                System.out.println("After resetting back to the original complex sentence: " + result.getComplexSentence().buildComplexSentence());
                            }
                        }
                    }
                }
                if (LogicConstants.RESOLUTION_PASSED.equals(result.getStatus())) {
                    break;
                }
            }
            if (LogicConstants.RESOLUTION_PASSED.equals(result.getStatus())) {
                result.setStatus(LogicConstants.RESOLUTION_PASSED);
                break;
            } else if (!atomicQuerySentence.canResolve()) {
                result.setStatus(LogicConstants.RESOLUTION_FAIL);
                System.out.println("Adding " + atomicQuerySentence.buildAtomicSentence() + " to the set of sentences that cannot be resolved");
                atomicSentencesWithoutResolution.add(atomicQuerySentence);
                return result;
            }
        }
        System.out.println("Result after resolution: " + result.getComplexSentence().buildComplexSentence());
        System.out.println("----------------------------------------------------------------------------------");
        return result;
    }

    private boolean checkIfOneAtomicSentenceIsNegatedAndTheOtherIsNot(AtomicSentence atomicSentence1, AtomicSentence atomicSentence2) {
        return (atomicSentence1.isNegated() && !atomicSentence2.isNegated()) || (!atomicSentence1.isNegated() && atomicSentence2.isNegated());
    }

    private Map<String, String> unify(Predicate predicate1, Predicate predicate2) {
        Map<String, String> substitution = new HashMap<>();
        if (!predicate1.getName().equals(predicate2.getName()) || predicate1.getTerms().size() != predicate2.getTerms().size()) {
            return substitution;
        }
        for (int i = 0; i < predicate1.getTerms().size(); i++) {
            Term term1 = predicate1.getTerms().get(i);
            Term term2 = predicate2.getTerms().get(i);
            //If both the terms are constants and term1 is not equal to term2 then return false
            if (term1 instanceof Constant && term2 instanceof Constant && !term1.getName().equals(term2.getName())) {
                //returning empty substitution if the Constant terms don't match.
                return new HashMap<>();
            } else if (term1 instanceof Constant && term2 instanceof Variable) {
                //If term1 is a constant and term2 is a Variable and the variable already has a substitution then,
                //checking if the substituted value is equal to term1
                //Return false if it is not equal
                if (substitution.containsKey(term2.getName())) {
                    String value = substitution.get(term2.getName());
                    if (!value.equals(term1.getName()) && Character.isUpperCase(value.charAt(0))) {
                        return new HashMap<>();
                    } else {
                        substitution.put(value, term1.getName());
                        substitution.put(term2.getName(), term1.getName());
                    }
                } else {
                    substitution.put(term2.getName(), term1.getName());
                }
            } else if (term1 instanceof Variable && term2 instanceof Constant) {
                //Similar explanation as above but term1 and term2 are interchanged
                if (substitution.containsKey(term1.getName())) {
                    String value = substitution.get(term1.getName());
                    if (!value.equals(term2.getName()) && Character.isUpperCase(value.charAt(0))) {
                        return new HashMap<>();
                    } else {
                        substitution.put(value, term2.getName());
                        substitution.put(term1.getName(), term2.getName());
                    }
                } else {
                    substitution.put(term1.getName(), term2.getName());
                }
            } else if (term1 instanceof Variable && term2 instanceof Variable) {
                //If both the terms are variables, then we are in a tricky situation!! HeHe :P
                //If term2 has a constant substitution already, add that constant substitution to term1
                boolean flag = false;
                if (substitution.containsKey(term2.getName())) {
                    String value = substitution.get(term2.getName());
                    if (Character.isUpperCase(value.charAt(0))) {
                        substitution.put(term1.getName(), value);
                        flag = true;
                    } else if (!substitution.containsKey(term1.getName())) {
                        substitution.put(term1.getName(), term2.getName());
                        flag = true;
                    }
                }
                if (substitution.containsKey(term1.getName())) {
                    //If term1 has a constant substitution already, add that constant substitution to term2
                    String value = substitution.get(term1.getName());
                    if (Character.isUpperCase(value.charAt(0))) {
                        substitution.put(term2.getName(), value);
                        flag = true;
                    } else if (!substitution.containsKey(term2.getName())) {
                        substitution.put(term2.getName(), term1.getName());
                        flag = true;
                    }
                }
                if (!flag) {
                    //If none of them are already available in the KB, then
                    substitution.put(term1.getName(), term2.getName());
                    //substitution.put(term2.getName(), term1.getName());
                }
            } else {
                substitution.put(term1.getName(), term2.getName());
            }
        }
        replaceAllVariablesWithConstants(substitution);
        return substitution;
    }

    private void replaceAllVariablesWithConstants(Map<String, String> substitution) {
        int counter = 0;
        for (Map.Entry entry : substitution.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (Character.isLowerCase(value.charAt(0)) && !key.equals(value)) {
                substitution.put(key, recursivelySearchForConstantValue(substitution, value, counter));
            }
        }
    }

    private String recursivelySearchForConstantValue(Map<String, String> substitution, String value, int counter) {
        counter++;
        if (counter >= LogicConstants.MAXIMUM_SUBSTITUTIONS_FOR_VARIABLES) {
            return value;
        }
        if (!substitution.containsKey(value)) {
            return value;
        }
        if (Character.isUpperCase(value.charAt(0))) {
            return value;
        }
        return recursivelySearchForConstantValue(substitution, substitution.get(value), counter);
    }

    /**
     * For a given atomic sentence, to all the variables in the predicate, this method applies the values
     * of the variables in the substitution.
     *
     * @param complexSentence
     * @param substitution
     */
    private void applyUnification(ComplexSentence complexSentence, Map<String, String> substitution) {
        //Step 1: For each atomic sentence that is part of the complex sentence, apply substitution.
        for (AtomicSentence atomicSentence : complexSentence.getAtomicSentences()) {
            applyUnification(atomicSentence, substitution);
        }
    }


    /**
     * For a given atomic sentence, to all the variables in the predicate, this method applies the values
     * of the variables in the substitution.
     *
     * @param atomicSentence
     * @param substitution
     */
    private void applyUnification(AtomicSentence atomicSentence, Map<String, String> substitution) {
        Predicate predicate = atomicSentence.getPredicate();
        for (int i = 0; i < predicate.getTerms().size(); i++) {
            if (predicate.getTerms().get(i) instanceof Variable) {
                if (substitution.containsKey(predicate.getTerms().get(i).getName())) {
                    System.out.println("Substituting '" + substitution.get(((predicate.getTerms().get(i)).getName())) + "' for " + ((Variable) predicate.getTerms().get(i)).getName());
                    String value = substitution.get(((predicate.getTerms().get(i)).getName()));
                    //If the first character is a lower case letter than the Term is a Variable, else Constant
                    if (Character.isLowerCase(value.charAt(0))) {
                        Variable variable = new Variable(value);
                        predicate.getTerms().set(i, variable);
                    } else {
                        Constant constant = new Constant(value);
                        predicate.getTerms().set(i, constant);
                    }
                }
            }
        }
    }

    /**
     * First step of resolution: This method negates the given query
     *
     * @param query
     * @return
     */
    private String negate(String query) {
        query = query.trim();
        if (query.contains(Operators.NEGATION)) {
            return query.substring(1, query.length());
        } else {
            return Operators.NEGATION + query;
        }
    }
}