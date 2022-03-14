package com.company;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.out;

public class LexicalAnalyzer {
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

        char actualCharacter, lastCharacter='?';
        boolean isComment1=false;
        boolean isComment2=false;

        for (int i = 0; i < input.length(); i++)
        {
            actualCharacter= input.charAt(i);
            if(isOperator(actualCharacter) && i>0) //Can't be the first character and only used for operators
            {
                lastCharacter=input.charAt(i-1);
            }
            if(!isComment1 && !isComment2)
            {
                if(isNumber(actualCharacter) && !isIdentifier) //It's a number (constant)
                {
                    appendOutput("<constant>");
                }
                if(isLetter(actualCharacter)) //It's a letter (identifier)
                {
                    appendOutput("<identifier>");
                    isIdentifier=true;
                }
                if(actualCharacter=='=')
                {
                    if(lastCharacter==':') // ":=" (get value)
                    {
                        appendOutput("<getValue>");
                    }
                    if(lastCharacter=='<') // "<=" (less equal)
                    {
                        appendOutput("<lessEqual>");
                    }
                    if(lastCharacter=='>') // ">=" (greater equal)
                    {
                        appendOutput("<greaterEqual>");
                    }
                    if(lastCharacter=='=') // "==" (is equal?)
                    {
                        appendOutput("<isEqual>");
                    }
                }
                if(lastCharacter==60 && actualCharacter==62) // "<>" (not equal)
                {
                    appendOutput("<notEqual>");
                }
                if (actualCharacter==123) // '{' (begin of comment1)
                {
                    isComment1=true;
                    appendOutput("<{}Comment1>");
                }
                if(lastCharacter==40 && actualCharacter==42) // "(*" (begin of comment2)
                {
                    isComment2=true;
                    appendOutput("<(**)Comment2>");
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

    private static final ArrayList<String> output = new ArrayList<>();
    private static boolean isIdentifier=false; //used to include numbers to identifier
    private static boolean lastWasOperator=false; //used to avoid unexpected operator duplication like "<== as lessEqual and isEqual"

    private static void appendOutput(String s) {
        if(output.size()==0) //The list with 0 size must be extended
        {
            output.add(s);
        }
        if(!Objects.equals(s, output.get(output.size() - 1)) && !lastWasOperator) //check the last item to avoid duplications like "<constant><constant>"
        {
            output.add(s);
        }
        //check the last item to avoid unexpected operator duplication
        lastWasOperator=
                (Objects.equals("<lessEqual>", output.get(output.size() - 1))
                || Objects.equals("<greaterEqual>", output.get(output.size() - 1))) && !Objects.equals(s, "<isEqual>")
                && !lastWasOperator;
        if(!Objects.equals("<identifier>",output.get(output.size()-1)))
        {
            isIdentifier=false;
        }
    }

    private static boolean isLetter(char actualCharacter) {
        return actualCharacter > 64 && actualCharacter < 91 || actualCharacter > 96 && actualCharacter < 123;
    }

    private static boolean isNumber(char actualCharacter) {
        return actualCharacter > 47 && actualCharacter < 58;
    }

    private static boolean isOperator(char actualCharacter) {
        return actualCharacter == '=' || actualCharacter == '>' || actualCharacter == ')' || actualCharacter == '*';
    }
}