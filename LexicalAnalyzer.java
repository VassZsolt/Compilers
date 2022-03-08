package com.company;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.out;

public class LexicalAnalyzer {
    public static ArrayList<String> output = new ArrayList<>();
    static boolean isIdentifier=false; //used to include numbers to identifier
    static boolean lastWasOperator=false; //used to avoid unexpected operator duplication like "<== as lessEqual and isEqual"

    public static void appendOutput(String s)
    {
        if(output.size()==0) //The list with 0 size must be extended
        {
            output.add(s);
        }
        if(!Objects.equals(s, output.get(output.size() - 1)) && !lastWasOperator) //check the last item to avoid duplications like "<constant><constant>"
        {
            output.add(s);
        }
        if((Objects.equals("<lessEqual>",output.get(output.size()-1)) || Objects.equals("<greaterEqual>",output.get(output.size()-1) )) && !Objects.equals(s,"<isEqual>") && !lastWasOperator) //check the last item to avoid unexpected operator duplication
        {
            lastWasOperator=true;
        }
        else
        {
            lastWasOperator=false;
        }
        if(!Objects.equals("<identifier>",output.get(output.size()-1)))
        {
            isIdentifier=false;
        }
    }
    public static void main(String[] args) {

        //Task: Analyze input text and get the identifiers, constants, operators and comments.

        Scanner myInput= new Scanner(System.in);
        String input=myInput.nextLine();

        /**
         * constant: from a digit to a non-digit character
         * identifier: from a letter to a non-digit and non-letter character
         * getValue: represented by ":=" characters
         * notEqual: represented by "<>" characters
         * lessEqual: represented by "<=" characters
         * greaterEqual: represented by ">=" characters
         * isEqual: represented by "==" characters
         * Comment1: Started with '{' and end with '}'
         * Comment2: Started with "(*" and end with "*)"
         */

        String [] options  = {"<constant>", "<identifier>", "<getValue>", "<notEqual>", "<lessEqual>", "<greaterEqual>", "<isEqual>", "<{}Comment1>", "<(**)Comment2>"};

        char actualCharacter, lastCharacter='?';
        boolean isComment1=false;
        boolean isComment2=false;

        for (int i = 0; i < input.length(); i++)
        {
            actualCharacter= input.charAt(i);
            if((actualCharacter=='=' || actualCharacter=='>' || actualCharacter==')' || actualCharacter=='*') && i>0) //Can't be the first character
            {
                    lastCharacter=input.charAt(i-1);
            }
            if(!isComment1 && !isComment2)
            {
                if(actualCharacter>47 && actualCharacter<58 && !isIdentifier) //It's a number (constant)
                {
                    appendOutput(options[0]);
                }
                if(actualCharacter>64 && actualCharacter<91 || actualCharacter>96 && actualCharacter<123) //It's a letter (identifier)
                {
                    appendOutput(options[1]);
                    isIdentifier=true;
                }
                if(actualCharacter==61) //actual character is '='
                {
                    if(lastCharacter==58) // ":=" (get value)
                    {
                        appendOutput(options[2]);
                    }
                    if(lastCharacter==60) // "<=" (less equal)
                    {
                        appendOutput(options[4]);
                    }
                    if(lastCharacter==62) // ">=" (greater equal)
                    {
                        appendOutput(options[5]);
                    }
                    if(lastCharacter==61) // "==" (is equal?)
                    {
                        appendOutput(options[6]);
                    }
                }
                if(lastCharacter==60 && actualCharacter==62) // "<>" (not equal)
                {
                    appendOutput(options[3]);
                }
                if (actualCharacter==123) // '{' (begin of comment1)
                {
                    isComment1=true;
                    appendOutput(options[7]);
                }
                if(lastCharacter==40 && actualCharacter==42) // "(*" (begin of comment2)
                {
                    isComment2=true;
                    appendOutput(options[8]);
                }
            }
            if(actualCharacter==125 && isComment1) // '}' (end of comment1)
            {
                isComment1=false;
            }
            if(lastCharacter==42 && actualCharacter==41 && isComment2) // "*)" (end of comment2)
            {
                isComment2=false;
            }
        }
        for (String s : output) {
            out.println(s);
        }
        out.println("<EOF>\n");
    }
}