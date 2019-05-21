package com.jordan.bla.services;


import com.jordan.bla.models.FarmField;
import com.jordan.bla.models.FertileLand;
import com.jordan.bla.models.LandState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Calculator {

    private LandState[][] farmFieldArray;
    private int lowerXbound;
    private int lowerYbound;
    private int upperXbound;
    private int upperYbound;
    private List<FertileLand> fertileLands = new ArrayList<>();


    public Calculator() {
        Boundaries boundary = new Boundaries();
        this.lowerXbound = boundary.getLowerXbound();
        this.lowerYbound = boundary.getLowerYbound();
        this.upperXbound = boundary.getUpperXbound();
        this.upperYbound = boundary.getUpperYbound();
    }

    public void addField(FarmField myFarmField) {

        this.farmFieldArray = myFarmField.getFarmFieldArray();
    }

    //Sort and return our list of FertileLand objects.
    public List<FertileLand> getFertileLands() {
        Collections.sort(fertileLands);
        return fertileLands;
    }


    public void calculate() {
        int[] nextFertileLand;

        //Grab the first point on the FarmField that is Fertile
        try {
            nextFertileLand = findNextFertileLand();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            System.out.println("Did you make all the land barren?");
            return;
        }

        //Parent loop that generates all FertileLand objects and adds them to the fertileLands List.
        while (true) {
            int x = nextFertileLand[0];
            int y = nextFertileLand[1];

            this.fertileLands.add(floodFertileLand(x, y));
            try {
                nextFertileLand = findNextFertileLand();
            } catch (ArrayIndexOutOfBoundsException e) {
                //There's no more fertile land to add to our list
                break;
            }
        }
    }

    //Change the state of Fertile land to Flooded,
    //Add 1 to the area of our Fertile land object counter,
    //Find an adjacent piece of Fertile land to flood, and go there
    private FertileLand floodFertileLand(int x, int y) {
        this.farmFieldArray[x][y] = LandState.Flooded;
        int area = 1;
        LandState north;
        LandState south;
        LandState east;
        LandState west;

        while (true) {

            //First we look N
            north = lookN(x, y);
            switch (north) {
                case OutOfBounds:
                case Barren:
                case Flooded:
                    break;
                case Fertile:
                    y++;
                    area++;
                    this.farmFieldArray[x][y] = LandState.Flooded;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
            }

            //Then S
            south = lookS(x, y);
            switch (south) {
                case OutOfBounds:
                case Barren:
                case Flooded:
                    break;
                case Fertile:
                    y--;
                    area++;
                    this.farmFieldArray[x][y] = LandState.Flooded;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
            }

            //Then E
            east = lookE(x, y);
            switch (east) {
                case OutOfBounds:
                case Barren:
                case Flooded:
                    break;
                case Fertile:
                    x++;
                    area++;
                    this.farmFieldArray[x][y] = LandState.Flooded;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
            }

            //Then W
            west = lookW(x, y);
            switch (west) {
                case OutOfBounds:
                case Barren:
                case Flooded:
                    break;
                case Fertile:
                    x--;
                    area++;
                    this.farmFieldArray[x][y] = LandState.Flooded;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
            }

            //If we're stuck in a corner, we might have flooded the entire Fertile section, or we might have
            //Just put ourselves in a bind, we should check our FarmField for any Flooded land touching Fertile land
            int[] coords;

            try {
                coords = findFloodTouchingFertile();
            } catch (DoneFloodingException ex) {
                //We must be done flooding this section of land, create our FertileLand object, and return it
                FertileLand myFertileLand = new FertileLand();
                myFertileLand.setArea(area);
                return myFertileLand;
            }

            //We are not done flooding, we've found a plot of Flooded land that has nearby Fertile land
            //Go to the plot of Flooded land, and start looking around for more fertile land to flood
            x = coords[0];
            y = coords[1];


        }

    }
    //Start at lowerXbound,lowerYbound and find the first part of Fertile land on our FarmField
    private int[] findNextFertileLand() throws ArrayIndexOutOfBoundsException {
        int xCoord = -1;
        int yCoord = -1;
        int[] nextFertileLand = new int[2];
        xLoop:
        for (int ii = lowerXbound; ii <= upperXbound; ii++) {
            for (int jj = lowerYbound; jj <= upperYbound; jj++) {
                if (farmFieldArray[ii][jj] == LandState.Fertile) {
                    xCoord = ii;
                    yCoord = jj;
                    break xLoop;
                }
            }
        }
        //If there is no more Fertile land on our FarmField, throw an exception, instead of returning coordinates.
        if (xCoord == -1) {
            throw new ArrayIndexOutOfBoundsException("There is no more fertile land.");
        }
        nextFertileLand[0] = xCoord;
        nextFertileLand[1] = yCoord;
        return nextFertileLand;
    }

    //Search the entire FarmField for any Flooded land that is nearby any Fertile land
    private int[] findFloodTouchingFertile() throws DoneFloodingException {
        int[] coords = new int[2];
        coords[0] = -1;
        coords[1] = -1;
        for (int ii = lowerXbound; ii <= upperXbound; ii++) {
            for (int jj = lowerYbound; jj <= upperYbound; jj++) {
                //Look around here for unflooded, but still fertile land
                if (farmFieldArray[ii][jj] == LandState.Flooded) {
                    try {
                        coords = isNearFertileLand(ii, jj);
                        return coords;
                    } catch (NoFertileLandSurroundingException e) {
                        //There's nothing here, let's find somewhere else
                    }

                }
            }
        }

        throw new DoneFloodingException("Done flooding.");

    }

    //Search the immediate coordinates for nearby fertile land that can be accessed
    private int[] isNearFertileLand(int x, int y) throws NoFertileLandSurroundingException {
        int[] coords = new int[2];
        LandState north;
        LandState south;
        LandState east;
        LandState west;
        coords[0] = x;
        coords[1] = y;

        //First we look N
        north = lookN(x, y);
        switch (north) {
            case OutOfBounds:
            case Barren:
            case Flooded:
                break;
            case Fertile:
                return coords;
        }

        //Then S
        south = lookS(x, y);
        switch (south) {
            case OutOfBounds:
            case Barren:
            case Flooded:
                break;
            case Fertile:
                return coords;
        }

        //Then E
        east = lookE(x, y);
        switch (east) {
            case OutOfBounds:
            case Barren:
            case Flooded:
                break;
            case Fertile:
                return coords;
        }

        //Then W
        west = lookW(x, y);
        switch (west) {
            case OutOfBounds:
            case Barren:
            case Flooded:
                break;
            case Fertile:
                return coords;
        }
        //We must not be near any Fertile land
        throw new NoFertileLandSurroundingException("No more fertile land here.");
    }

    //Without actually moving around, just grab the value of the coordinates in the 4 cardinal directions
    private LandState lookN(int x, int y) {
        if (y >= upperYbound) {
            return LandState.OutOfBounds;
        } else {
            return farmFieldArray[x][y + 1];
        }
    }

    private LandState lookS(int x, int y) {
        if (y <= lowerYbound) {
            return LandState.OutOfBounds;
        } else {
            return farmFieldArray[x][y - 1];
        }
    }

    private LandState lookE(int x, int y) {
        if (x >= upperXbound) {
            return LandState.OutOfBounds;
        } else {
            return farmFieldArray[x + 1][y];
        }
    }

    private LandState lookW(int x, int y) {
        if (x <= lowerXbound) {
            return LandState.OutOfBounds;
        } else {
            return farmFieldArray[x - 1][y];
        }
    }

    private class DoneFloodingException extends Throwable {
        private DoneFloodingException(String message) {
            super(message);
        }
    }

    private class NoFertileLandSurroundingException extends Throwable {
        private NoFertileLandSurroundingException(String message) {
            super(message);
        }
    }

}
