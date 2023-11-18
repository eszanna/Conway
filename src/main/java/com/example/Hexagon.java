package com.example;

import java.io.Serializable;

class Hexagon implements Serializable{
    private int q;
    private int r;
    private boolean state; //alive or dead

    public Hexagon(int q, int r, boolean state) {
        this.q = q;
        this.r = r;
        this.state = state;
    }

    public int getQ(){
        return 0;
    }

    public int getR(){
        return r;
    }
    
    public boolean getState() {
        return state;
    }

    public void setState(boolean newState) {
        this.state = newState;
    }
}