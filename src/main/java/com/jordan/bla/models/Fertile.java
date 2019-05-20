package com.jordan.bla.models;

import java.util.Objects;

public class Fertile implements Comparable<Fertile> {

    private int area;

    public Fertile() {
    }

    public int getArea() {
        return area;
    }

    public void setArea(int a) {
        this.area = a;
    }

    public int compareTo(Fertile obj) {
        return this.area - obj.area;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }


    public boolean equals(Fertile obj) {
        return this.area == obj.getArea();
    }


}
