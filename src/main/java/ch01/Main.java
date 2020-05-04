package ch01;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dawn
 */
public class Main {
    public static void main(String[] args) {
        Stm prog = new CompoundStm(
                new AssignStm(
                        "a",
                        new OpExp(
                                new NumExp(5),
                                OpExp.ADD,
                                new NumExp(3)
                        )
                ),
                new CompoundStm(
                        new AssignStm("b",
                                new EseqExp(
                                        new PrintStm(
                                                new PairExpList(
                                                        new IdExp("a"),
                                                        new LastExpList(
                                                                new OpExp(
                                                                        new IdExp("a"),
                                                                        OpExp.SUB,
                                                                        new NumExp(1))
                                                        )
                                                )
                                        ),
                                        new OpExp(
                                                new NumExp(10),
                                                OpExp.MUL,
                                                new IdExp("a")
                                        )
                                )
                        ),
                        new PrintStm(
                                new LastExpList(
                                        new IdExp("b")
                                )
                        )
                )
        );
        System.out.println(maxArgs(prog));
        System.out.println(prog.interpret());
        interp(prog);
        interp2(prog);
    }

    public static int maxArgs(Stm stm) {
        return getMaxLength(stm);
    }

    public static int getMaxLength(Stm stm) {
        if (stm instanceof PrintStm) {
            return getMaxLength(((PrintStm) stm).exps);
        }
        if (stm instanceof CompoundStm) {
            CompoundStm cs = (CompoundStm) stm;
            int length1 = maxArgs(cs.stm1);
            int length2 = maxArgs(cs.stm2);
            return length1 < length2 ? length2 : length1;
        }
        if (stm instanceof AssignStm) {
            AssignStm as = (AssignStm) stm;
            return getMaxLength(as.exp);
        }
        return 0;
    }

    public static int getMaxLength(ExpList exps) {
        if (exps instanceof LastExpList) {
            int l = getMaxLength(((LastExpList) exps).head);
            return 1 < l ? l : 1;
        }
        if (exps instanceof PairExpList) {
            PairExpList pairExpList = (PairExpList) exps;
            return getMaxLength(pairExpList.head) + getMaxLength(pairExpList.tail);
        }
        return 0;
    }

    public static int getMaxLength(Exp exp) {
        if (exp instanceof EseqExp) {
            EseqExp eseqExp = (EseqExp) exp;
            int l1 = getMaxLength(eseqExp.stm);
            int l2 = getMaxLength(eseqExp.exp);
            return l1 < l2 ? l2 : l1;
        }
        if (exp instanceof IdExp) {
            return 1;
        }
        if (exp instanceof NumExp) {
            return 1;
        }
        if (exp instanceof OpExp) {
            return 1;
        }
        return 0;
    }


    private static Map<String, String> var = new HashMap<>();

    public static void interp(Stm stm) {
        if (stm instanceof PrintStm) {
            System.out.println(getString(((PrintStm) stm).exps));
            return;
        }
        if (stm instanceof CompoundStm) {
            CompoundStm cs = (CompoundStm) stm;
            interp(cs.stm1);
            interp(cs.stm2);
            return;
        }
        if (stm instanceof AssignStm) {
            AssignStm as = (AssignStm) stm;
            String r = getString(as.exp);
            var.put(as.id, r);
        }
    }

    public static String getString(ExpList exps) {
        if (exps instanceof LastExpList) {
            return getString(((LastExpList) exps).head);
        }
        if (exps instanceof PairExpList) {
            PairExpList pairExpList = (PairExpList) exps;
            return getString(pairExpList.head) + " " + getString(pairExpList.tail);
        }
        throw new RuntimeException("can not deal with " + exps);
    }

    public static String getString(Exp exp) {
        if (exp instanceof EseqExp) {
            EseqExp eseqExp = (EseqExp) exp;
            interp(eseqExp.stm);
            return getString(eseqExp.exp);
        }
        if (exp instanceof IdExp) {
            return var.get(((IdExp) exp).id);
        }
        if (exp instanceof NumExp) {
            return String.valueOf(((NumExp) exp).num);
        }
        if (exp instanceof OpExp) {
            OpExp opExp = (OpExp) exp;
            int o1 = Integer.valueOf(getString(opExp.left));
            int o2 = Integer.valueOf(getString(opExp.right));
            int r;
            switch (opExp.op) {
                case OpExp.ADD: r = o1 + o2; break;
                case OpExp.SUB: r = o1 - o2; break;
                case OpExp.MUL: r = o1 * o2; break;
                case OpExp.DIV: r = o1 / o2; break;
                default: throw new RuntimeException("op can not be " + opExp.op);
            }
            return String.valueOf(r);
        }
        throw new RuntimeException("can not deal with " + exp);
    }

    public static void interp2(Stm stm) {
        Table t = new Table("null", -1, null);
        interpStm(stm, t);
    }

    public static Table interpStm(Stm stm, Table t) {
        if (stm instanceof PrintStm) {
            System.out.println(getString(((PrintStm) stm).exps));
            return t;
        }
        if (stm instanceof CompoundStm) {
            CompoundStm cs = (CompoundStm) stm;
            t = interpStm(cs.stm1, t);
            t = interpStm(cs.stm2, t);
            return t;
        }
        if (stm instanceof AssignStm) {
            AssignStm as = (AssignStm) stm;
            t = interpExp(as.exp, t);
            int r = lockUp(t, "op");
            return update(t, as.id, r);
        }
        throw new RuntimeException("can not deal with " + stm);
    }
    public static Table interpExp(Exp exp, Table t) {
        if (exp instanceof EseqExp) {
            EseqExp eseqExp = (EseqExp) exp;
            interpStm(eseqExp.stm, t);
            return interpExp(eseqExp.exp, t);
        }
        if (exp instanceof IdExp) {
            return t;
        }
        if (exp instanceof NumExp) {
            NumExp numExp = (NumExp) exp;
            return update(t, "op", numExp.num);
        }
        if (exp instanceof OpExp) {
            OpExp opExp = (OpExp) exp;
            t = interpExp(opExp.left, t);
            int o1 = lockUp(t, "op");
            t = interpExp(opExp.right, t);
            int o2 = lockUp(t, "op");
            int r;
            switch (opExp.op) {
                case OpExp.ADD: r = o1 + o2; break;
                case OpExp.SUB: r = o1 - o2; break;
                case OpExp.MUL: r = o1 * o2; break;
                case OpExp.DIV: r = o1 / o2; break;
                default: throw new RuntimeException("op can not be " + opExp.op);
            }
            return update(t, "op", r);
        }
        throw new RuntimeException("can not deal with " + exp);
    }

    public static Table interpExpList(ExpList exps, Table t) {
        if (exps instanceof LastExpList) {
            return interpExp(((LastExpList) exps).head,  t);
        }
        if (exps instanceof PairExpList) {
            PairExpList pairExpList = (PairExpList) exps;
            t = interpExp(pairExpList.head, t);
            t = interpExpList(pairExpList.tail, t);
            return t;
        }
        throw new RuntimeException("can not deal with " + exps);
    }
    private static class Table {
        private String id;
        private int value;
        private Table tail;

        public Table(String id, int value, Table tail) {
            this.id = id;
            this.value = value;
            this.tail = tail;
        }
    }

    public static Table update(Table t, String id, int value) {
        return new Table(id, value, t);
    }
    public static int lockUp(Table t, String id) {
        if (t.id.equals(id)) {
            return t.value;
        }
        if (null != t.tail) {
            return lockUp(t.tail, id);
        }
        return -1;
    }
}
