package com.example;

import java.io.Serializable;

public abstract class Shape implements Serializable {
    private boolean state; //alive or dead

    public Shape(boolean state) {
        this.state = state;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean newState) {
        this.state = newState;
    }
}