package LexicalAnalyser.DFA;


import LexicalAnalyser.Constants.TokenTypes;

public class Tokens {
    String name;
    TokenTypes type;

    Tokens(String name, TokenTypes type){
        this.name=name;
        this.type=type;
    }

}
