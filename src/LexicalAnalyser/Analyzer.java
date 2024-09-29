package LexicalAnalyser;

import Common.SymbolTable;
import LexicalAnalyser.Constants.DataTypes;
import LexicalAnalyser.Constants.States;
import LexicalAnalyser.Constants.TokenTypes;
import LexicalAnalyser.DFA.DFALL;
import LexicalAnalyser.DFA.Tokens;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Analyzer {

    static SymbolTable symbolTable = new SymbolTable();


    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File("src/Examples/source.zn"));
        DFALL dfa = new DFALL();
        String line;
        while (scanner.hasNextLine()) {
            line = removeComments(scanner.nextLine());
            for (int i = 0; i < line.length(); i++) {
                if (i + 1 == line.length()) {
                    dfa.currentState = States.VARIABLE;
                }
                dfa.addNewState(line.charAt(i));
            }
        }

        for (Tokens token : firstPass(dfa.getTokens())) {
            System.out.println(token.name + " " + token.type);
        }
        secondPass(firstPass(dfa.getTokens()));
        System.out.println();
        for (SymbolTable.Row row: symbolTable.getTable()){
            System.out.println(row.identifier+" "+row.dataType);
        }
    }

    public static String removeComments(String line){
        String toReturn="";
        boolean flag=false;
        States currentState=States.INITIAL;
        States previousState=States.INITIAL;
        for (int i=0; i<line.length(); i++){
             if (!flag){
                toReturn+=line.charAt(i);
                if (line.charAt(i)=='/'){
                    currentState=States.FORWARDSLASH;
                }
                if (line.charAt(i)=='*'){
                    currentState=States.ASTERISK;
                }

                // We have a comment of the form //Content.
                if (currentState==previousState && previousState==States.FORWARDSLASH){
                   toReturn=toReturn.substring(0, toReturn.length()-2);
                   break;
                }
                if (currentState==States.ASTERISK && previousState==States.FORWARDSLASH){
                    flag=true;
                    currentState=States.INITIAL;
                    toReturn=toReturn.substring(0, toReturn.length()-2);
                }

            }
             // We have a comment of the form /* Content */
             else{
                 if (line.charAt(i)=='*'){
                     currentState=States.ASTERISK;
                 }
                 if (line.charAt(i)=='/'){
                     currentState=States.FORWARDSLASH;
                 }
                 if (previousState==States.ASTERISK && currentState==States.FORWARDSLASH){
                     flag=false;

                 }
            }
             previousState=currentState;
             currentState=States.INITIAL;
        }

        return toReturn;
    }


    //first pass is mainly for String data
    public static ArrayList<Tokens> firstPass(ArrayList<Tokens> data) {
        ArrayList<Tokens> toReturn = new ArrayList<>();
        String holder = "";
        //flag=true means we have encountered quotation and everything is being treated as one token
        boolean flag = false;
        for (int i = 0; i < data.size(); i++) {
            if (!flag) {
                if (data.get(i).type != TokenTypes.QUOTATION) {
                    toReturn.add(data.get(i));
                } else {
                    flag = true;
                }
            } else {
                if (data.get(i).type == TokenTypes.QUOTATION) {
                    toReturn.add(new Tokens(holder, TokenTypes.DATA));
                    flag = false;
                } else {
                    holder = holder + data.get(i).name + " ";
                }
            }

        }
        return toReturn;
    }


    // Second Pass is related to type inference and storing the variables in symbol table
    public static void secondPass(ArrayList<Tokens> data) {
        boolean flag = false;
        Tokens temp = null;
        for (Tokens token : data) {
            if (flag) {
                if (token.type == TokenTypes.DATA) {
                    try{
                        Integer.parseInt(token.name);
                        symbolTable.addData(temp.name, DataTypes.INT);
                    }catch (Exception e){
                        symbolTable.addData(temp.name, DataTypes.STRING);
                    }
                }

            }
            if (token.type == TokenTypes.IDENTIFIER) {
                flag = true;
                temp = token;
            }

        }
    }
}