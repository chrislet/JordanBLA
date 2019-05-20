package com.jordan.bla.models;

public class Barren {

    private int[] coordinates = new int[4];

    public Barren() {

    }

    public int[] getCoordinates() {

        return coordinates;
    }

    public void setCoordinates(int[] barrenLand) {

        if (barrenLand.length != 4) {
            throw new ArrayIndexOutOfBoundsException("Barren land needs 4 points, you gave me " + barrenLand.length);
        }
        this.coordinates = barrenLand;
    }
}
