package LexicalAnalyser;

import LexicalAnalyser.Constants.States;
import LexicalAnalyser.DFA.DFALL;
import LexicalAnalyser.DFA.Tokens;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Analyzer {
    public static void main(String[] args) throws FileNotFoundException {


        Scanner scanner = new Scanner(new File("src/Examples/source.zn"));
        DFALL dfa = new DFALL();
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            for (int i = 0; i < line.length(); i++) {
                if (i + 1 == line.length()) {
                    dfa.currentState = States.VARIABLE;
                }
                dfa.addNewState(line.charAt(i));
            }
        }

        for (Tokens token : dfa.getTokens()) {
            System.out.println(token.name + " " + token.type);
        }
    }
}