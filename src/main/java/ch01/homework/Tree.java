package ch01.homework;

import java.util.Arrays;
import java.util.List;

/**
 * @author dawn
 */
public class Tree {
    public Tree left;
    public String key;
    public String value;
    public Tree right;

    public Tree(Tree left, String key, String value, Tree right) {
        this.left = left;
        this.key = key;
        this.right = right;
        this.value = value;
    }

    public static Tree insert(String key, String value, Tree tree) {
        if (null == tree) { return new Tree(null, key, value, null); }
        else if (key.compareTo(tree.key) < 0) {
            return new Tree(insert(key,  value,tree.left), tree.key,  value,tree.right);
        }
        else if (key.compareTo(tree.key) > 0) {
            return new Tree(tree.left, tree.key,  value,insert(key, value, tree.right));
        } else {
            return new Tree(tree.left, key,  value, tree.right);
        }
    }

    public static String lookup(Tree tree, String key) {
        return null == tree ?
                null
                :
                (tree.key.equals(key) ?
                        tree.value
                        :
                        (null != lookup(tree.left, key) ? lookup(tree.left, key) : lookup(tree.right, key)));
    }

    public static void main(String[] args) {
        List<String> s = Arrays.asList("t", "s", "p", "i", "p", "f", "b", "s", "t");
        Tree tree = null;
        for (String ss : s) {
            tree = insert(ss, ss, tree);
        }
        System.out.println(1);
    }

}
