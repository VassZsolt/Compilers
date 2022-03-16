package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

import static java.lang.System.out;

public class LLL1 {
    public static InitialiseComponents components=new InitialiseComponents();
    public static void main(String [] args) {
        out.println("This is a LLL(1) algorithm implementations. (Lambda free LL(1)");
        out.println("Input example:#S-aS|bAc,A-bAc|d"); //#S-aS|bAc,A-bAc|d
        out.println("Input example:#S-ABC,A-a|Bbc|Ccd,B-bBb|cCc,C-dDd|Dd,D-e"); //#S-ABC,A-a|Bbc|Ccd,B-bBb|cCc,C-dDd|Dd,D-e
        initialiseComponent();
    }

    public static void initialiseComponent(){
        List<String> rulesLeft=components.getRulesLeft();
        List<String> rulesRight=components.getRulesRight();

        generateTableFirstRow(rulesLeft,rulesRight);
    }

    public static void generateTableFirstRow(List<String> rulesLeft, List<String> rulesRight) {
        List<Character> leftElements=components.getTableLeftElements(rulesLeft);
        List<Integer> indexOfLeftElements=new ArrayList<>();
        String [][] table=new String[5][5];
        String mixed="";
        int counter=0;

        for (Character leftElement : leftElements) {
            for (String rule : rulesLeft) {
                if (leftElement == rule.charAt(0)) {
                    counter++;
                }
            }
            indexOfLeftElements.add(counter);
            counter=0;
        }



        out.println("This is the table:");
        for (Character leftElement : leftElements) {
            out.print("        "+leftElement);
        }
        out.println();
        out.print("H0    ");

        for(int i=0; i<leftElements.size();i++){
            for(int j=0;j<indexOfLeftElements.get(i);j++) {
                if(components.isTerminal(rulesRight.get(counter).charAt(0))){
                    if(mixed.equals("-")){
                        mixed="";
                    }
                    mixed+=rulesRight.get(counter).charAt(0);
                }
                else if (components.isNonTerminal(rulesRight.get(counter).charAt(0))){
                    if(mixed.equals("")){
                        mixed+="-";
                    }


                }
                counter++;
            }
            table[0][i]=mixed;
            mixed="";
        }
        int size=table[0].length/table[0][0].length();
        for(int i=0;i<size;i++) {
            out.print("      "+table[0][i]);
        }
        out.println();
        int value=1;
        generateTableOtherRows(rulesLeft,rulesRight,table,indexOfLeftElements,value);
    }
    public static void generateTableOtherRows(List<String> rulesLeft, List<String> rulesRight, String[][] table, List<Integer> indexOfLeftElements,int value){
        int counter=0; //index of progress of rules right side
        int position=0;
        String mixed="";
        List<String> isItEqual=new ArrayList<>();
        List<String> firstOne=new ArrayList<>();
        int size=table[0].length/table[0][0].length();

        for(int i=0;i<size;i++){
            for(int j=0; j<indexOfLeftElements.get(i);j++){
                if(components.isNonTerminal(rulesRight.get(counter).charAt(0))) {
                    position=components.getNonTerminalColumn(rulesLeft, rulesRight.get(counter).charAt(0)); //próbáljuk ki csak a bal oldalakkal
                    if(!table[value - 1][i].equals("-")) {
                        mixed+=table[value-1][i]+table[value-1][position];
                    }
                    else {
                        mixed+=table[value-1][position];
                    }
                }
                else {
                    mixed+=table[value-1][i];
                }
                counter++;
            }
            table[value][i]=filterTerminal(mixed);
            mixed="";
        }

        for(int i=0; i<size; i++){
            if(table[value][i].equals(table[value - 1][i])) {
                isItEqual.add("X");
            }
        }

        out.print("H"+value+"    ");
        for(int i=0; i<size;i++){
            out.print("      "+table[value][i]);
        }
        out.println();

        if(isItEqual.size()==5){
            out.print("FIRST1");
            for(int i=0; i<size; i++){
                out.print("      "+table[value][i]);
                firstOne.add(table[value][i]);
            }
            out.println("\n");
            generateSecondTableFirstRow(rulesLeft,rulesRight,firstOne,indexOfLeftElements);
        }
        value++;
        if(isItEqual.size()!=5){
            generateTableOtherRows(rulesLeft,rulesRight,table,indexOfLeftElements,value);
        }

    }

    public static void generateSecondTableFirstRow(List<String>rulesLeft, List<String> rulesRight,List<String> firstOne,List<Integer> indexOfLeftElements){
        out.println("The second table:");
        List<Character> rightElements = components.getTableRightElements(rulesRight);
        for (Character rightElement : rightElements) {
            out.print("\t" + rightElement+"\t ");
        }
        out.println();
        filterOtherTable(rulesLeft,rulesRight,firstOne,indexOfLeftElements);
    }

    public static void filterOtherTable(List<String> rulesLeft, List<String> rulesRight, List<String> firstOne, List<Integer> indexOfLeftElements) {
        int counter=0, counter2=0;
        int position=0,position2=0;
        int size=0;
        String [][]table=new String[11][6];
        int columnNumber=6;
        int rowNumber=11;
        List<Character> leftElements=components.getTableLeftElements(rulesLeft);
        List<Character> rightElements=components.getTableRightElements(rulesRight);
        List<Character> allElements=components.getTableLeftElements(rulesLeft);

        allElements.addAll(rightElements);

        for(int i=0; i< leftElements.size();i++){
            for(int j=0; j<indexOfLeftElements.get(i);j++){
                if(components.isNonTerminal(rulesRight.get(counter).charAt(0))){
                    position=components.getNonTerminalColumn(rulesLeft, rulesRight.get(counter).charAt(0));

                    //if(firstOne.get(position).length()== rightElements.size()-1){
                    if(firstOne.get(position).length()== rightElements.size()-1){
                        for(int k=0; k< leftElements.size();k++){
                            counter2=counter+1;
                            table[i][k]="("+rulesRight.get(counter)+","+position2+")";
                        }
                    }
                    else {
                        position=components.getNonTerminalColumn(rulesLeft,rulesRight.get(counter).charAt(0));
                        size=firstOne.get(position).length();
                        for(int k=0; k<size;k++){
                            position2=components.getTerminalColumn(rightElements,firstOne.get(position).charAt(k));
                            counter2=counter+1;
                            table[i][position2]="("+rulesRight.get(counter)+","+position2+")";
                        }

                    }
                }
                else {
                    position2=components.getTerminalColumn(rightElements,rulesRight.get(counter).charAt(0));
                    counter2=counter+1;
                    table[i][position2]="("+rulesRight.get(counter)+","+counter2+")";

                }
                counter++;
            }
        }


        position= leftElements.size();

        for(int i=0; i<rightElements.size();i++) {
            for(int j=0; j< rightElements.size();j++){
                if(rightElements.get(i)=='#' && rightElements.get(j)=='#') {
                    table[position][j]="accept";
                }
                else if (rightElements.get(i)==rightElements.get(j)) {
                    table[position][j]="pop  ";
                }
            }
            position++;
        }

        for(int i=0; i<rowNumber;i++){
            out.print(allElements.get(i));
            for(int j=0; j<columnNumber;j++){
                out.print("\t"+table[i][j]);
            }
            out.println();
        }

    }


    public static String filterTerminal(String value) { //filter the unique terminals
        StringBuilder newWord= new StringBuilder();
        int counter=-1;
        for(int i=0; i<value.length();i++){
            if(newWord.toString().equals("")){
                newWord.append(value.charAt(i));
            }
            else {
                for (int j=0; j<newWord.length();j++){
                    if(newWord.charAt(j)==value.charAt(i)) {
                        counter++;
                    }
                }
                if(counter==-1) {
                    newWord.append(value.charAt(i));
                }
                counter=-1;
            }
        }
        return newWord.toString();
    }
}
