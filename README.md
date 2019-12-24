# Logic Programming Agent

## Overview
The project aims at developing a logic agent using the first order logic (FOL) inference method. 
The Knowledge Base (KB) is encoded as first order logic clauses. 

The agent is designed to read the input from an input.txt file and outputs the solution to a output.txt file.
The input file contains a list of queries and the clauses present in the knowledge base.
The agent performs RESOLUTION on the first order logic clauses in the KB.

## Prerequisites

Before you begin, ensure you have met the following requirements:

* You have installed the lastest minor version of java 1.8, both JRE and JDK. You can use [this](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) for installation.

## Using the experiment

If you are using an IDE like IntelliJ or Eclipse, use the following steps to run the project
* Point the working directory to Logic-Programming-Agent
* Main class: LogicAgent
* JRE: 1.8
* Run the project

If you are running the project on command line, follow these steps:<br/>
* Clone/Download the project to your local machine.
* Navigate to the folder with LogicAgent.java file.
* Run the following commands:
```
javac LogicAgent.java
```

```
java LogicAgent
```


## Code Structure / File description

`Format for input.txt`

<N = NUMBER OF QUERIES>
<QUERY 1>
…
<QUERY N>
<K = NUMBER OF GIVEN SENTENCES IN THE KNOWLEDGE BASE>
<SENTENCE 1>
…
<SENTENCE K>
  
The first line contains an integer N specifying the number of queries. The following N lines contain
one query per line. The line after the last query contains an integer K specifying the number of
sentences in the knowledge base. The remaining K lines contain the sentences in the knowledge
base, one sentence per line.
`Query format`

Each query will be a single literal of the form Predicate(Constant_Arguments) or
~Predicate(Constant_Arguments) and will not contain any variables. Each predicate will have
between 1 and 25 constant arguments. Two or more arguments will be separated by commas.

`KB format` 

Each sentence in the knowledge base is written in one of the following forms:

1) An implication of the form p1 ∧ p2 ∧ ... ∧ pm ⇒ q, where its premise is a conjunction of
literals and its conclusion is a single literal. Remember that a literal is an atomic sentence
or a negated atomic sentence.

2) A single literal: q or ~q

`Note`

1. & denotes the conjunction operator.<br/>
2. | denotes the disjunction operator. It will not appear in the queries nor in the KB given as
input. But you will likely need it to create your proofs. <br/>
3. => denotes the implication operator.<br/>
4. ~ denotes the negation operator.<br/>
5. No other operators besides &, =>, and ~ are used in the knowledge base.<br/>
6. There will be no parentheses in the KB except as used to denote arguments of predicates.<br/>
7. Variables are denoted by a single lowercase letter.<br/>
8. All predicates (such as HighBP) and constants (such as Alice) are case sensitive
alphabetical strings that begin with uppercase letters.<br/>
9. Each predicate takes at least one argument. Predicates will take at most 25 arguments. A
given predicate name will not appear with different number of arguments.<br/>
10. There will be at most 10 queries and 100 sentences in the knowledge base.<br/>
11. See the sample input below for spacing patterns.<br/>
12. You can assume that the input format is exactly as it is described.<br/>
13. There will be no syntax errors in the given input.<br/>
14. The KB will be true (i.e., will not contain contradictions).<br/>

`Format for output.txt:`

For each query, determine if that query can be inferred from the knowledge base or not, one
query per line:

<ANSWER 1>
…
<ANSWER N>

Each answer should be either TRUE if you can prove that the corresponding query sentence is
true given the knowledge base, or FALSE if you cannot.
  

`/agent/KnowledgeBasedAgent.java`

* This is the core of the project where the actual inference is performed. 

1. For a given KB in the input, we convert the KB to CNF form in order to perform resolution.
2. After that, for each query in the input, we perform resolution and check if the query is TRUE or FALSE.

`/exceptions/FileException.java`

* Generic exception class for File related exceptions in this project.

`/model/AtomicSentence.java`

* An atomic sentence consists of the predicate and a boolean which indicates if the predicate is negated or not.

`/model/ComplexSentence.java`

* A complex sentence is list of atomic sentences.

`/model/Predicate.java`

* A predicate in FOL is a function that consists of a name and a list of arguments called terms.
Eg: Father(Bob, Mary)

`/model/Term.java`

* A term is an argument for a function in FOL (In this project, we only have predicates and no functions) which can either be a Constant or Variable.

`/model/KnowledgeBase.java`

* The KB is a list of sentences both atomic and complex.

`/model/Variable.java`

* Variables are denoted by a single lowercase letter.

`/model/Constant.java`

* The first letter in a Constant is an uppercase letter.


## Contact

If you want to contact me you can reach me at <km69564@usc.edu> or <krishnamanoj14@gmail.com>.
