package com.jordan.bla.services

import com.jordan.bla.models.BLAExceptions
import spock.lang.Specification

class InputParserSpec extends Specification {
    def 'test parseInputExit'(){
        given:"Set input as 'exit'"
        def stringInput = "exit"
        def boundary = new Boundaries()
        def myInterpreter = new InputParser(boundary)

        when:"Call parseInput"
        def dataPointsInt = myInterpreter.parseInput(stringInput)

        then:"Expect a 0 length integer array to be returned"
        dataPointsInt.length == 0
    }

    def 'test parseInputNotMultipleOfFour'(){
        given:"Set input as '1 2 3 4 5 6 7 8 9'"
        def stringInput = "1 2 3 4 5 6 7 8 9"
        def boundary = new Boundaries()
        def myInterpreter = new InputParser(boundary)

        when:"Call parseInput"
        myInterpreter.parseInput(stringInput)

        then:"Expect an exception"
        BLAExceptions.InsufficientDataPointsException e = thrown()
        e.message =="You gave me 9 point(s)."
    }
    def 'test parseInputXCoordOutOfBounds'(){
        given:"Set input as '1 2 300 4'"
        def stringInput = "1 2 300 4"
        def boundary = new Boundaries()
        def myInterpreter = new InputParser(boundary)
        myInterpreter.lowerXbound = 0
        myInterpreter.lowerYbound = 0
        myInterpreter.upperXbound = 10
        myInterpreter.upperYbound = 20

        when:"Call parseInput"
        myInterpreter.parseInput(stringInput)

        then:"Expect an exception"
        BLAExceptions.LandOutOfBoundsException e = thrown()
        e.message =="x coordinates out of bounds 300"
    }
    def 'test parseInputYCoordOutOfBounds'(){
        given:"Set input as '1 2 3 400'"
        def stringInput = "1 2 3 400"
        def boundary = new Boundaries()
        def myInterpreter = new InputParser(boundary)
        myInterpreter.lowerXbound = 0
        myInterpreter.lowerYbound = 0
        myInterpreter.upperXbound = 10
        myInterpreter.upperYbound = 20

        when:"Call parseInput"
        myInterpreter.parseInput(stringInput)

        then:"Expect an exception"
        BLAExceptions.LandOutOfBoundsException e = thrown()
        e.message =="y coordinates out of bounds 400"
    }
    def 'test parseInputBottomLeftToTopRight'(){
        given:"Set input as '3 4 1 2'"
        def stringInput = "3 4 1 2"
        def boundary = new Boundaries()
        def myInterpreter = new InputParser(boundary)
        myInterpreter.lowerXbound = 0
        myInterpreter.lowerYbound = 0
        myInterpreter.upperXbound = 10
        myInterpreter.upperYbound = 20

        when:"Call parseInput"
        myInterpreter.parseInput(stringInput)

        then:"Expect an exception"
        BLAExceptions.LandInputOutOfOrderException e = thrown()
        e.message =="Data points out of order 3 4 1 2"
    }
    def 'test parseInput'(){
        given:"Set input as '1 2 3 4'"
        def stringInput = "1 2 3 4"
        def boundary = new Boundaries()
        def myInterpreter = new InputParser(boundary)
        myInterpreter.lowerXbound = 0
        myInterpreter.lowerYbound = 0
        myInterpreter.upperXbound = 10
        myInterpreter.upperYbound = 20

        when:"Call parseInput"
        def dataPointsInt = myInterpreter.parseInput(stringInput)

        then:"Expect a response"
        dataPointsInt[0] == 1
        dataPointsInt[1] == 2
        dataPointsInt[2] == 3
        dataPointsInt[3] == 4
    }
    def 'test parseInputMultipleCharacters'(){
        given:"Set input with multiple characters surrounding '1 2 3 4'"
        def stringInput = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                "1incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                "2ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit " +
                "3in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat " +
                "4non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
        def boundary = new Boundaries()
        def myInterpreter = new InputParser(boundary)
        myInterpreter.lowerXbound = 0
        myInterpreter.lowerYbound = 0
        myInterpreter.upperXbound = 10
        myInterpreter.upperYbound = 20

        when:"Call parseInput"
        def dataPointsInt = myInterpreter.parseInput(stringInput)

        then:"Expect a response"
        dataPointsInt[0] == 1
        dataPointsInt[1] == 2
        dataPointsInt[2] == 3
        dataPointsInt[3] == 4
    }

}
