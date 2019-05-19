package com.jordan.bla.models;

public class Barren {

    private int [] coordinates = new int [4];

    public void Barren(){

    }

    public void setCoordinates(int [] barrenLand){

        if (barrenLand.length != 4){
            throw new ArrayIndexOutOfBoundsException("Barren land needs 4 points, you gave me " + barrenLand.length);
        }
        try {
            for (int ii = 0; ii < barrenLand.length; ii++){
                this.coordinates [ii] = barrenLand [ii];
            }
        }catch(NullPointerException e){
            throw e;
        }
    }

    public int [] getCoordinates(){

        return coordinates;
    }

    public int getArea (){
        int x = coordinates [2] - coordinates [0] + 1;
        int y = coordinates [3] - coordinates [1] + 1;
        int area = x * y;
        return area;
    }
}
