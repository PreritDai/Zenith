package LexicalAnalyser;

import LexicalAnalyser.Constants.States;
import LexicalAnalyser.Constants.TokenTypes;
import LexicalAnalyser.DFA.DFALL;
import LexicalAnalyser.DFA.Tokens;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Analyzer {
    public static void main(String[] args) throws FileNotFoundException {


        Scanner scanner = new Scanner(new File("src/Examples/source.zn"));
        DFALL dfa = new DFALL();
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            for (int i = 0; i <line.length(); i++) {
                if (i + 1 == line.length()) {
                    dfa.currentState = States.VARIABLE;
                }
                dfa.addNewState(line.charAt(i));
            }
        }

        for (Tokens token : firstPass(dfa.getTokens())) {
            System.out.println(token.name + " " + token.type);
        }
    }

    public static ArrayList<Tokens> firstPass(ArrayList<Tokens> data){
        ArrayList<Tokens> toReturn=new ArrayList<>();
        String holder="";
        //flag=true means we have encountered quotation and everything is being treated as one token
        boolean flag=false;
        for (int i=0; i<data.size(); i++){
            if (!flag){
                if (data.get(i).type!=TokenTypes.QUOTATION){
                    toReturn.add(data.get(i));
                }else{
                    flag=true;
                }
            }else{
                if (data.get(i).type==TokenTypes.QUOTATION){
                    toReturn.add(new Tokens(holder, TokenTypes.DATA));
                    flag=false;
                }else{
                    holder=holder+data.get(i).name+" ";
                }
            }

        }
        return toReturn;
    }


}