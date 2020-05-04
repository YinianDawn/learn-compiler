package ch01;

/**
 * @author dawn
 */
public class OpExp extends Exp {
    public Exp left, right;
    public int op;
    public final static int ADD = 1, SUB = 2, MUL = 3, DIV = 4;

    public OpExp(Exp left, int op, Exp right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public String interpret() {
        String op = "";
        switch (this.op) {
            case ADD: op = "+"; break;
            case SUB: op = "-"; break;
            case MUL: op = "*"; break;
            case DIV: op = "/"; break;
            default:
        }
        return (left instanceof OpExp ? "(" : "")
                + left.interpret()
                + (left instanceof OpExp ? ")" : "")
                + " " + op + " "
                + (right instanceof OpExp ? "(" : "")
                + right.interpret()
                + (right instanceof OpExp ? ")" : "");
    }
}
