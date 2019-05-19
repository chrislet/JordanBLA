package com.jordan.bla.models;

import com.jordan.bla.models.Barren;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Field {

    private int lowerXbound;
    private int lowerYbound;
    private int upperXbound;
    private int upperYbound;
    private int [] [] land;



    public void Field() {

    }

    public void createLand(){
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
            System.exit(2);
        } catch (NumberFormatException e){
            System.out.println("Properties file is misconfigured.");
            e.printStackTrace();
            System.exit(1);
        }


        this.land = new int [upperXbound + 1] [upperYbound + 1];

        //For fields that don't start at 0,0
        for (int ii = 0; ii <= lowerXbound; ii++){
            for (int jj = 0; jj <= lowerYbound; jj++){
                this.land [ii] [jj] = 0;
            }
        }

        for (int ii = lowerXbound; ii <= upperXbound; ii++){
            for (int jj = lowerYbound; jj <= upperYbound; jj++){
                this.land [ii] [jj] = 1;
            }
        }
    }

    public void addBarrenLand(Barren barrenLand){
        int [] barrenLandCoordinates = barrenLand.getCoordinates();
        for (int ii = barrenLandCoordinates[0]; ii <= barrenLandCoordinates[2]; ii++){
            for (int jj = barrenLandCoordinates[1]; jj <= barrenLandCoordinates[3]; jj++){
                this.land [ii] [jj] = 0;
            }
        }
    }

    public int [] [] getLand () {
        return this.land;
    }
}
