package com.company;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class TopDownAnalyzer {
    /**
     * The program determines whether the world input
     * can be derived from the rules.
     * If the word can be derived, the program writes the derivation steps to the console.
     */

    public static void main(String[] args) {
        out.println("This program is only work with the right formatted input.");
        initialiseComponents();
    }
    public static InitialiseComponents components=new InitialiseComponents();
    private static void initialiseComponents(){
        char StartSymbol='S'; //In basic terminology it's S, but it can be everything else
        List <String> memory=new ArrayList<>(); //Includes derivation steps
        String generatedWord=String.valueOf(StartSymbol);
        memory.add(generatedWord);


        String word= components.getInputWord();

        generateWord(generatedWord, word, memory);
    }
    private static void generateWord(String generatedWord, String word, List<String> memory){
        int index=0; //progress in the word to be generated
        int numberOfRule=0;
        char lastNonTerminal='?'; //the simplest solution for backTrack

        while(!word.equals(generatedWord)) {
            if(lastNonTerminal!=generatedWord.charAt(index))
            {
                numberOfRule=0;
            }

            if (components.isNonTerminal(generatedWord, index)) { //It's a non terminal letter (capital letter)
                lastNonTerminal=generatedWord.charAt(index);
                generatedWord=components.applyThisRuleFor(numberOfRule,index,lastNonTerminal,generatedWord);
                if(!memory.contains(generatedWord)) { //add last step of derivation to the memory
                    memory.add(generatedWord);
                }
                if(generatedWord.length()==0) {//If the word can't be derived break the whole iteration
                    break;
                }
                if(generatedWord.length()> word.length()||
                        //shouldn't generate longer word than the original one
                        (components.isTerminal(generatedWord, index) && generatedWord.charAt(index)!=word.charAt(index)))
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
}