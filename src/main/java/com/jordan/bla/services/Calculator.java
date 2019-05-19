package com.jordan.bla.services;



import com.jordan.bla.models.Fertile;
import com.jordan.bla.models.Field;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Calculator {

    private int[][] land;
    private int lowerXbound;
    private int lowerYbound;
    private int upperXbound;
    private int upperYbound;
    private List<Fertile> fertileLands = new ArrayList<>();


    public void Calculator() {
    }

    public void addField (Field myField){
        try {
            InputStream input = new FileInputStream("src/main/resources/BarrenLand.properties");
            Properties propFile = new Properties();
            if (input == null){
                System.out.println("Unable to load properties file.");
                System.exit(1);
            }
            propFile.load(input);
            input.close();
            //Set boundaries
            this.lowerXbound = Integer.parseInt(propFile.getProperty("field.lowerXbound"));
            this.lowerYbound = Integer.parseInt(propFile.getProperty("field.lowerYbound"));
            this.upperXbound = Integer.parseInt(propFile.getProperty("field.upperXbound"));
            this.upperYbound = Integer.parseInt(propFile.getProperty("field.upperYbound"));
        } catch (IOException e) {
            System.out.println("Unable to load properties file.");
            e.printStackTrace();
            System.exit(1);
        } catch (NumberFormatException e){
            System.out.println("Properties file is misconfigured.");
            e.printStackTrace();
            System.exit(1);
        }
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

                this.fertileLands.add(floodFertileLand(x,y));
                try{
                    nextFertileLand = findNextFertileLand();
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    //There's no more fertile land to add to our list
                    break;
                }
        }
        return;



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

        while (true){

            nblocked = false;
            sblocked = false;
            eblocked = false;
            wblocked = false;

            try {//First we look N
                n = lookN(x,y);
            }catch(ArrayIndexOutOfBoundsException ex){
                n = -1;
            }
            try {//Then S
                s = lookS(x,y);
            }catch(ArrayIndexOutOfBoundsException ex){
                s = -1;
            }
            try {//Then E
                e = lookE(x,y);
            }catch(ArrayIndexOutOfBoundsException ex){
                e = -1;
            }
            try {//Then W
                w = lookW(x,y);
            }catch(ArrayIndexOutOfBoundsException ex){
                w = -1;
            }

            switch (n) {
                case -1:
                    nblocked = true;
                    break;
                case 0:
                    nblocked = true;
                    break;
                case 1:
                    y++;
                    area++;
                    this.land[x][y] = 2;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
                case 2:
                    nblocked = true;
                    break;
            }
            switch (s) {
                case -1:
                    sblocked = true;
                    break;
                case 0:
                    sblocked = true;
                    break;
                case 1:
                    y--;
                    area++;
                    this.land[x][y] = 2;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
                case 2:
                    sblocked = true;
                    break;
            }
            switch (e) {
                case -1:
                    eblocked = true;
                    break;
                case 0:
                    eblocked = true;
                    break;
                case 1:
                    x++;
                    area++;
                    this.land[x][y] = 2;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
                case 2:
                    eblocked = true;
                    break;
            }
            switch (w) {
                case -1:
                    wblocked = true;
                    break;
                case 0:
                    wblocked = true;
                    break;
                case 1:
                    x--;
                    area++;
                    this.land[x][y] = 2;
                    //System.out.println("Flooded " + x + " and " + y + " ");
                    continue;
                case 2:
                    wblocked = true;
                    break;
            }
            int [] coords;
            if (nblocked && sblocked && eblocked && wblocked ){
                try{ coords = stillFlooding();
                }
                catch (DoneFloodingException ex){
                 //We must be done flooding
                    //System.out.println("I've finished flooding");
                    Fertile myFertile = new Fertile();
                    myFertile.setArea(area);
                    return myFertile;
                }

                //We are not done flooding
                x = coords [0];
                y = coords [1];
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
            yLoop:
            for (int jj = lowerYbound; jj <= upperYbound; jj++) {
                if (land[ii][jj] == 1) {
                    xCoord = ii;
                    yCoord = jj;
                    break xLoop;
                }
            }
        }
        if (xCoord == -1 || yCoord == -1) {
            throw new ArrayIndexOutOfBoundsException("There is no more fertile land.");
        }
        nextFertileLand[0] = xCoord;
        nextFertileLand[1] = yCoord;
        return nextFertileLand;
    }

    private int[] floodTouchingFertile(int x, int y) throws NoFertileLandSurroundingException {
        int[] coords = new int[2];
        int n;
        int s;
        int e;
        int w;
        coords [0] = x;
        coords [1] = y;

        try {//First we look N
            n = lookN(x,y);
        }catch(ArrayIndexOutOfBoundsException ex){
            n = -1;
        }
        try {//Then S
            s = lookS(x,y);
        }catch(ArrayIndexOutOfBoundsException ex){
            s = -1;
        }
        try {//Then E
            e = lookE(x,y);
        }catch(ArrayIndexOutOfBoundsException ex){
            e = -1;
        }
        try {//Then W
            w = lookW(x,y);
        }catch(ArrayIndexOutOfBoundsException ex){
            w = -1;
        }

        switch (n){
            case -1:
                break;
            case 0:
                break;
            case 1:
                return coords;
            case 2:
                break;
        }
        switch (s){
            case -1:
                break;
            case 0:
                break;
            case 1:
                return coords;
            case 2:
                break;
        }
        switch (e){
            case -1:
                break;
            case 0:
                break;
            case 1:
                return coords;
            case 2:
                break;
        }
        switch (w){
            case -1:
                break;
            case 0:
                break;
            case 1:
                return coords;
            case 2:
                break;
        }
        throw new NoFertileLandSurroundingException("No more fertile land here.");
    }

    private int[] stillFlooding() throws DoneFloodingException {
        int [] coords = new int [2];
        coords [0] = -1;
        coords [1] = -1;
        xLoop:
        for (int ii = lowerXbound; ii <= upperXbound; ii++) {
            yLoop:
            for (int jj = lowerYbound; jj <= upperYbound; jj++) {

                //0 indicates barren land, 1 indicates fertile land
                if (land[ii][jj] == 0 || land[ii][jj] == 1) {
                    //System.out.println("done?");
                    continue yLoop;
                }

                //2 indicates flooded land, look around here for unflooded, but still fertile land
                if (land[ii][jj] == 2) {

                    try{
                        coords = floodTouchingFertile(ii, jj);
                        return coords;
                    }catch(NoFertileLandSurroundingException e){
                        //There's nothing here, let's find somewhere else

                    }

                }
            }
        }
        if (coords[0] == -1 || coords[1] == -1) {
            throw new DoneFloodingException("Done flooding.");
        }
        else {
            return coords;
        }
    }

    private int lookN(int x, int y) throws ArrayIndexOutOfBoundsException {
        if (y == upperYbound) {
            throw new ArrayIndexOutOfBoundsException("Out of bounds");
        } else {
            return land[x][y + 1];
        }
    }

    private int lookS(int x, int y) throws ArrayIndexOutOfBoundsException {
        if (y == lowerYbound) {
            throw new ArrayIndexOutOfBoundsException("Out of bounds");
        } else {
            return land[x][y - 1];
        }
    }

    private int lookE(int x, int y) throws ArrayIndexOutOfBoundsException {
        if (x == upperXbound) {
            throw new ArrayIndexOutOfBoundsException("Out of bounds");
        } else {
            return land[x + 1][y];
        }
    }

    private int lookW(int x, int y) throws ArrayIndexOutOfBoundsException {
        if (x == lowerXbound) {
            throw new ArrayIndexOutOfBoundsException("Out of bounds");
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
        public NoFertileLandSurroundingException(String message) { super(message) ; }
    }
}
