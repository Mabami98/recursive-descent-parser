/**
 * Enumeration of all possible token types used by the lexer and parser.
 * These correspond to keywords, symbols, literals, and special tokens
 * like ERROR and EOF.
 */
enum TokenType {
    FORW, BACK, LEFT, RIGHT,
    DOWN, UP, COLOR, REP,
    PERIOD, QUOTE, DECIMAL, HEX,
    ERROR, EOF
}

/**
 * Class to represent a token produced by the lexer.
 * A token has a type, a line number (row), and optionally a numeric or color value.
 */
public class Token {
    private TokenType type;  // the category of the token
    private int data;        // used for numeric values (e.g. movement steps)
    private int row;         // the row number in the input source
    private String color;    // used for color tokens (hex format)

    /**
     * Constructor for tokens with no associated value (e.g., keywords).
     */
    public Token(TokenType type, int row) {
        this.type = type;
        this.data = 0;
        this.row = row;
    }

    /**
     * Constructor for tokens with numeric data (e.g., REP count, step size).
     */
    public Token(TokenType type, int data, int row) {
        this.type = type;
        this.data = data;
        this.row = row;
    }

    /**
     * Constructor for tokens that represent a color value.
     */
    public Token(TokenType type, String color, int row) {
        this.type = type;
        this.color = color;
        this.row = row;
    }

    // Returns the token type (e.g., FORW, DECIMAL, COLOR)
    public TokenType getType() {
        return type;
    }

    // Returns the stored color string (if applicable)
    public String getColor() {
        return color;
    }

    // Returns the stored numeric data (if applicable)
    public int getData() {
        return data;
    }

    // Returns the row number in the original source where the token appears
    public int getRow() {
        return row;
    }
}
