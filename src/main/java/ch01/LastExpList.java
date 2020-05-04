package ch01;

/**
 * @author dawn
 */
public class LastExpList extends ExpList {
    public Exp head;

    public LastExpList(Exp head) {
        this.head = head;
    }

    @Override
    public String interpret() {
        return head.interpret();
    }
}
