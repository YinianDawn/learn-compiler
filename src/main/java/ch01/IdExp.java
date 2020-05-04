package ch01;

/**
 * @author dawn
 */
public class IdExp extends Exp {
    public String id;

    public IdExp(String id) {
        this.id = id;
    }


    @Override
    public String interpret() {
        return id;
    }
}
