package com.jordan.bla.models;

import com.jordan.bla.services.Boundaries;

public class Field {

    private int lowerXbound;
    private int lowerYbound;
    private int upperXbound;
    private int upperYbound;
    private int[][] land;


    public Field() {
        Boundaries boundary = new Boundaries();
        this.lowerXbound = boundary.getLowerXbound();
        this.lowerYbound = boundary.getLowerYbound();
        this.upperXbound = boundary.getUpperXbound();
        this.upperYbound = boundary.getUpperYbound();

    }

    public void createLand() {

        this.land = new int[upperXbound + 1][upperYbound + 1];

        //For fields that don't start at 0,0
        for (int ii = 0; ii <= upperXbound; ii++) {
            for (int jj = 0; jj <= upperYbound; jj++) {
                this.land[ii][jj] = 0;
            }
        }

        for (int ii = lowerXbound; ii <= upperXbound; ii++) {
            for (int jj = lowerYbound; jj <= upperYbound; jj++) {
                this.land[ii][jj] = 1;
            }
        }
    }

    public void addBarrenLand(Barren barrenLand) {
        int[] barrenLandCoordinates = barrenLand.getCoordinates();
        for (int ii = barrenLandCoordinates[0]; ii <= barrenLandCoordinates[2]; ii++) {
            for (int jj = barrenLandCoordinates[1]; jj <= barrenLandCoordinates[3]; jj++) {
                this.land[ii][jj] = 0;
            }
        }
    }

    public int[][] getLand() {
        return this.land;
    }
}
