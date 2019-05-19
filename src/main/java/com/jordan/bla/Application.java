package com.jordan.bla;


import com.jordan.bla.models.Barren;
import com.jordan.bla.models.Fertile;
import com.jordan.bla.models.Field;
import com.jordan.bla.services.Calculator;
import com.jordan.bla.services.Interpreter;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main (String args[]) {

        Scanner scan = new Scanner(System.in);
        String input;
        int [] dataPointsInt;

        while (true){
            System.out.println("Type \"exit\" to end the program.");
             input = scan.nextLine();
            Interpreter interpret = new Interpreter();
            try{
                dataPointsInt = interpret.parseInput(input);
            }
            catch (Interpreter.LandOutOfBoundsException e){
                System.out.println(e.getMessage());
                System.out.println("Your barren land was outside of the boundary of the total land.");
                System.out.println("Please try again.");
                continue;
            }
            catch (Interpreter.LandInputOutOfOrderException e){
                System.out.println(e.getMessage());
                System.out.println("Your barren land was not input with bottom left first, followed by top right.");
                System.out.println("Please try again.");
                continue;
            }
            catch (Interpreter.InsufficientDataPointsException e){
                System.out.println(e.getMessage());
                System.out.println("Your barren land did not have sufficient data points to draw a rectangle.");
                System.out.println("Please try again.");
                continue;
            }
            catch (NumberFormatException e){
                System.out.println(e.getMessage());
                System.out.println("Your input could not be parsed into integers.");
                System.out.println("Please try again.");
                continue;
            }
            catch (NullPointerException e){
                System.out.println(e.getMessage());
                System.out.println("Your input could not be parsed into integers.");
                System.out.println("Please try again.");
                continue;
            }
            //System.out.println("You typed " + input);
            //System.out.println("I parsed " +  Arrays.toString(dataPointsInt));

            Field myField = new Field ();
            myField.createLand();
            int [] barrenPoints = new int [4];
            for (int ii = 0; ii < dataPointsInt.length; ii+= 4){
                barrenPoints [0] = dataPointsInt [ii];
                barrenPoints [1] = dataPointsInt [ii+1];
                barrenPoints [2] = dataPointsInt [ii+2];
                barrenPoints [3] = dataPointsInt [ii+3];
                Barren aBarren = new Barren();
                aBarren.setCoordinates(barrenPoints);
                myField.addBarrenLand(aBarren);

            }

            /*
            int [] [] tempLand = myField.getLand();
            for (int ii = tempLand.length - 1; ii >= 0; ii--){
                for (int jj = 0; jj < tempLand[0].length; jj++) {
                    System.out.print(tempLand[ii][jj]);
                }
                System.out.println("");
            }
            */

            Calculator calc = new Calculator ();

            calc.addField(myField);

            calc.calculate();

            List<Fertile> myFertileLands = calc.getFertileLands();

            //System.out.println("Number of fertile lands is "+ myFertileLands.size());
            for (Fertile land : myFertileLands){
                System.out.printf(land.getArea() + " ");
            }
            System.out.println(" ");

        }

    }
}
