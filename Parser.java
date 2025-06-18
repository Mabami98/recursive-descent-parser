/**
 * The Parser class implements a recursive descent parser for a small
 * custom programming language (as defined by the grammar in Main.java).
 * It processes a stream of tokens produced by the Lexer and builds a
 * parse tree that represents the structure of the program.
 */
public class Parser {
    public Lexer lexer;  // the token stream source

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Entry point for parsing.
     * Starts by parsing an <EXPR> and verifies that all tokens have been consumed.
     */
    public ParseTree parse() throws SyntaxError {
        ParseTree result = Expr();
        if (lexer.peekToken().getType() != TokenType.EOF) {
            throw new SyntaxError(lexer.peekToken());
        }
        return result;
    }

    /**
     * Parses the <EXPR> non-terminal.
     * <EXPR> ::= <STMT> <EXPR> | <STMT> e
     */
    public ParseTree Expr() throws SyntaxError {
        ParseTree result;
        ParseTree left;

        // base case: end of input
        if (lexer.peekToken().getType() == TokenType.EOF) {
            return new SingleNode(lexer.peekToken());
        }

        // parse a statement
        ParseTree right = Stmt();

        // check what follows to decide between single or compound expression
        if (lexer.peekToken().getType() == TokenType.EOF) {
            result = new RegularNode(new SingleNode(lexer.peekToken()), right);
        } else if (lexer.peekToken().getType() == TokenType.QUOTE) {
            result = new RegularNode(null, right);
        } else {
            left = Expr();  // recurse on the left-hand expression
            result = new RegularNode(left, right);
        }

        return result;
    }

    /**
     * Parses the <STMT> non-terminal.
     * Handles REP loops, pen control (UP/DOWN), color setting, and movement.
     */
    public ParseTree Stmt() throws SyntaxError {
        ParseTree result;
        Token next = lexer.peekToken();

        if (next.getType() == TokenType.REP) {
            lexer.nextToken();  // consume REP
            Token left = lexer.nextToken();  // should be a number
            ParseTree right = Loop();  // parse the loop body
            result = new RepNode(right, left);

        } else if (next.getType() == TokenType.UP || next.getType() == TokenType.DOWN) {
            // UP or DOWN statements
            Token command = lexer.nextToken();
            result = new SingleNode(command);

            if (lexer.peekToken().getType() != TokenType.PERIOD) {
                throw new SyntaxError(command);
            }
            lexer.nextToken();  // consume PERIOD

        } else if (next.getType() == TokenType.COLOR) {
            // COLOR command followed by HEX value and PERIOD
            lexer.nextToken();  // consume COLOR
            if (lexer.peekToken().getType() == TokenType.HEX) {
                result = new SingleNode(lexer.peekToken(), lexer.peekToken().getColor());
                lexer.nextToken();  // consume HEX

                if (lexer.peekToken().getType() != TokenType.PERIOD) {
                    throw new SyntaxError(lexer.peekToken());
                }
                lexer.nextToken();  // consume PERIOD
            } else {
                throw new SyntaxError(lexer.peekToken());
            }

        } else {
            // movement commands
            result = Move();
        }

        return result;
    }

    /**
     * Parses the <LOOP> non-terminal.
     * <LOOP> ::= " <EXPR> " | <STMT>
     */
    public ParseTree Loop() throws SyntaxError {
        ParseTree result;

        if (lexer.peekToken().getType() == TokenType.QUOTE) {
            lexer.nextToken();  // consume opening quote
            result = Expr();    // parse the inner expression

            if (lexer.peekToken().getType() != TokenType.QUOTE) {
                throw new SyntaxError(lexer.peekToken());
            }
            lexer.nextToken();  // consume closing quote
        } else {
            result = Stmt();  // single statement loop
        }

        return result;
    }

    /**
     * Parses the <MOVE> non-terminal.
     * <MOVE> ::= FORW | BACK | LEFT | RIGHT followed by a number and period
     */
    public ParseTree Move() throws SyntaxError {
        Token action;
        Token value;
        ParseTree result;

        // check for one of the movement commands
        if (lexer.peekToken().getType() == TokenType.FORW ||
            lexer.peekToken().getType() == TokenType.BACK ||
            lexer.peekToken().getType() == TokenType.LEFT ||
            lexer.peekToken().getType() == TokenType.RIGHT) {

            action = lexer.nextToken();  // consume movement keyword

            if (lexer.peekToken().getType() == TokenType.DECIMAL) {
                value = lexer.nextToken();  // consume numeric value
            } else {
                throw new SyntaxError(lexer.peekToken());
            }

            if (lexer.peekToken().getType() != TokenType.PERIOD) {
                throw new SyntaxError(lexer.peekToken());
            }

            lexer.nextToken();  // consume PERIOD
            result = new MoveNode(action, value);
            return result;

        } else {
            throw new SyntaxError(lexer.peekToken());
        }
    }
}
