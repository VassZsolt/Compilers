package com.company.TopDownAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class TopDownAnalyzer {
    /**
     * The program determines whether the world input
     * can be derived from the rules.
     * If the word can be derived, the program writes the derivation steps to the console.
     */
    public static String input;

    public static String getInput() { //Reading from the Console
        Scanner read=new Scanner(System.in);
        input=read.nextLine();
        //Example0 ba#S-TS|T,T-a|b"
        //Example1 "ab#S-TS|T,T-a|b";
        //Example2 "qwerty#S-qweZ,Z-Fty,F-r";
        //Example3 "qwert#S-abc|qweZ,Z-abc";

        if (input.contains("+")) { //Only needed for school assignments
            input=input.replaceAll("\\+","");
        }

        return input;
    }
    public static List<String> getAllRules(String s) {
        //s contains the input in a same format (word#S-TS|T,T-a|b) and return a list which contains the all the rules
        List<String> rules=new ArrayList<>();
        String[] rawRules=s.split("#"); //separate input to word and rules
        rawRules=rawRules[1].split(","); //contains the raw rules like (S-TS|T)
        char tempNonTerminal;

        for (String rawRule : rawRules) {
            tempNonTerminal=rawRule.charAt(0);
            String[] tempRuleAlternatives=rawRule.substring(2).split("\\|"); //tempRuleAlternatives contains the substitution possibilities of our non-terminal letter
            for (String tempRuleAlternative : tempRuleAlternatives) {
                if (!rules.contains(tempNonTerminal + "-" + tempRuleAlternative)) { //if the actual rule is unknown yet than put it to rules
                    rules.add(tempNonTerminal + "-" + tempRuleAlternative);
                }
            }
        }
        return rules;
    }
    public static String getWord(String s){ //s contains the input in a same format (word#S-TS|T,T-a|b) and return the word
        String [] rawInput=s.split("#"); //separate the input to word and rules
        return rawInput[0];
    }
    public static List<String> getRulesFor(char nonTerminal) {
        //gets a non-terminal letter and return the rules which can be derived from the non-terminal
        List <String> rules = getAllRules(input);
        List<String> rulesForNonTerminal=new ArrayList<>(); //only contains the possible rules for nonTerminal
        for (String rule : rules) {
            if (rule.charAt(0) == nonTerminal) {
                rulesForNonTerminal.add(rule);
            }
        }
        return rulesForNonTerminal;
    }
    public static String applyThisRuleFor(int ruleNumber,int indexOfNonTerminal,char nonTerminal, String generatedWord){
        //retrieves non-terminal and the number of rule and if possible, returns the generated word after using the rule
        List<String> rules=getRulesFor(nonTerminal);
        if(ruleNumber>=rules.size())
        {
            out.println("The input word can't be derived because there are no rule for it");
            return generatedWord="";
        }
        else {
            String firstPartOfWord, generatedPartOfWord, lastPartOfWord;
            lastPartOfWord=generatedWord.substring(indexOfNonTerminal+1);
            generatedPartOfWord=rules.get(ruleNumber).substring(2);
            firstPartOfWord=generatedWord.substring(0,indexOfNonTerminal);
            generatedWord=firstPartOfWord+generatedPartOfWord+lastPartOfWord;
        }
        return generatedWord;
    }

    public static void main(String[] args)
    {
        out.println("This program is only work with the right formatted input.");
        out.println("Please write an input word and the substitution rules in this format: word#S-TS|T,T-a|b");

        char StartSymbol='S'; //In basic terminology it's S, but it can be everything else
        List <String> memory=new ArrayList<>(); //Includes derivation steps
        String generatedWord=String.valueOf(StartSymbol);
        memory.add(generatedWord);

        String word=getWord(getInput());
        int index=0; //progress in the word to be generated
        int numberOfRule=0;
        char lastNonTerminal='?'; //the simplest solution for backTrack

        while(!word.equals(generatedWord)) {
                if(lastNonTerminal!=generatedWord.charAt(index))
                {
                    numberOfRule=0;
                }

                if (isNonTerminal(generatedWord, index)) { //It's a non terminal letter (capital letter)
                    lastNonTerminal=generatedWord.charAt(index);
                    generatedWord=applyThisRuleFor(numberOfRule,index,lastNonTerminal,generatedWord);
                    if(!memory.contains(generatedWord)) { //add last step of derivation to the memory
                        memory.add(generatedWord);
                    }
                    if(generatedWord.length()==0) {//If the word can't be derived break the whole iteration
                        break;
                    }
                    if(generatedWord.length()> word.length()||
                    //shouldn't generate longer word than the original one
                      (isTerminal(generatedWord, index) && generatedWord.charAt(index)!=word.charAt(index)))
                    //and shouldn't get wrong terminal letter
                    {
                        memory.remove(memory.size()-1);
                        generatedWord= memory.get(memory.size()-1); //the backtrack (rollback)
                    }
                }

            if(generatedWord.charAt(index)==word.charAt(index)) { //in case of match go to the next letter
                index++;
                numberOfRule=0;
            }
            else
            {
                numberOfRule++;
            }
        }

        for (String s : memory) {
            out.println(s);
        }
    }

    private static boolean isTerminal(String generatedWord, int index) {//If it's a non-capital letter return true
        return generatedWord.charAt(index) > 96 && generatedWord.charAt(index) < 123;
    }

    private static boolean isNonTerminal(String generatedWord, int index) { //If it's a capital letter return true
        return generatedWord.charAt(index) > 64 && generatedWord.charAt(index) < 91;
    }
}
