package com.jordan.bla.services;

import com.jordan.bla.models.BLAExceptions;

import java.util.Arrays;

public class InputParser {

    private int lowerXbound;
    private int lowerYbound;
    private int upperXbound;
    private int upperYbound;

    public InputParser(Boundaries boundary) {
        this.lowerXbound = boundary.getLowerXbound();
        this.lowerYbound = boundary.getLowerYbound();
        this.upperXbound = boundary.getUpperXbound();
        this.upperYbound = boundary.getUpperYbound();
    }

    public int[] parseInput(String stringInput)
            throws BLAExceptions.LandOutOfBoundsException, BLAExceptions.LandInputOutOfOrderException,
            BLAExceptions.InsufficientDataPointsException, NumberFormatException {
        //Check if the user is attempting to exit the program
        if (stringInput.toLowerCase().contains("exit")) {
            System.out.println("Have a nice day!");
            return new int [0];
        }
        //Remove all non integers and non spaces from the input, cut the spaces down to one space
        stringInput = stringInput.replaceAll("[^0-9 -]", " ");
        stringInput = stringInput.replaceAll(" +", " ");
        stringInput = stringInput.trim();

        //Split the input to an array
        String[] dataPointsString = stringInput.split(" ");

        //Inputs should consist of multiples of 4, as the points are x,y coordinates and indicate two points each
        if (dataPointsString.length % 4 != 0 || dataPointsString.length == 0) {
            System.out.println(Arrays.toString(dataPointsString));
            throw new BLAExceptions.InsufficientDataPointsException("You gave me " + dataPointsString.length + " point(s).");

        }

        //Convert to int []
        int[] dataPointsInt = new int[dataPointsString.length];
        for (int ii = 0; ii < dataPointsString.length; ii++) {
            dataPointsInt[ii] = Integer.parseInt(dataPointsString[ii]);
        }

        //Check if the x coordinates are outside the boundaries
        for (int ii = 0; ii < dataPointsInt.length; ii += 2) {
            if (dataPointsInt[ii] < lowerXbound || dataPointsInt[ii] > upperXbound) {
                throw new BLAExceptions.LandOutOfBoundsException("x coordinates out of bounds " + dataPointsInt[ii]);
            }
        }

        //Check if the y coordinates are outside the boundaries
        for (int ii = 1; ii < dataPointsInt.length; ii += 2) {
            if (dataPointsInt[ii] < lowerYbound || dataPointsInt[ii] > upperYbound) {
                throw new BLAExceptions.LandOutOfBoundsException("y coordinates out of bounds " + dataPointsInt[ii]);
            }
        }

        //Check if the vertices are bottom left to top right
        for (int ii = 0; ii < dataPointsInt.length; ii += 4) {
            if (dataPointsInt[ii + 2] < dataPointsInt[ii] || dataPointsInt[ii + 3] < dataPointsInt[ii + 1]) {
                throw new BLAExceptions.LandInputOutOfOrderException("Data points out of order "
                        + dataPointsInt[ii] + " "
                        + dataPointsInt[ii + 1] + " "
                        + dataPointsInt[ii + 2] + " "
                        + dataPointsInt[ii + 3]);
            }
        }


        return dataPointsInt;
    }


}
