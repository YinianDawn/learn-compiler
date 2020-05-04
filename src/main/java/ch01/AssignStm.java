package ch01;

/**
 * @author dawn
 */
public class AssignStm extends Stm {
    public String id;
    public Exp exp;

    public AssignStm(String id, Exp exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String interpret() {
        return id + " = " + exp.interpret() + ";";
    }
}
