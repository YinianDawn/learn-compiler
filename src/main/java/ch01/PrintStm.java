package ch01;

/**
 * @author dawn
 */
public class PrintStm extends Stm {
    public ExpList exps;

    public PrintStm(ExpList exps) {
        this.exps = exps;
    }

    @Override
    public String interpret() {
        return "print(" + exps.interpret() + ");";
    }
}
