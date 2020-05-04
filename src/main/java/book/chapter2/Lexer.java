package book.chapter2;

interface Lexer {
    public java_cup.runtime.Symbol nextToken() throws java.io.IOException;
}
