package com.jordan.bla.models;

import com.jordan.bla.services.Boundaries;

public class FarmField {

    private int lowerXbound;
    private int lowerYbound;
    private int upperXbound;
    private int upperYbound;
    private LandState[][] land;

    public FarmField() {
        Boundaries boundary = new Boundaries();
        this.lowerXbound = boundary.getLowerXbound();
        this.lowerYbound = boundary.getLowerYbound();
        this.upperXbound = boundary.getUpperXbound();
        this.upperYbound = boundary.getUpperYbound();

    }

    public void createLand() {
        //Include the + 1 to allow the last coordinate of the field to be included in the array.
        this.land = new LandState[upperXbound + 1][upperYbound + 1];

        //Initialize the entire Farm Field as Barren land.
        for (int ii = 0; ii <= upperXbound; ii++) {
            for (int jj = 0; jj <= upperYbound; jj++) {
                this.land[ii][jj] = LandState.Barren;
            }
        }
        //Starting at the lower bound, mark our Farm Field as Fertile land.
        for (int ii = lowerXbound; ii <= upperXbound; ii++) {
            for (int jj = lowerYbound; jj <= upperYbound; jj++) {
                this.land[ii][jj] = LandState.Fertile;
            }
        }
    }

    //Grab the coordinates of a Barren land object's bottom left and top right positions.
    //Iterate from the bottom left to the top right, and mark the Farm Field as Barren at those coordinates.
    public void addBarrenLand(BarrenLand barrenLand) {
        int[] barrenLandCoordinates = barrenLand.getCoordinates();
        for (int ii = barrenLandCoordinates[0]; ii <= barrenLandCoordinates[2]; ii++) {
            for (int jj = barrenLandCoordinates[1]; jj <= barrenLandCoordinates[3]; jj++) {
                this.land[ii][jj] = LandState.Barren;
            }
        }
    }

    public LandState[][] getLand() {
        return this.land;
    }
}
