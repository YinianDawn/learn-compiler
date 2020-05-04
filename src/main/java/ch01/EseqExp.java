package ch01;

/**
 * @author dawn
 */
public class EseqExp extends Exp {
    public Stm stm;
    public Exp exp;

    public EseqExp(Stm stm, Exp exp) {
        this.stm = stm;
        this.exp = exp;
    }


    @Override
    public String interpret() {
        return "(" + stm.interpret() + " " + exp.interpret() + ")";
    }
}
