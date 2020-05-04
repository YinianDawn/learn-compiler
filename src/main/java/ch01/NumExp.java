package ch01;

/**
 * @author dawn
 */
public class NumExp extends Exp {
    public int num;

    public NumExp(int num) {
        this.num = num;
    }

    @Override
    public String interpret() {
        return num + "";
    }
}
