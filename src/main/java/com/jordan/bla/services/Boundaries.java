package com.jordan.bla.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Boundaries {

    private int lowerXbound;
    private int lowerYbound;
    private int upperXbound;
    private int upperYbound;

    //Read from the BarrenLand.properties file, and grab our bounds.
    public Boundaries() {
        final Properties propFile = new Properties();
        try (final FileInputStream stream = new FileInputStream("src/main/resources/BarrenLand.properties")) {
            propFile.load(stream);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to find the properties file");
        } catch (IOException e) {
            System.out.println("IOException, unable to read the properties file");
        }

        try {
            this.lowerXbound = Integer.parseInt(propFile.getProperty("field.lowerXbound"));
            this.lowerYbound = Integer.parseInt(propFile.getProperty("field.lowerYbound"));
            this.upperXbound = Integer.parseInt(propFile.getProperty("field.upperXbound"));
            this.upperYbound = Integer.parseInt(propFile.getProperty("field.upperYbound"));
        } catch (NumberFormatException e) {
            System.out.println("Properties file is misconfigured.");
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
