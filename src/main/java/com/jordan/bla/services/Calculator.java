package com.jordan.bla.services;


import com.jordan.bla.models.Fertile;
import com.jordan.bla.models.Field;
import com.jordan.bla.models.LandState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Calculator {

    private LandState[][] land;
    private int lowerXbound;
    private int lowerYbound;
    private int upperXbound;
    private int upperYbound;
    private List<Fertile> fertileLands = new ArrayList<>();


    public Calculator() {
        Boundaries boundary = new Boundaries();
        this.lowerXbound = boundary.getLowerXbound();
        this.lowerYbound = boundary.getLowerYbound();
        this.upperXbound = boundary.getUpperXbound();
        this.upperYbound = boundary.getUpperYbound();
    }

    public void addField(Field myField) {
        this.land = myField.getLand();
    }

    public List<Fertile> getFertileLands() {
        Collections.sort(fertileLands);
        return fertileLands;
    }

    public void calculate() throws ArrayIndexOutOfBoundsException {
        int[] nextFertileLand;

        try {
            nextFertileLand = findNextFertileLand();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            System.out.println("Did you make all the land barren?");
            throw e;
        }


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

    private Fertile floodFertileLand(int x, int y) {
        this.land[x][y] = LandState.Flooded;
        int area = 1;
        LandState north;
        LandState south;
        LandState east;
        LandState west;
        boolean nblocked;
        boolean sblocked;
        boolean eblocked;
        boolean wblocked;

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
                    this.land[x][y] = LandState.Flooded;
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
                    this.land[x][y] = LandState.Flooded;
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
                    this.land[x][y] = LandState.Flooded;
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
                    this.land[x][y] = LandState.Flooded;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
            }

            int[] coords;
            
            try {
                coords = findFloodTouchingFertile();
            } catch (DoneFloodingException ex) {
                //We must be done flooding
                //System.out.println("I've finished flooding");
                Fertile myFertile = new Fertile();
                myFertile.setArea(area);
                return myFertile;
            }

            //We are not done flooding
            x = coords[0];
            y = coords[1];


        }

    }

    private int[] findNextFertileLand() throws ArrayIndexOutOfBoundsException {
        //Start at lowerXbound,lowerYbound and find the first part of fertile land
        int xCoord = -1;
        int yCoord = -1;
        int[] nextFertileLand = new int[2];
        xLoop:
        for (int ii = lowerXbound; ii <= upperXbound; ii++) {
            for (int jj = lowerYbound; jj <= upperYbound; jj++) {
                if (land[ii][jj] == LandState.Fertile) {
                    xCoord = ii;
                    yCoord = jj;
                    break xLoop;
                }
            }
        }
        if (xCoord == -1) {
            throw new ArrayIndexOutOfBoundsException("There is no more fertile land.");
        }
        nextFertileLand[0] = xCoord;
        nextFertileLand[1] = yCoord;
        return nextFertileLand;
    }

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

        throw new NoFertileLandSurroundingException("No more fertile land here.");
    }

    private int[] findFloodTouchingFertile() throws DoneFloodingException {
        int[] coords = new int[2];
        coords[0] = -1;
        coords[1] = -1;
        for (int ii = lowerXbound; ii <= upperXbound; ii++) {
            for (int jj = lowerYbound; jj <= upperYbound; jj++) {

                //0 indicates barren land, 1 indicates fertile land
                if (land[ii][jj] == LandState.Barren || land[ii][jj] == LandState.Fertile) {
                    //System.out.println("done?");
                    continue;
                }

                //2 indicates flooded land, look around here for unflooded, but still fertile land
                if (land[ii][jj] == LandState.Flooded) {

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

    private LandState lookN(int x, int y) {
        if (y >= upperYbound) {
            return LandState.OutOfBounds;
        } else {
            return land[x][y + 1];
        }
    }

    private LandState lookS(int x, int y) {
        if (y <= lowerYbound) {
            return LandState.OutOfBounds;
        } else {
            return land[x][y - 1];
        }
    }

    private LandState lookE(int x, int y) {
        if (x >= upperXbound) {
            return LandState.OutOfBounds;
        } else {
            return land[x + 1][y];
        }
    }

    private LandState lookW(int x, int y) {
        if (x <= lowerXbound) {
            return LandState.OutOfBounds;
        } else {
            return land[x - 1][y];
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
