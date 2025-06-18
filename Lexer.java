import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * The Lexer class is responsible for reading and tokenizing input text
 * into a sequence of Tokens. It supports basic drawing commands and control
 * structures for a toy language similar in behavior to turtle graphics.
 */
public class Lexer {
    public String input;            // raw input as string (unused in this version)
    public List<Token> tokens;     // list of all identified tokens
    private int currentToken;      // index of the current token (used during parsing)

    /**
     * Reads the input stream and returns its contents as a String.
     * Handles skipping lines starting with '%'.
     */
    private static String readInput(InputStream f) throws java.io.IOException {
        Reader stdin = new InputStreamReader(f);
        StringBuilder buf = new StringBuilder();
        char[] input = new char[1024];
        int read;

        while ((read = stdin.read(input)) != -1) {
            if (read == '%') {
                // skip comments
                while ((read = stdin.read()) != '\n');
            }
            buf.append(input, 0, read);
        }

        return buf.toString();
    }

    /**
     * Constructor: tokenizes the input stream and stores results in the tokens list.
     * Also handles errors and row tracking.
     */
    public Lexer(InputStream in) throws java.io.IOException {
        tokens = new ArrayList<>();
        int inputPos = 0;
        int currentRow = 1;
        currentToken = 0;

        String x = Lexer.readInput(in);
        String input = x.replaceAll("%(.|\t)*\n", "\n"); // remove comments

        // Regular expression for all supported token types
        Pattern tokenPattern = Pattern.compile(
            "(?i)FORW( |\t|\n)|(?i)BACK( |\t|\n)|(?i)LEFT( |\t|\n)|(?i)RIGHT( |\t|\n)|" +
            "(?i)DOWN|(?i)UP|(?i)COLOR( |\t|\n)|" +
            "(?i)REP[ |\t|\n]+\n?[1-9][0-9]*[ |\t|\n]|\\.|\"|" +
            "[1-9][0-9]*|#(?i)[A-Za-z0-9]{6}|\n| +|\t+"
        );

        Matcher m = tokenPattern.matcher(input);

        // Iterate through matches and convert them into tokens
        while (m.find()) {
            if (m.start() != inputPos) {
                // Unrecognized characters between matches
                tokens.add(new Token(TokenType.ERROR, currentRow));
            }

            String match = m.group();

            if (match.matches("\n")) {
                currentRow++;
            } else if (match.matches("(?i)FORW( |\t|\n)")) {
                tokens.add(new Token(TokenType.FORW, currentRow));
                if (match.endsWith("\n")) currentRow++;
            } else if (match.matches("(?i)BACK( |\t|\n)")) {
                tokens.add(new Token(TokenType.BACK, currentRow));
                if (match.endsWith("\n")) currentRow++;
            } else if (match.matches("(?i)LEFT( |\t|\n)")) {
                tokens.add(new Token(TokenType.LEFT, currentRow));
                if (match.endsWith("\n")) currentRow++;
            } else if (match.matches("(?i)RIGHT( |\t|\n)")) {
                tokens.add(new Token(TokenType.RIGHT, currentRow));
                if (match.endsWith("\n")) currentRow++;
            } else if (match.matches("(?i)DOWN")) {
                tokens.add(new Token(TokenType.DOWN, currentRow));
                if (match.endsWith("\n")) currentRow++;
            } else if (match.matches("(?i)UP")) {
                tokens.add(new Token(TokenType.UP, currentRow));
                if (match.endsWith("\n")) currentRow++;
            } else if (match.matches("(?i)COLOR( |\t|\n)")) {
                tokens.add(new Token(TokenType.COLOR, currentRow));
                if (match.endsWith("\n")) currentRow++;
            } else if (match.matches("(?i)REP[ |\t|\n]+\n?[1-9][0-9]*[ |\t|\n]+")) {
                tokens.add(new Token(TokenType.REP, currentRow));

                // Adjust line count if REP includes newlines
                Matcher m2 = Pattern.compile("\n").matcher(match);
                while (m2.find()) currentRow++;

                // Extract the numeric part of the REP command
                if (match.matches("(.|\n|\t)*[0-9]+(.|\n|\t)*")) {
                    String num = match.replaceAll("[^0-9]+", "");
                    tokens.add(new Token(TokenType.DECIMAL, Integer.parseInt(num), currentRow));
                }
            } else if (match.equals(".")) {
                tokens.add(new Token(TokenType.PERIOD, currentRow));
            } else if (match.equals("\"")) {
                tokens.add(new Token(TokenType.QUOTE, currentRow));
            } else if (Character.isDigit(match.charAt(0))) {
                tokens.add(new Token(TokenType.DECIMAL, Integer.parseInt(match), currentRow));
            } else if (match.charAt(0) == '#') {
                tokens.add(new Token(TokenType.HEX, match, currentRow));
            }

            inputPos = m.end(); // advance position
        }

        // Handle any remaining characters that weren't matched
        if (inputPos != input.length()) {
            tokens.add(new Token(TokenType.ERROR, currentRow));
        }

        // Append EOF token to mark end of input
        if (tokens.size() != 0) {
            int prevRow = tokens.get(tokens.size() - 1).getRow();
            tokens.add(new Token(TokenType.EOF, prevRow));
        } else {
            tokens.add(new Token(TokenType.EOF, 0));
        }
    }

    /**
     * Returns the current token without advancing the token pointer.
     */
    public Token peekToken() {
        return tokens.get(currentToken);
    }

    /**
     * Returns the current token and moves to the next one.
     */
    public Token nextToken() {
        Token res = peekToken();
        currentToken++;
        return res;
    }

    /**
     * Checks whether there are more tokens to process.
     */
    public boolean hasMoreTokens() {
        return currentToken < tokens.size();
    }
}
