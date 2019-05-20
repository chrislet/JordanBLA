package com.jordan.bla.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Boundaries {

    private int lowerXbound;
    private int lowerYbound;
    private int upperXbound;
    private int upperYbound;

    public Boundaries() {
        try {
            InputStream input = new FileInputStream("src/main/resources/BarrenLand.properties");
            Properties propFile = new Properties();
            if (input == null) {
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
        } catch (NumberFormatException e) {
            System.out.println("Properties file is misconfigured.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public int getLowerXbound() {
        return this.lowerXbound;
    }

    public int getLowerYbound() {
        return this.lowerYbound;
    }

    public int getUpperXbound() {
        return this.upperXbound;
    }

    public int getUpperYbound() {
        return this.upperYbound;
    }
}
