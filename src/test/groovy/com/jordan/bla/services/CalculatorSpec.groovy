package com.jordan.bla.services

import com.jordan.bla.models.BLAExceptions
import com.jordan.bla.models.BarrenLand
import com.jordan.bla.models.FarmField
import com.jordan.bla.models.LandState
import spock.lang.Specification

class CalculatorSpec extends Specification{
    def 'test findNextFertileLand' (){
        given:"A FarmField of size 2 x 3, and a piece of BarrenLand," +
                "Set points ( 0 , 0 ) and ( 1 , 1 ) for the BarrenLand"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
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
                "Set points ( 0 , 0 ) and ( 1 , 2 ) for the BarrenLand" +
                "Making the whole FarmField Barren"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2
        def aBarren = new BarrenLand()
        aBarren.setCoordinates([0, 0, 1, 2] as int[])

        when:"Create a FarmField,and add the Barren land to it," +
                "Then add the FarmField to our Calculator" +
                "And attempt to find the next Fertile land"
        aFarmField.createFarmFieldArray()
        aFarmField.addBarrenLand(aBarren)
        aCalculator.addField(aFarmField)
        aCalculator.findNextFertileLand()

        then:"We should receive an exception"
        ArrayIndexOutOfBoundsException e = thrown()
        e.message =="There is no more fertile land."
    }

    def 'test findVisitedTouchingFertile'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField, mark one point as visited" +
                "Then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aFarmField.farmFieldArray[1][1] = LandState.Visited
        aCalculator.addField(aFarmField)

        then:"Attempt to Find our point ( 1 , 1 )"
        aCalculator.findVisitedTouchingFertile() == ([1, 1] as int[])
    }

    def 'test findVisitedTouchingFertileAllBarren'(){
        given:"A FarmField of size 2 x 3" +
                "With a Barren object the size of the FarmField"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2
        def aBarren = new BarrenLand()
        aBarren.setCoordinates([0, 0, 1, 2] as int[])

        when:"Create a FarmField, add our Barren object to the FarmField" +
                "Mark one point as visited" +
                "Then add the FarmField to our Calculator" +
                "Attempt to find our point"
        aFarmField.createFarmFieldArray()
        aFarmField.addBarrenLand(aBarren)
        aFarmField.farmFieldArray[1][1] = LandState.Visited
        aCalculator.addField(aFarmField)
        aCalculator.findVisitedTouchingFertile()

        then:"Expect an exception"
        BLAExceptions.DoneVisitingException ex = thrown()
        ex.message =="Done visiting."
    }

    def 'test CalculateExample1'() {
        given:
        "A FarmField of size 400 x 600" +
                "With a Barren object with the following coordinates" +
                "{“0 292 399 307”}"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 399
        aFarmField.upperYbound = 599
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 399
        aCalculator.upperYbound = 599
        def aBarren = new BarrenLand()
        aBarren.setCoordinates([0, 292, 399, 307] as int[])

        when:"Create a FarmField, add our Barren object to the FarmField" +
                "Then add the FarmField to our Calculator" +
                "Calculate our Fertile Land area" +
                "Get our results"
        aFarmField.createFarmFieldArray()
        aFarmField.addBarrenLand(aBarren)
        aCalculator.addField(aFarmField)
        aCalculator.calculate()
        def fertileLands = aCalculator.getFertileLands()

        then:"Get our sorted List of FertileLand objects"
        fertileLands[0].getArea() == 116800
        fertileLands[1].getArea() == 116800
    }
    def 'test CalculateExample2'() {
        given:
        "A FarmField of size 400 x 600" +
                "With 4 Barren objects with the following coordinates" +
                "{“48 192 351 207”, “48 392 351 407”, “120 52 135 547”, “260 52 275 547”} "
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 399
        aFarmField.upperYbound = 599
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 399
        aCalculator.upperYbound = 599
        def aBarren1 = new BarrenLand()
        def aBarren2 = new BarrenLand()
        def aBarren3 = new BarrenLand()
        def aBarren4 = new BarrenLand()
        aBarren1.setCoordinates([48, 192, 351, 207] as int[])
        aBarren2.setCoordinates([48, 392, 351, 407] as int[])
        aBarren3.setCoordinates([120, 52, 135, 547] as int[])
        aBarren4.setCoordinates([260, 52, 275, 547] as int[])

        when:"Create a FarmField, add our Barren object to the FarmField" +
                "Then add the FarmField to our Calculator" +
                "Calculate our Fertile Land area" +
                "Get our results"
        aFarmField.createFarmFieldArray()
        aFarmField.addBarrenLand(aBarren1)
        aFarmField.addBarrenLand(aBarren2)
        aFarmField.addBarrenLand(aBarren3)
        aFarmField.addBarrenLand(aBarren4)
        aCalculator.addField(aFarmField)
        aCalculator.calculate()
        def fertileLands = aCalculator.getFertileLands()

        then:"Get our sorted List of FertileLand objects"
        fertileLands[0].getArea() == 22816
        fertileLands[1].getArea() == 192608
    }

    def 'test CalculateAllBarren'(){
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2
        def aBarren = new BarrenLand()
        aBarren.setCoordinates([0, 0, 1, 2] as int[])
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)


        when:"Create a FarmField,and add the Barren land to it," +
                "Then add the FarmField to our Calculator" +
                "And attempt to calculate"
        aFarmField.createFarmFieldArray()
        aFarmField.addBarrenLand(aBarren)
        aCalculator.addField(aFarmField)
        aCalculator.calculate()

        then:"Expect an error message"
        buffer.toString() == "There is no more fertile land.\r\nDid you make all the land barren?\r\n"
    }

    def 'test lookN'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)
        aCalculator.farmFieldArray[0][1] = LandState.Visited

        then:"From ( 0 , 0 ) looking North, we should see Visited"
        aCalculator.lookN(0,0) == LandState.Visited
    }
    def 'test lookNAtTopOfField'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)

        then:"From ( 0 , 2 ) looking North, we should see OutOfBounds"
        aCalculator.lookN(0,2) == LandState.OutOfBounds
    }
    def 'test lookNFromOutOfBounds'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)

        then:"From ( 4 , 0 ) looking North, we should see OutOfBounds"
        aCalculator.lookN(4,0) == LandState.OutOfBounds
    }
    def 'test lookS'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)
        aCalculator.farmFieldArray[0][1] = LandState.Visited

        then:"From ( 0 , 2 ) looking South, we should see Visited"
        aCalculator.lookS(0,2) == LandState.Visited
    }
    def 'test lookSAtBottomOfField'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)

        then:"From ( 0 , 0 ) looking South, we should see OutOfBounds"
        aCalculator.lookS(0,0) == LandState.OutOfBounds
    }
    def 'test lookSFromOutOfBounds'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)

        then:"From ( 4 , 0 ) looking South, we should see OutOfBounds"
        aCalculator.lookS(4,0) == LandState.OutOfBounds
    }
    def 'test lookE'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)
        aCalculator.farmFieldArray[1][1] = LandState.Visited

        then:"From ( 0 , 1 ) looking East, we should see Visited"
        aCalculator.lookE(0,1) == LandState.Visited
    }
    def 'test lookEAtRightOfField'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)

        then:"From ( 1 , 0 ) looking East, we should see OutOfBounds"
        aCalculator.lookE(1,0) == LandState.OutOfBounds
    }
    def 'test lookEFromOutOfBounds'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)

        then:"From ( 0 , 4 ) looking East, we should see OutOfBounds"
        aCalculator.lookE(0,4) == LandState.OutOfBounds
    }
    def 'test lookW'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)
        aCalculator.farmFieldArray[0][1] = LandState.Visited

        then:"From ( 1 , 1 ) looking West, we should see Visited"
        aCalculator.lookW(1,1) == LandState.Visited
    }
    def 'test lookWAtLeftOfField'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)

        then:"From ( 0 , 0 ) looking West, we should see OutOfBounds"
        aCalculator.lookW(0,0) == LandState.OutOfBounds
    }
    def 'test lookWFromOutOfBounds'(){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aCalculator = new Calculator(boundary)
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        aCalculator.lowerXbound = 0
        aCalculator.lowerYbound = 0
        aCalculator.upperXbound = 1
        aCalculator.upperYbound = 2

        when:"Create a FarmField then add the FarmField to our Calculator"
        aFarmField.createFarmFieldArray()
        aCalculator.addField(aFarmField)

        then:"From ( 0 , 4 ) looking West, we should see OutOfBounds"
        aCalculator.lookN(0,4) == LandState.OutOfBounds
    }

}
