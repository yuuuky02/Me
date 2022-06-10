package com.example.meee;

import android.graphics.Color;

public class Diagram {
    private float x;
    private float y;
    private int width = 5;
    private int color = Color.RED;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Diagram(float x, float y, int width, int color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.color = color;


    }
}
