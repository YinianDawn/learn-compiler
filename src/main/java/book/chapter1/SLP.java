package book.chapter1;

interface Stm {
}

class CompoundStm implements Stm {
    Stm stm1, stm2;

    CompoundStm(Stm s1, Stm s2) {
        stm1 = s1;
        stm2 = s2;
    }
}

class AssignStm implements Stm {
    String id;
    Exp exp;

    AssignStm(String i, Exp e) {
        id = i;
        exp = e;
    }
}

class PrintStm implements Stm {
    ExpList exps;

    PrintStm(ExpList e) {
        exps = e;
    }
}

interface Exp {
}

class IdExp implements Exp {
    String id;

    IdExp(String i) {
        id = i;
    }
}

class NumExp implements Exp {
    int num;

    NumExp(int n) {
        num = n;
    }
}

class OpExp implements Exp {
    Exp left, right;
    int operator;
    final static int Plus = 1, Minus = 2, Times = 3, Div = 4;

    OpExp(Exp l, int o, Exp r) {
        left = l;
        operator = o;
        right = r;
    }
}

class EseqExp implements Exp {
    Stm stm;
    Exp exp;

    EseqExp(Stm s, Exp e) {
        stm = s;
        exp = e;
    }
}

interface ExpList {
}

class PairExpList implements ExpList {
    Exp head;
    ExpList tail;

    public PairExpList(Exp h, ExpList t) {
        head = h;
        tail = t;
    }
}

class LastExpList implements ExpList {
    Exp head;

    public LastExpList(Exp h) {
        head = h;
    }
}
