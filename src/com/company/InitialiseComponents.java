package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class InitialiseComponents {
    String input;
    public InitialiseComponents(){
        out.println("Please write the input in this format:word#S-TS|T,T-a|b");

        Scanner read=new Scanner(System.in);
        input= read.nextLine();

        //Example0 ba#S-TS|T,T-a|b"
        //Example1 "ab#S-TS|T,T-a|b";
        //Example2 "qwerty#S-qweZ,Z-Fty,F-r";
        //Example3 "qwert#S-abc|qweZ,Z-abc";
        //Example4 "aabbaba#S-AB|CD|CB|SS,A-BC|a,B-SC|b,C-DD|b,D-BA";

        if (input.contains("+")) { //Only needed for school assignments
            input=input.replaceAll("\\+","");
        }

    }

    private List<String> getAllRules(String s) {
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
    public String applyThisRuleFor(int ruleNumber,int indexOfNonTerminal,char nonTerminal, String generatedWord){
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
    public List<String> getRulesFor(char nonTerminal) {
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


    String token="-"; //Token separate a rule right and left side

    public List<String> getRulesLeft() {
        List<String> rulesLeft=new ArrayList<>();
        List<String> rules=getAllRules(input);
        for(String rule: rules) {
            int position=rule.indexOf(token);
            rulesLeft.add(rule.substring(0,position));
        }
        return rulesLeft; //Return the rules left side
    }
    public List<String> getRulesRight() {
        List<String> rulesRight=new ArrayList<>();
        List<String> rules=getAllRules(input);
        for(String rule: rules) {
            int position=rule.indexOf(token);
            rule=rule.replace(rule.substring(0,position+1),""); //delete left side and the token
            rulesRight.add(rule);
        }
        return rulesRight; //Return the rules right side
    }
    public String getInputWord(){ //s contains the input in a same format (word#S-TS|T,T-a|b) and return the word
        String [] rawInput=input.split("#"); //separate the input to word and rules
        return rawInput[0];
    }

    public boolean isTerminal(char character){//If it's a non-capital letter return true
        return character>96 && character<123;
    }

    public boolean isNonTerminal(char character) { //If it's a capital letter return true
        return character>64 && character<91;
    }
    public List<Character> getTableLeftElements(List<String> rulesLeft){
        List<Character> leftElements=new ArrayList<>();
        for (String s : rulesLeft) {
            for (int i = 0; i < s.length(); i++) {
                if (isNonTerminal(s.charAt(i))) {
                    if (leftElements.size() > 0) {
                        for (int j = 0; j < leftElements.size(); j++) {
                            if (leftElements.get(j) == s.charAt(i)) {
                                break;
                            } else if (j + 1 == leftElements.size()) {
                                leftElements.add(s.charAt(i));
                            }
                        }
                    } else {
                        leftElements.add(s.charAt(i));
                    }
                }
            }
        }
        return leftElements;
    }
    public List<Character> getTableRightElements(List<String> rulesRight) {
        List<Character> rightElements=new ArrayList<>();
        for (String s : rulesRight) {
            for (int i = 0; i < s.length(); i++) {
                if (isTerminal(s.charAt(i))) {
                    if (rightElements.size() > 0) {
                        for (int j = 0; j < rightElements.size(); j++) {
                            if (rightElements.get(j) == s.charAt(i)) {
                                break;
                            } else if (j + 1 == rightElements.size()) {
                                rightElements.add(s.charAt(i));
                            }
                        }
                    } else {
                        rightElements.add(s.charAt(i));
                    }
                }
            }
        }
        rightElements.add('#');
        return rightElements;
    }
}
