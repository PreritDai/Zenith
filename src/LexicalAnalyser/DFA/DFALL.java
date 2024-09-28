package LexicalAnalyser.DFA;

import java.util.ArrayList;
import java.util.HashMap;

import LexicalAnalyser.Constants.TokenTypes;
import LexicalAnalyser.Constants.States;


/*
    DFALL=Deterministic Finite AutoMata Linked List
    It is an implementation of Deterministic Finite Automata Model through LL
 */

public class DFALL {

    //Basic concept of Linked List
    State head = null;
    State current = null;
    static State state = null;

    //There are three states, two quotations and one initial. In quotations state all text will be considered one token
    public static States currentState = States.INITIAL;


    /*
    extraTokens stores intermediate Token
    for example id x=10
    while this line is being tokenized lets say our DFA reaches a point where it is about to add x into a list of tokens
    if I add = simultaneously in that list
    my = will appear before x
    which might be conflicting so a new variable is created to store such intermediate tokens which cannot be stored currently
     */
    Tokens extraTokens = null;

    static int flag = 1;//flag=1 means that the current part we are working on is not a token

    /*
    Stores the list of special symbols. Whenever DFA encounters any special symbol, it treats everything it has encountered till now
    as a token
     */
    public static ArrayList<Character> specialSymbols = new ArrayList<>();

    /*
    It has been initialised to reduce manual work to writing if else statement to identify the type of token
     */
    public static HashMap<String, TokenTypes> tokenSymbol = new HashMap<>();


    /*
    It stores the list of tokens
     */
    private static final ArrayList<Tokens> tokens = new ArrayList<>();

    public DFALL() {
        specialSymbols.add(' ');
        specialSymbols.add('<');
        specialSymbols.add('>');
        specialSymbols.add('=');
        specialSymbols.add('*');
        specialSymbols.add('"');
        specialSymbols.add('(');
        specialSymbols.add(')');

        tokenSymbol.put("id", TokenTypes.ID);
        tokenSymbol.put("while", TokenTypes.WHILE);
        tokenSymbol.put("=", TokenTypes.ASSIGNMENT);
        tokenSymbol.put("+", TokenTypes.ADDITION);
        tokenSymbol.put("-", TokenTypes.SUBTRACTION);
        tokenSymbol.put("(", TokenTypes.LARPEN);
        tokenSymbol.put(")", TokenTypes.PARPEN);
        tokenSymbol.put("if", TokenTypes.IF);

    }

    public static class State {
        State next_state;
        char data;

        State(char data) {
            this.data = data;
            this.next_state = null;
            if (specialSymbols.contains(data)) {
                flag = 0;
            }

        }
    }

    public void addNewState(char data) {
        if (head == null) {
            state = new State(data);
            head = state;
            current = head;
        } else {
            state = new State(data);
            current.next_state = state;
            current = state;
        }

        /*
        Checking if flag is equal to zero, if it is equal to zero, we consider it as a new token.
        Flag will be set equal to zero whenever a special symbol is encountered such as space, =, etc.
        for example
        id name = 10
        here after we have id we encounter a space, and so we have a new token called as id
         */
        if (flag == 0) {
            String rawToken;
            //We donot require the last word in case of a space
            if (data == ' ') {
                rawToken = getLL(false);
            } else if (data == '=') {
                rawToken = getLL(false);
                extraTokens = new Tokens("=", TokenTypes.ASSIGNMENT);
            } else if (data == '"') {
                rawToken = getLL(false);
                extraTokens = new Tokens(String.valueOf('"'), TokenTypes.QUOTATION);
            } else {
                rawToken = getLL(true);
            }

            //Clearing the old linked list
            head = null;
            current = null;
            Tokens finalToken;


            //If it contains it is not a variable
            //It is a variable
            finalToken = new Tokens(rawToken, tokenSymbol.getOrDefault(rawToken, TokenTypes.IDENTIFIER));
            if (!rawToken.isEmpty()) {
                tokens.add(finalToken);
            }

            if (extraTokens != null) {
                tokens.add(extraTokens);
                extraTokens = null;
            }
            flag = 1;
        }
        /*
        The current state can only be set to variable by the Main class
        If it is set to the state variable , we will now force assign the token as the value of an Variable(Data)
        Its data type in the symbol table will be looked later
         */
        if (currentState == States.VARIABLE) {
            currentState = States.INITIAL;
            String rawToken = getLL(true);
            if (!rawToken.isEmpty()) {
                Tokens finalToken = new Tokens(rawToken, TokenTypes.DATA);
                tokens.add(finalToken);
                head = null;
                current = null;
            }
        }
    }


    /*
    When the flag is set to true, the last value of the list is also returned
    but if the flag is set to false the last value of the list is not returned
    for example
    if flag=true and data=int" it will return int"
    if flag=false and data=int" it will return int
     */
    public String getLL(boolean flag) {
        current = head;
        StringBuilder data = new StringBuilder();
        if (flag) {
            while (current != null) {
                data.append(current.data);
                current = current.next_state;
            }
        } else {
            while (current.next_state != null) {
                data.append(current.data);
                current = current.next_state;
            }
        }
        return data.toString();
    }

    public ArrayList<Tokens> getTokens() {
        return tokens;
    }
}
