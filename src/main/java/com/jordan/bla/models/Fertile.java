package com.jordan.bla.models;

import java.util.Objects;

public class Fertile implements Comparable<Fertile> {

    private int area;

    public void Fertile (){
    }

    public void setArea(int a){
        this.area = a;
    }
    public int getArea(){
        return area;
    }


    public int compareTo(Fertile obj) {
        int comparison = this.area - obj.area;
        return comparison;
    }

    @Override
    public int hashCode() {
        return Objects.hash();
    }


    public boolean equals(Fertile obj) {
        if (this.area == obj.getArea()){
            return true;
        }
        else {
            return false;
        }
    }


}
