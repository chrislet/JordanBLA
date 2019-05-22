package com.jordan.bla.models

import com.jordan.bla.services.Boundaries
import spock.lang.Specification

class FarmFieldSpec extends Specification {

    def 'test createFarmFieldArray' (){
        given:"A FarmField of size 2 x 3"
        def boundary = new Boundaries()
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2

        when:"You initialize the FarmField"
        aFarmField.createFarmFieldArray()

        then:"The entire FarmField should be Fertile"
        aFarmField.farmFieldArray[0][0] == LandState.Fertile
        aFarmField.farmFieldArray[0][1] == LandState.Fertile
        aFarmField.farmFieldArray[0][2] == LandState.Fertile
        aFarmField.farmFieldArray[1][0] == LandState.Fertile
        aFarmField.farmFieldArray[1][1] == LandState.Fertile
        aFarmField.farmFieldArray[1][2] == LandState.Fertile
    }

    def 'test createPartialFarmFieldArray' (){
        given:"A FarmField of size 2 x 3, with the lowerYbound being 1"
        def boundary = new Boundaries()
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 1
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2

        when:"You initialize the FarmField"
        aFarmField.createFarmFieldArray()

        then:"The state of the FarmField should be Barren where Y = 0, and Fertile elsewhere"
        aFarmField.farmFieldArray[0][0] == LandState.Barren
        aFarmField.farmFieldArray[0][1] == LandState.Fertile
        aFarmField.farmFieldArray[0][2] == LandState.Fertile
        aFarmField.farmFieldArray[1][0] == LandState.Barren
        aFarmField.farmFieldArray[1][1] == LandState.Fertile
        aFarmField.farmFieldArray[1][2] == LandState.Fertile
    }

    def 'test addBarrenLand' (){
        given:"A FarmField of size 2 x 3, and a piece of BarrenLand," +
                "Set points ( 0 , 1 ) and ( 1 , 2 ) for the BarrenLand"
        def boundary = new Boundaries()
        def aFarmField = new FarmField(boundary)
        aFarmField.lowerXbound = 0
        aFarmField.lowerYbound = 0
        aFarmField.upperXbound = 1
        aFarmField.upperYbound = 2
        def aBarren = new BarrenLand()
        aBarren.setCoordinates([0, 1, 1, 2] as int[])

        when:"Create a FarmField,and add the Barren land to it"
        aFarmField.createFarmFieldArray()
        aFarmField.addBarrenLand(aBarren)

        then:"The state of the FarmField should be Fertile at ( 0 , 0 ) and ( 0 , 1 ), and Barren elsewhere"
        aFarmField.farmFieldArray[0][0] == LandState.Fertile
        aFarmField.farmFieldArray[0][1] == LandState.Barren
        aFarmField.farmFieldArray[0][2] == LandState.Barren
        aFarmField.farmFieldArray[1][0] == LandState.Fertile
        aFarmField.farmFieldArray[1][1] == LandState.Barren
        aFarmField.farmFieldArray[1][2] == LandState.Barren


    }
}
