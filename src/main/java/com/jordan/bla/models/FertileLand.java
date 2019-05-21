package com.jordan.bla.models;

import java.util.Objects;

public class FertileLand implements Comparable<FertileLand> {

    private int area;

    public FertileLand() {
    }

    public int getArea() {

        return area;
    }

    public void setArea(int a) {

        this.area = a;
    }

    public int compareTo(FertileLand obj) {

        return this.area - obj.area;
    }

    @Override
    public int hashCode() {

        return Objects.hash();
    }


    public boolean equals(FertileLand obj) {

        return this.area == obj.getArea();
    }


}
