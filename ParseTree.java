/**
 * Abstract base class for all parse tree nodes.
 * Every node must implement an evaluate() method that applies
 * its logic using a Leona instance.
 */
abstract class ParseTree {
    abstract public void evaluate(Leona leona);
}

/**
 * Represents a move instruction in the language, such as FORW, BACK, LEFT, or RIGHT.
 * This node contains:
 * - operator: the movement command
 * - num: the number of steps/degrees to apply
 */
class MoveNode extends ParseTree {
    public Token operator;
    public Token num;

    public MoveNode(Token action, Token value) {
        this.operator = action;
        this.num = value;
    }

    /**
     * Evaluates the movement by modifying the Leona object's state accordingly.
     */
    public void evaluate(Leona leona) {
        if (operator.getType() == TokenType.FORW) {
            leona.move(num.getData());
        } else if (operator.getType() == TokenType.BACK) {
            leona.move(-num.getData());
        } else if (operator.getType() == TokenType.LEFT) {
            leona.turnLeft(num.getData());
        } else if (operator.getType() == TokenType.RIGHT) {
            leona.turnRight(num.getData());
        }
    }
}

/**
 * Represents a general-purpose node with left and right subtrees.
 * Used for sequencing multiple statements or expressions.
 */
class RegularNode extends ParseTree {
    public ParseTree left;
    public ParseTree right;

    public RegularNode(ParseTree left, ParseTree right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Evaluates both subtrees. Order is: right first, then left.
     * If both are null, the program exits.
     */
    public void evaluate(Leona leona) {
        if (left == null && right == null) {
            System.exit(1);  // invalid node
        } else {
            if (right != null) right.evaluate(leona);
            if (left != null) left.evaluate(leona);
        }
    }
}

/**
 * Represents a loop construct (REP).
 * The left token holds the number of repetitions,
 * and the right subtree contains the body to repeat.
 */
class RepNode extends ParseTree {
    public ParseTree right;
    public Token left;

    public RepNode(ParseTree right, Token left) {
        this.left = left;
        this.right = right;
    }

    /**
     * Evaluates the right subtree multiple times based on the left token's value.
     */
    public void evaluate(Leona leona) {
        for (int i = 0; i < left.getData(); i++) {
            right.evaluate(leona);
        }
    }
}

/**
 * Represents a single command, such as UP, DOWN, COLOR, or EOF.
 * Optionally stores a color string in `data` for color-related commands.
 */
class SingleNode extends ParseTree {
    public Token node;
    public String data;  // optional data (e.g. color hex)

    public SingleNode(Token node) {
        this.node = node;
    }

    public SingleNode(Token node, String data) {
        this.node = node;
        this.data = data;
    }

    /**
     * Evaluates a simple command. Applies pen state or color changes.
     */
    public void evaluate(Leona leona) {
        if (node.getType() == TokenType.EOF) {
            System.out.print("");
        }
        if (node.getType() == TokenType.UP) {
            leona.up();
        }
        if (node.getType() == TokenType.DOWN) {
            leona.down();
        }
        if (node.getType() == TokenType.HEX) {
            leona.changeColor(data.toUpperCase());
        }
    }
}
