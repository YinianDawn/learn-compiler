package ch01;

/**
 * @author dawn
 */
public class PairExpList extends ExpList {
    public Exp head;
    public ExpList tail;

    public PairExpList(Exp head, ExpList tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public String interpret() {
        return head.interpret() + ", " + tail.interpret();
    }
}
