package com.example;

public class Square extends Shape {
    private int i;
    private int j;

    public Square(int i, int j, boolean state) {
        super(state);
        this.i = i;
        this.j = j;
    }
}
