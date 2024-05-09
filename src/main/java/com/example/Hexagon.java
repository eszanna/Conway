package com.example;

public class Hexagon extends Shape {
    private int q;
    private int r;

    public Hexagon(int q, int r, boolean state) {
        super(state);
        this.q = q;
        this.r = r;
    }

    public int getQ(){
        return q;
    }

    public int getR(){
        return r;
    }
}