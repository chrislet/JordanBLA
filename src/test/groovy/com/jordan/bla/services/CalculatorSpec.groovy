package com.jordan.bla.services

import com.jordan.bla.models.BarrenLand
import com.jordan.bla.models.FarmField
import spock.lang.Specification

class CalculatorSpec extends Specification{
    def 'test findNextFertileLand' (){
        given:"A FarmField of size 2 x 3, and a piece of BarrenLand," +
                "Set points ( 0 , 0 ) and ( 1 , 1 ) for the BarrenLand"
        def aCalculator = new Calculator()
        def aFarmField = new FarmField()
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        def aBarren = new BarrenLand()
        aBarren.setCoordinates([0, 0, 1, 1] as int[])

        when:"Create a FarmField,and add the Barren land to it," +
                "Then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aFarmField.addBarrenLand(aBarren)
        aCalculator.addField(aFarmField)

        then:"We should find the point ( 0 , 2 ) as Fertile land"
        aCalculator.findNextFertileLand() == ([ 0 , 2 ] as int [])
    }

    def 'test findNextFertileLandAllBarren' (){
        given:"A FarmField of size 2 x 3, and a piece of BarrenLand," +
                "Set points ( 0 , 0 ) and ( 1 , 2 ) for the BarrenLand"
        def aCalculator = new Calculator()
        def aFarmField = new FarmField()
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        def aBarren = new BarrenLand()
        aBarren.setCoordinates([0, 0, 1, 2] as int[])

        when:"Create a FarmField,and add the Barren land to it," +
                "Then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aFarmField.addBarrenLand(aBarren)
        aCalculator.addField(aFarmField)

        then:"We should receive an exception"
        ArrayIndexOutOfBoundsException e = thrown()
        e.message =="There is no more fertile land."
    }
}
