package com.example;

import java.io.Serializable;

class Square implements Serializable{
    private int i;
    private int j;
    private boolean state; //alive or dead

    public Square(int i, int j, boolean state) {
        this.i = i;
        this.j = j;
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean newState) {
        this.state = newState;
    }
}
