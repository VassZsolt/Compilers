package com.company.TopDownAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class TopDownAnalyzer {

    public static String input;

    public static String getInput() {
        Scanner read=new Scanner(System.in);
        input=read.nextLine(); //"For example: ba#S-TS|T,T-a|b"
        //String input="ab#S-TS|T,T-a|b";
        //String input="qwerty#S-qweZ,Z-Fty,F-r";
        //String input="qwert#S-abc|qweZ,Z-abc";

        return input;
    }
    public static List<String> getAllRules(String s) { //s contains the input in a same format (word#S-TS|T,T-a|b)
        List<String> rules=new ArrayList<>();
        String[] rawRules=s.split("#"); //separate word and rules
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
    public static String getWord(String s){ //s contains the input in a same format (word#S-TS|T,T-a|b)
        String [] rawInput=s.split("#"); //separate the input to word and rules
        return rawInput[0];
    }
    public static List<String> getRulesFor(char nonTerminal) {

        List <String> rules = getAllRules(input);
        List<String> rulesForNonTerminal=new ArrayList<>(); //only contains the possible rules for nonTerminal
        for (String rule : rules) {
            if (rule.charAt(0) == nonTerminal) {
                rulesForNonTerminal.add(rule);
            }
        }
        return rulesForNonTerminal;
    }


    /*
    public static String applyNextRuleFor(char nonTerminal, String generatedWord, int numberOfRule)
    {

        List<String> nonTerminalLetterAndPosition=new ArrayList<>();
        //List<Integer>nonTerminalAppliedRuleNumber=new ArrayList<>();

        for(int i=0; i<generatedWord.length();i++) { //megállapitom hol fordul elő a keresett nemterminális
            if (generatedWord.charAt(i) == nonTerminal) {
                if (!nonTerminalLetterAndPosition.contains(nonTerminal + "-" + String.valueOf(i))) {
                    nonTerminalLetterAndPosition.add(nonTerminal + "-" + String.valueOf(i));
                    //nonTerminalAppliedRuleNumber.add(0);
                }
            }
        }

            List <String> rules=getRulesFor(nonTerminal);
            String eleje="", vége="", közepe="";

            String word =getWord(getInput());

            int i=generatedWord.length()-1;
            while (i>=0)
            {
                if(generatedWord.charAt(i)==nonTerminal) {
                    for (int j=0; j<nonTerminalLetterAndPosition.size();j++) {
                        if(Integer.parseInt(nonTerminalLetterAndPosition.get(j).substring(2))==i) {
                            vége=generatedWord.substring(Integer.parseInt(nonTerminalLetterAndPosition.get(j).substring(2))+1);
                            //közepe=rules.get(nonTerminalAppliedRuleNumber.get(j)).substring(2);
                            if(rules.size()>=numberOfRule)
                            {
                                közepe=rules.get(numberOfRule).substring(2);
                            }
                            else {
                                out.println("nem levezethető");
                                return "";
                            }
                            eleje=generatedWord.substring(0,Integer.parseInt(nonTerminalLetterAndPosition.get(j).substring(2)));
                            generatedWord=eleje+közepe+vége;
                            //nonTerminalAppliedRuleNumber.set(j,nonTerminalAppliedRuleNumber.get(j)+1);

                            out.println(generatedWord);
                        }
                    }
                }
                i--;
            }

    return generatedWord;
    }
    */

    public static String applyThisRuleFor(int ruleNumber,int indexOfNonTerminal,char nonTerminal, String generatedWord){
        List<String> rules=getRulesFor(nonTerminal);
        if(ruleNumber>=rules.size())
        {
            out.println("NEM LEVEZETHETŐ, mert nincs rá szabály");
            return generatedWord="";
        }
        else {
            String eleje="", közepe="", vége="";
            vége=generatedWord.substring(indexOfNonTerminal+1);
            közepe=rules.get(ruleNumber).substring(2);
            eleje=generatedWord.substring(0,indexOfNonTerminal);
            generatedWord=eleje+közepe+vége;
        }
        return generatedWord;
    }

    public static void main(String[] args)
    {
        out.println("This program is only work with the right formatted input.");
        out.println("Please write an input word and the substitution rules in this format: word#S-TS|T,T-a|b");

        char StartSymbol='S';
        List <String> memory=new ArrayList<>();
        String generatedWord=String.valueOf(StartSymbol);
        memory.add(generatedWord);




        String word=getWord(getInput());
        int index=0;
        int numberOfRule=0;
        char lastNonTerminal='?';

        while(!word.equals(generatedWord)) {

                if(lastNonTerminal!=generatedWord.charAt(index))
                {
                    numberOfRule=0;
                }

                if (generatedWord.charAt(index) > 64 && generatedWord.charAt(index) < 91) { //It's a non terminal letter (capital letter)
                    lastNonTerminal=generatedWord.charAt(index);
                    generatedWord=applyThisRuleFor(numberOfRule,index,lastNonTerminal,generatedWord);
                    if(!memory.contains(generatedWord)) {
                        memory.add(generatedWord);
                    }
                    if(generatedWord.length()==0) {
                        break;
                    }
                    if(generatedWord.length()> word.length()|| ((generatedWord.charAt(index)>96 && generatedWord.charAt(index)<123) && generatedWord.charAt(index)!=word.charAt(index)))
                    {
                        memory.remove(memory.size()-1);
                        generatedWord= memory.get(memory.size()-1);
                    }
                }


            if(generatedWord.charAt(index)==word.charAt(index)) {
                index++;
                //out.println("Index now: "+index);
                numberOfRule=0;
            }
            else
            {
                numberOfRule++;
            }

        }

        out.println("------------------------------");
        for (String s : memory) {
            out.println(s);
        }
    }
}
