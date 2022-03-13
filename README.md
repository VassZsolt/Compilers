# Compilers
DE-University-Fordítóprogramok

The first program was a __Lexical Analyser__ what can analyze the input text and get the identifiers, contants, operators and comments.  

constant: from a digit to a non-digit character  
identifier: from a letter to a non-digit and non-letter character  
getValue: represented by ":=" characters  
notEqual: represented by "<>" characters  
lessEqual: represented by "<=" characters  
greaterEqual: represented by ">=" characters  
isEqual: represented by "==" characters  
Comment1: Started with '{' and end with '}'  
Comment2: Started with "(*" and end with "*)"

The second program was a __TopDownAnalyzer__ what can determines whether the world input can be derived from the rules. If the word can be derived, the rogram writes the derivation steps to the console.  

Reading from the condolse in this format: word#rules (example: ba#S-TS|T,T-a|b)  
Terminals: non-capital letters  
Non-terminals: capital letters  
The program use left derivations and it's left recursion free.  
The backtrack (or rollback) operations is solved with a simple variable replace.  
