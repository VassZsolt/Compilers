package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {

    public static List<String> GetRules(String[] splittedInput)
    {
        List<String> substitutionRules=new ArrayList<>(); //In format (A1-a or B1-x)
        String temp; //Will be different meanings
        String[] temporary;
        int ruleCounter=0; //count the rules from a non-terminal expression

        for (int i=0; i<splittedInput.length;i++)
        {
            temp=splittedInput[i].substring(2); //Contains the alternatives
            temporary=temp.split("\\|");
            for(int j=0; j< temporary.length; j++)
            {
                ruleCounter=0;
                for(int k=0; k< substitutionRules.size();k++)
                {
                    if(substitutionRules.get(k).startsWith(splittedInput[i].substring(0,1)))
                    {
                        ruleCounter++;
                    }
                }
                temp=splittedInput[i].substring(0,1)+String.valueOf(ruleCounter)+"-"+temporary[j];
                if(!substitutionRules.contains(temp))
                {
                    substitutionRules.add(temp);
                }
            }
        }
        return substitutionRules;
    }

    public static void main(String[] args) {
        out.println("Write the generated word and the substitution rules in this format:word#rules");
        //out.println("For example: aba#S-AAA,A-a|b");
        //out.println("For example: abb#S-AAA,A-a|b");
        out.println("For example: ba#S-TS|T,T-a|b");
        //Scanner read = new Scanner(System.in);
        //String input = read.nextLine();
        String input="ba#S-TS|T,T-a|b";
        out.println();

        String[] splittedInput=input.split("#");
        String w=splittedInput[0]; //It should be the generated word
        splittedInput=splittedInput[1].split(","); //Separate the rules
        List<String> configuration=new ArrayList<>(); //In format (status, pointer, generated, history, stillRequired)
        configuration.add("q,0,lambda,lambda,S"); //It's a fix staring config (default q,1,lambda,S)!!!!!!!!

        List<String> substitutionRules=GetRules(splittedInput); //Got the Rules in this format: ["A0-a", "A1-b"]


        /*
        Configuration build:
        ---------------------
        In format (status, pointer, generated, history, stillRequired)
        status: It's a boolean, true-accepted false-denied
        pointer: It's an integer, indexed the letters in our word
        generated: It's a string, which contains the already generated word
        history: It's a string, which contains the already used Rules
        stillRequired: It's a string, which contains the word non-generated part that's still required.
         */

        boolean status=true;
        int pointer=0;
        String generated="lambda";
        String history="lambda";
        String stillRequired=w; //The complete word

        String temp="start";
        boolean finish=false;
        while(!finish) {
            if (temp.equals("start")) {
                for (String substitutionRule : substitutionRules) {
                    if (substitutionRule.startsWith("S0")) {
                        temp = "";
                        generated = String.valueOf(substitutionRule.substring(3));
                        history = "S0";
                    }
                }
            }

            for (int i = 0; i < generated.length(); i++) { //Make terminal expressions from non-terminals
                temp = String.valueOf(generated.substring(i, i + 1));
                char helper=temp.charAt(0);
                if (64< helper&& 91>helper ) //ASCII codes can help us find out isNonTerminal or isTerminal
                {
                    //It's a non-terminal letter
                    temp=temp+"0";
                    for (int j = 0; j < substitutionRules.size(); j++) {
                        if (temp.equals(substitutionRules.get(j).substring(0, 2)) && generated.length()<=w.length()) {
                            //Use the first rule for this nonTerminal expression
                            generated = generated.substring(0,i)+substitutionRules.get(j).substring(3)+generated.substring(i+1);
                            history = history + temp;
                        }
                    }
                }
            }

            boolean lastRuleChanged =false;

            for (int i = 0; i < generated.length(); i++) {
                if(pointer!=w.length()) {
                    temp = String.valueOf(generated.substring(pointer, pointer + 1));
                    char helper=temp.charAt(0);
                    if (helper > 96 && helper < 123) {
                        //It's a terminal letter

                        temp = generated.substring(pointer, pointer + 1);
                        if (temp.equals(w.substring(pointer, pointer + 1))) {
                            pointer++;
                            stillRequired = w.substring(pointer);
                        } else {
                            status = false; //Backtrack needed
                        }

                    }
                    else //backtrack needed v2 (used for nonTerminal letters)
                    {
                        lastRuleChanged=false;
                        int cauntOfChangedLetter=0;
                        for(int j=0; j<substitutionRules.size();j++) {
                            if(substitutionRules.get(j).contains(temp) && lastRuleChanged==false) //got how made the bad nonTerminal letter
                            {
                                temp= substitutionRules.get(j).charAt(0)+String.valueOf(Integer.valueOf(substitutionRules.get(j).charAt(1))-47);
                                cauntOfChangedLetter=substitutionRules.get(j).substring(3).length();
                                lastRuleChanged=true;
                            }
                        }
                        for(int j=0; j<substitutionRules.size();j++)
                        {
                            if(substitutionRules.get(j).contains(temp))
                            {
                                generated=generated.substring(0,pointer)+substitutionRules.get(j).substring(3)+generated.substring(pointer+cauntOfChangedLetter);
                                history=history.substring(0,2+pointer*2)+substitutionRules.get(j).substring(0,2)+history.substring(4+pointer*2);
                                //Use the next rule for this nonTerminal expression
                            }
                        }
                    }
                }

                if (!status) //Backtrack definition
                {
                    int db=0; //The searched rule exist?
                    for(int j=0; j< substitutionRules.size();j++)
                    {
                        db++;
                        if(substitutionRules.get(j).contains(temp) && lastRuleChanged==false) //got how made the bad terminal letter
                        {
                            db--;
                            temp= substitutionRules.get(j).substring(0,1)+String.valueOf(Integer.valueOf(substitutionRules.get(j).charAt(1))-47); //-------------------------------------------------------------------------
                            //define the next rule
                            lastRuleChanged=true;

                        }
                        if(substitutionRules.get(j).contains(temp))
                        {
                            db--;
                            generated=generated.substring(0,pointer)+substitutionRules.get(j).substring(3)+generated.substring(pointer+1);
                            history=history.substring(0,2+pointer*2)+substitutionRules.get(j).substring(0,2)+history.substring(4+pointer*2);
                            //Use the next rule for this nonTerminal expression
                        }
                        if(db==substitutionRules.size())
                        {
                            finish=true;
                            break;
                            //There are no more rule from this NonTerminal expression
                        }
                    }
                    status = true;
                }
            }

            if(pointer==w.length() || generated.equals(w))
            {
                out.println("The word: "+w);
                if(generated.equals(w))
                {
                    out.println("We can generate the input word.");
                    out.println("One of the possible definitions:"+ history);
                }
                else
                {
                    out.println("We can't generate the input word from these rules:");
                    for(int i=0; i< substitutionRules.size();i++)
                    {
                        out.println(substitutionRules.get(i));
                    }
                }
                finish=true;
            }
        }
        //read.close();
    }
}
