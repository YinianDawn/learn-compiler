package ch01;

/**
 * @author dawn
 */
public class CompoundStm extends Stm {
    public Stm stm1, stm2;

    public CompoundStm(Stm stm1, Stm stm2) {
        this.stm1 = stm1;
        this.stm2 = stm2;
    }

    @Override
    public String interpret() {
        return stm1.interpret() + " " + stm2.interpret();
    }
}
