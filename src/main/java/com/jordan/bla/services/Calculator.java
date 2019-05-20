package com.jordan.bla.services;


import com.jordan.bla.models.Fertile;
import com.jordan.bla.models.Field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calculator {

    private int[][] land;
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
        this.land[x][y] = 2;
        int area = 1;
        int n;
        int s;
        int e;
        int w;
        boolean nblocked;
        boolean sblocked;
        boolean eblocked;
        boolean wblocked;

        while (true) {

            nblocked = false;
            sblocked = false;
            eblocked = false;
            wblocked = false;

            //First we look N
            n = lookN(x, y);
            switch (n) {
                case -1:
                case 0:
                case 2:
                    nblocked = true;
                    break;
                case 1:
                    y++;
                    area++;
                    this.land[x][y] = 2;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
            }

            //Then S
            s = lookS(x, y);
            switch (s) {
                case -1:
                case 0:
                case 2:
                    sblocked = true;
                    break;
                case 1:
                    y--;
                    area++;
                    this.land[x][y] = 2;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
            }

            //Then E
            e = lookE(x, y);
            switch (e) {
                case -1:
                case 0:
                case 2:
                    eblocked = true;
                    break;
                case 1:
                    x++;
                    area++;
                    this.land[x][y] = 2;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
            }

            //Then W
            w = lookW(x, y);
            switch (w) {
                case -1:
                case 0:
                case 2:
                    wblocked = true;
                    break;
                case 1:
                    x--;
                    area++;
                    this.land[x][y] = 2;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
            }

            int[] coords;
            if (nblocked && sblocked && eblocked && wblocked) {
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

    }

    private int[] findNextFertileLand() throws ArrayIndexOutOfBoundsException {
        //Start at lowerXbound,lowerYbound and find the first part of fertile land
        int xCoord = -1;
        int yCoord = -1;
        int[] nextFertileLand = new int[2];
        xLoop:
        for (int ii = lowerXbound; ii <= upperXbound; ii++) {
            for (int jj = lowerYbound; jj <= upperYbound; jj++) {
                if (land[ii][jj] == 1) {
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
        int north;
        int south;
        int east;
        int west;
        coords[0] = x;
        coords[1] = y;

        //First we look N
        north = lookN(x, y);
        switch (north) {
            case -1:
            case 0:
            case 2:
                break;
            case 1:
                return coords;
        }

        //Then S
        south = lookS(x, y);
        switch (south) {
            case -1:
            case 0:
            case 2:
                break;
            case 1:
                return coords;
        }

        //Then E
        east = lookE(x, y);
        switch (east) {
            case -1:
            case 0:
            case 2:
                break;
            case 1:
                return coords;
        }

        //Then W
        west = lookW(x, y);
        switch (west) {
            case -1:
            case 0:
            case 2:
                break;
            case 1:
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
                if (land[ii][jj] == 0 || land[ii][jj] == 1) {
                    //System.out.println("done?");
                    continue;
                }

                //2 indicates flooded land, look around here for unflooded, but still fertile land
                if (land[ii][jj] == 2) {

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

    private int lookN(int x, int y) {
        if (y >= upperYbound) {
            return -1;
        } else {
            return land[x][y + 1];
        }
    }

    private int lookS(int x, int y) {
        if (y <= lowerYbound) {
            return -1;
        } else {
            return land[x][y - 1];
        }
    }

    private int lookE(int x, int y) {
        if (x >= upperXbound) {
            return -1;
        } else {
            return land[x + 1][y];
        }
    }

    private int lookW(int x, int y) {
        if (x <= lowerXbound) {
            return -1;
        } else {
            return land[x - 1][y];
        }
    }

    private class DoneFloodingException extends Throwable {
        public DoneFloodingException(String message) {
            super(message);
        }
    }

    private class NoFertileLandSurroundingException extends Throwable {
        public NoFertileLandSurroundingException(String message) {
            super(message);
        }
    }
}
