/**
 * Custom exception class for handling syntax errors during parsing.
 * When thrown, it prints an error message including the row number
 * of the token where the error occurred and terminates the program.
 */
public class SyntaxError extends Exception {
    public Token errorToken;

    /**
     * Constructs a SyntaxError based on the problematic token.
     * Prints an error message to standard output and exits the program.
     *
     * @param token the token where the syntax error was detected
     */
    public SyntaxError(Token token) {
        System.out.println("Syntaxfel på rad " + token.getRow());
        System.exit(0);
    }
}
