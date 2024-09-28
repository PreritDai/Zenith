package LexicalAnalyser.DFA;


import LexicalAnalyser.Constants.TokenTypes;

public class Tokens {
    public String name;
    public TokenTypes type;

    public Tokens(String name, TokenTypes type){
        this.name=name;
        this.type=type;
    }

}
