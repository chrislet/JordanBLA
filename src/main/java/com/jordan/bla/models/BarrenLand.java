package com.jordan.bla.models;

public class BarrenLand {

    private int[] coordinates = new int[4];

    public BarrenLand() {

    }

    public int[] getCoordinates() {

        return coordinates;
    }

    public void setCoordinates(int[] barrenLand) {

        if (barrenLand.length != 4) {
            throw new ArrayIndexOutOfBoundsException("BarrenLand land needs 4 points, you gave me " + barrenLand.length);
        }
        this.coordinates = barrenLand;
    }
}
