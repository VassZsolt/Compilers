package com.company;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class CockeYoungerKasami {

    /**
     * This program is the Cocke-Younger-Kasami algorithm implementation in Java.
     */

    public static void main(String[] args) {
        out.println("The Cocke-Younger-Kasami algorithm implementation");
        out.println("The max length of input word is: 10.");
        initialiseComponent();
    }

    private static void initialiseComponent() {
        InitialiseComponents components = new InitialiseComponents();
        String inputWord = components.getInputWord();
        if(inputWord.length()>10) //Don't analyze longer words than 10 chars.
        {
            out.println("The max length of input word is: 10.");
            return;
        }

        List<String> rulesLeft=components.getRulesLeft();
        List<String> rulesRight=components.getRulesRight();

        generateFirstRow(rulesLeft,rulesRight, inputWord);
    }

    private static void generateFirstRow(List<String> rulesLeft, List<String> rulesRight, String inputWord){
        StringBuilder multiple = new StringBuilder(); //multiple contains all the right non-terminals for example A-a and B-a so multiple=AB
        String [][] table=new String[10][10];
        List<String>firstRow=new ArrayList<>();//contains non-terminals which can be derived our input word
        List<String>leftValue=new ArrayList<>();

        for(int i=0; i<inputWord.length();i++) {
            for(int j=0; j<rulesRight.size(); j++){
                if(String.valueOf(inputWord.charAt(i)).equals(rulesRight.get(j))) {
                    //If generated word actual letter is equal with our rule,
                    //write the non-terminal (the rule left side) to the table first row.
                    leftValue.add(rulesLeft.get(j));
                }
            }
            for (String s : leftValue) {
                multiple.append(s);
            }
            firstRow.add(multiple.toString());
            multiple = new StringBuilder();
            leftValue.clear();
        }

        for(int i=0; i<1; i++) {
            for(int j=0; j<firstRow.size(); j++) {
                table[i][j]=firstRow.get(j); //write firstRow elements to the table first row
            }
        }
        out.println("This is the first row:");
        for (int l = 0; l < firstRow.size();l++) {
            out.print(table[0][l]+" ");
        }
        out.println();
        int rowNumber=1, tableSize= firstRow.size();
        generateOtherRows(rulesLeft,rulesRight, table, rowNumber, tableSize);
    }
    private static void generateOtherRows(List<String> rulesLeft, List<String> rulesRight, String[][] table, int rowNumber, int tableSize){
        String combined=""   //combined contains the value of two cells
                , multiple="";//multiple contains all the right non-terminals for example A-a and B-a so multiple=AB

        for(int row=0; row<rowNumber; row++) {
            for(int column=0; column<tableSize; column++) {
                    if(column+1< tableSize) {
                        for(int cell1Char=0; cell1Char<table[row][column].length();cell1Char++){
                            for(int cell2Char=0; cell2Char<table[rowNumber-(row+1)][row+column+1].length(); cell2Char++){
                                combined=combined+table[row][column].charAt(cell1Char)+table[rowNumber-(row+1)][row+column+1].charAt(cell2Char);
                                for(int n=0; n<rulesRight.size();n++) {
                                    if(combined.equals(rulesRight.get(n))) {
                                        multiple=multiple+rulesLeft.get(n);
                                    }
                                }
                                combined="";
                            }
                        }
                        String tmp=table[rowNumber][column];
                        if(tmp !=null)
                        {
                            table[rowNumber][column]=table[rowNumber][column]+multiple;
                        }
                        else {
                            table[rowNumber][column] = multiple;
                        }
                        multiple="";
                    }
            }
        }
        if(tableSize>1) {
            out.println("This is the "+(rowNumber+1)+"th row:");
            for(int i=0; i<tableSize-1;i++) {
                if(table[rowNumber][i].equals("")) {
                    out.print("- ");
                }
                else {
                    out.print(table[rowNumber][i]+" ");
                }
            }
            out.println();
            rowNumber++;
            tableSize--;
            generateOtherRows(rulesLeft,rulesRight,table, rowNumber, tableSize);
        }
    }
}