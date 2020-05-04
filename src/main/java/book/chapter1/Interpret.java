package book.chapter1;

class Interpret {

    private static void interpret(Stm s) { /* you write this part */
        Table t = new Table("null", -1, null);
        interpretStm(s, t);
    }
    private static String getString(ExpList exps, Table t) {
        if (exps instanceof LastExpList) {
            return getString(((LastExpList) exps).head, t);
        }
        if (exps instanceof PairExpList) {
            PairExpList pairExpList = (PairExpList) exps;
            return getString(pairExpList.head, t) + " " + getString(pairExpList.tail, t);
        }
        throw new RuntimeException("can not deal with " + exps);
    }
    private static String getString(Exp exp, Table t) {
        if (exp instanceof EseqExp) {
            EseqExp eseqExp = (EseqExp) exp;
            interpret(eseqExp.stm);
            return getString(eseqExp.exp, t);
        }
        if (exp instanceof IdExp) {
            return  String.valueOf(lockup(t, ((IdExp) exp).id));
        }
        if (exp instanceof NumExp) {
            return String.valueOf(((NumExp) exp).num);
        }
        if (exp instanceof OpExp) {
            OpExp opExp = (OpExp) exp;
            int o1 = Integer.valueOf(getString(opExp.left, t));
            int o2 = Integer.valueOf(getString(opExp.right, t));
            int r;
            switch (opExp.operator) {
                case OpExp.Plus: r = o1 + o2; break;
                case OpExp.Minus: r = o1 - o2; break;
                case OpExp.Times: r = o1 * o2; break;
                case OpExp.Div: r = o1 / o2; break;
                default: throw new RuntimeException("op can not be " + opExp.operator);
            }
            return String.valueOf(r);
        }
        throw new RuntimeException("can not deal with " + exp);
    }

    private static Table interpretStm(Stm stm, Table t) {
        if (stm instanceof PrintStm) {
            System.out.println(getString(((PrintStm) stm).exps, t));
            return t;
        }
        if (stm instanceof CompoundStm) {
            CompoundStm cs = (CompoundStm) stm;
            t = interpretStm(cs.stm1, t);
            t = interpretStm(cs.stm2, t);
            return t;
        }
        if (stm instanceof AssignStm) {
            AssignStm as = (AssignStm) stm;
            t = interpretExp(as.exp, t);
            int r = lockup(t, "op");
            return update(t, as.id, r);
        }
        throw new RuntimeException("can not deal with " + stm);
    }
    private static Table interpretExp(Exp exp, Table t) {
        if (exp instanceof EseqExp) {
            EseqExp eseqExp = (EseqExp) exp;
            interpretStm(eseqExp.stm, t);
            return interpretExp(eseqExp.exp, t);
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
            t = interpretExp(opExp.left, t);
            int o1 = lockup(t, "op");
            t = interpretExp(opExp.right, t);
            int o2 = lockup(t, "op");
            int r;
            switch (opExp.operator) {
                case OpExp.Plus: r = o1 + o2; break;
                case OpExp.Minus: r = o1 - o2; break;
                case OpExp.Times: r = o1 * o2; break;
                case OpExp.Div: r = o1 / o2; break;
                default: throw new RuntimeException("op can not be " + opExp.operator);
            }
            return update(t, "op", r);
        }
        throw new RuntimeException("can not deal with " + exp);
    }

    private static class Table {
        private String id;
        private int value;
        private Table tail;

        Table(String id, int value, Table tail) {
            this.id = id;
            this.value = value;
            this.tail = tail;
        }
    }

    private static Table update(Table t, String id, int value) {
        return new Table(id, value, t);
    }
    private static int lockup(Table t, String id) {
        if (t.id.equals(id)) {
            return t.value;
        }
        if (null != t.tail) {
            return lockup(t.tail, id);
        }
        return -1;
    }

    private static int maxArgs(Stm s) { /* you write this part */
        return getMaxLength(s);
    }

    private static int getMaxLength(Stm stm) {
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

    private static int getMaxLength(ExpList exps) {
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

    private static int getMaxLength(Exp exp) {
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

    public static void main(String[] args) {
        System.out.println(maxArgs(Program.program));
        interpret(Program.program);
    }
}
