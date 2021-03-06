package com.jordan.bla;


import com.jordan.bla.models.BLAExceptions;
import com.jordan.bla.models.BarrenLand;
import com.jordan.bla.models.FarmField;
import com.jordan.bla.models.FertileLand;
import com.jordan.bla.services.Boundaries;
import com.jordan.bla.services.Calculator;
import com.jordan.bla.services.InputParser;

import java.util.List;
import java.util.Scanner;


public class Application {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String input;
        int[] dataPointsInt;
        System.out.println("Please input barren land.  Expected format is 4 numbers, " +
                "denoting bottom left to top right.");
        System.out.println("x y X Y ");
        System.out.println("You can input multiple barren sections on one line, or type \"exit\" to end the program.");
        while (scan.hasNextLine()) {

            //Grab our Boundaries
            Boundaries boundary = new Boundaries();

            input = scan.nextLine();
            InputParser interpret = new InputParser(boundary);
            try {
                dataPointsInt = interpret.parseInput(input);
            } catch (BLAExceptions.LandOutOfBoundsException e) {
                System.out.println(e.getMessage());
                System.out.println("Your barren land was outside of the boundary of the total land.");
                System.out.println("Please try again.");
                continue;
            } catch (BLAExceptions.LandInputOutOfOrderException e) {
                System.out.println(e.getMessage());
                System.out.println("Your barren land was not input with bottom left first, followed by top right.");
                System.out.println("Please try again.");
                continue;
            } catch (BLAExceptions.InsufficientDataPointsException e) {
                System.out.println(e.getMessage());
                System.out.println("Your barren land did not have sufficient data points to draw a rectangle.");
                System.out.println("Please try again.");
                continue;
            } catch (NumberFormatException | NullPointerException e) {
                System.out.println(e.getMessage());
                System.out.println("Your input could not be parsed into integers.");
                System.out.println("Please try again.");
                continue;
            }

            //User must be attempting to exit the program
            if (dataPointsInt.length == 0){
                break;
            }

            //Create our FarmField
            FarmField myFarmField = new FarmField(boundary);
            myFarmField.createFarmFieldArray();

            //Chop our input into individual BarrenLand objects, and add them to the FarmField
            int[] barrenPoints = new int[4];
            for (int ii = 0; ii < dataPointsInt.length; ii += 4) {
                barrenPoints[0] = dataPointsInt[ii];
                barrenPoints[1] = dataPointsInt[ii + 1];
                barrenPoints[2] = dataPointsInt[ii + 2];
                barrenPoints[3] = dataPointsInt[ii + 3];
                BarrenLand aBarrenLand = new BarrenLand();
                aBarrenLand.setCoordinates(barrenPoints);
                myFarmField.addBarrenLand(aBarrenLand);

            }




            //Create our Calculator object, and add our FarmField object to it
            Calculator calc = new Calculator(boundary);
            calc.addField(myFarmField);

            //Calculate the area of our FertileLand objects, and store them
            calc.calculate();

            //Sort and return our list of FertileLand objects
            List<FertileLand> myFertileLands = calc.getFertileLands();

            //Print our sorted list of FertileLand objects
            for (FertileLand land : myFertileLands) {
                System.out.print(land.getArea() + " ");
            }
            System.out.println(" ");
            System.out.println("Awaiting further input.");
            System.out.println("Type \"exit\" to end the program.");
        }

    }
}
