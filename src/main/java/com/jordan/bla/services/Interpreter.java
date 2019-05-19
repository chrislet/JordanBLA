package com.jordan.bla.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class Interpreter {

    private int lowerXbound;
    private int lowerYbound;
    private int upperXbound;
    private int upperYbound;

    public void Interpreter (){
    }

    public int [] parseInput (String stringInput)
            throws LandOutOfBoundsException, LandInputOutOfOrderException,
            InsufficientDataPointsException, NumberFormatException {
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
            System.exit(3);
        } catch (NumberFormatException e){
            System.out.println("Properties file is misconfigured.");
            e.printStackTrace();
            System.exit(1);
        }
        //Check if the user is attempting to exit the program
        if (stringInput.toLowerCase().contains("exit")){
            System.out.println("Have a nice day!");
            System.exit(0);
        }
        //Remove all non integers and non spaces from the input, cut the spaces down to one space
        stringInput = stringInput.replaceAll("[^0-9 -]", " ");
        stringInput = stringInput.replaceAll(" +", " ");
        stringInput = stringInput.trim();

        //Split the input to an array
        String [] dataPointsString = stringInput.split(" ");

        //Inputs should consist of multiples of 4, as the points are x,y coordinates and indicate two points each
        if (dataPointsString.length % 4 != 0 || dataPointsString.length == 0) {
            System.out.println(Arrays.toString(dataPointsString));
            throw new InsufficientDataPointsException("You gave me " + dataPointsString.length + " point(s).");

        }

        //Convert to int []
        int [] dataPointsInt = new int [dataPointsString.length];
        try {
            for (int ii = 0; ii < dataPointsString.length; ii++){
                dataPointsInt [ii] = Integer.parseInt(dataPointsString [ii]);
            }
        }catch(NumberFormatException e ) {
            throw e;
        }catch(NullPointerException e){
            throw e;
        }

        //Check if the x coordinates are outside the boundaries
        for (int ii = 0; ii < dataPointsInt.length; ii+=2){
            if (dataPointsInt [ii] < lowerXbound || dataPointsInt [ii] > upperXbound ){
                throw new LandOutOfBoundsException("x coordinates out of bounds " + dataPointsInt [ii]);
            }
        }

        //Check if the y coordinates are outside the boundaries
        for (int ii = 1; ii < dataPointsInt.length; ii+=2){
            if (dataPointsInt [ii] < lowerYbound || dataPointsInt [ii] > upperYbound ){
                throw new LandOutOfBoundsException("y coordinates out of bounds " + dataPointsInt [ii]);
            }
        }

        //Check if the vertices are bottom left to top right, we accept do accept zero height or zero width
        for (int ii=0; ii < dataPointsInt.length; ii+=4){
            if (dataPointsInt[ii+2] < dataPointsInt[ii] || dataPointsInt[ii+3] < dataPointsInt[ii+1]){
                throw new LandInputOutOfOrderException("Data points out of order "
                        + dataPointsInt[ii] + " "
                        + dataPointsInt[ii+1] + " "
                        + dataPointsInt[ii+2] + " "
                        + dataPointsInt[ii+3]);
            }
        }


        return dataPointsInt;
    }

    public static class LandOutOfBoundsException extends Exception {
        public LandOutOfBoundsException(String message){
            super(message);
        }
    }

    public static class LandInputOutOfOrderException extends Exception {
        public LandInputOutOfOrderException(String message){
            super(message);
        }
    }

    public static class InsufficientDataPointsException extends Exception {
        public InsufficientDataPointsException(String message){
            super(message);
        }
    }
}
