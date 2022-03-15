package com.company;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class ELL_1 {
    public static InitialiseComponents components=new InitialiseComponents();
    public static void main(String [] args){
        out.println("This is a ELL(1) algorithm implementations. (Simply LL(1) Analyzer)");
        out.println("Input example:#S-aS|bAc,A-bAc|d"); //#S-aS|bAc,A-bAc|d
        initialiseComponents();
    }

    public static void initialiseComponents() {
            components.getInputWord();
            List<String> rulesLeft =components.getRulesLeft();
            List<String> rulesRight =components.getRulesRight();

            table(rulesLeft,rulesRight);
    }

    public static void table(List<String> rulesLeft, List<String> rulesRight) {
        //List<Character> leftElements = getTableLeftElements(rulesLeft);
        List<Character> rightElements = components.getTableRightElements(rulesRight);
        List<Character> leftAndRightElements = components.getTableLeftElements(rulesLeft);
        List<Integer> indexOfLeftElements = new ArrayList<>();
        List<String> indexOfRightElements = new ArrayList<>();

        leftAndRightElements.addAll(rightElements);
        out.println("The table:");

        //the columns of the table.
        for (Character rightElement : rightElements) {
            out.print("\t"+rightElement+"\t");
        }

        for (Character leftAndRightElement : leftAndRightElements) {
            out.println();
            out.print(leftAndRightElement +"\t" );
            for (Character rightElement : rightElements) {
                if (components.isNonTerminal(leftAndRightElement)) {
                    for (int j = 0; j < rulesLeft.size(); j++) {
                        if (leftAndRightElement == rulesLeft.get(j).charAt(0)) {
                            indexOfLeftElements.add(j);
                        }
                    }
                    for (Integer indexOfLeftElement : indexOfLeftElements) {
                        indexOfRightElements.add(rulesRight.get(indexOfLeftElement));
                    }
                    if (rightElement == indexOfRightElements.get(0).charAt(0)) {
                        out.print("(" + indexOfRightElements.get(0) + "," + Integer.parseInt(String.valueOf(indexOfLeftElements.get(0) + 1)) + ")" + "\t");
                    } else if (rightElement == indexOfRightElements.get(1).charAt(0)) {
                        out.print("(" + indexOfRightElements.get(1) + "," + Integer.parseInt(String.valueOf(indexOfLeftElements.get(1) + 1)) + ")" + "\t");
                    } else {
                        out.print("error" + "\t");
                    }
                    indexOfLeftElements.clear();
                    indexOfRightElements.clear();
                } else if (leftAndRightElement == '#' && rightElement == '#') {
                    out.print("accept" + "\t");
                } else if (leftAndRightElement == rightElement) {
                    out.print("pop  " + "\t");
                } else {
                    out.print("error" + "\t");
                }
            }
        }
    }
}
