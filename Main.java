import java.io.IOException;

/*
 * Grammar definition for the toy programming language:
 *
 * <EXPR> := <STMT> <EXPR> | <STMT> e
 * <STMT> := REP NUM <LOOP> | UP PERIOD | DOWN PERIOD | COLOR HEX PERIOD | <MOVE> NUM PERIOD
 * <LOOP> := QUOTE <EXPR> QUOTE | <STMT>
 * <MOVE> := FORW | BACK | LEFT | RIGHT
 *
 * The grammar supports repetition, pen control, color changes, and movement commands.
 */

// Entry point for the interpreter
public class Main {
    public static void main(String[] args) throws IOException, SyntaxError {
        // Initialize the lexer with input from standard input (usually piped or redirected)
        Lexer lexer = new Lexer(System.in);

        // Create a drawing agent (Leona) with default starting position, angle, and color
        Leona leona = new Leona(0, 0, 0, true, "#0000FF");

        // Create a parser using the lexer
        Parser parser = new Parser(lexer);

        // Parse the input into a parse tree
        ParseTree result = parser.parse();

        // Evaluate the parsed tree using the drawing agent
        result.evaluate(leona);
    }
}
