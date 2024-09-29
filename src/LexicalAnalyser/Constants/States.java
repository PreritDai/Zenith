package LexicalAnalyser.Constants;

public enum States {
    INITIAL,
    VARIABLE,
    STARTINGQUOTATION,
    ENDINGQUOTATION,
    FORWARDSLASH, // It means '/'
    BACKWARDSLASH, // It means '\'
    ASTERISK
}
