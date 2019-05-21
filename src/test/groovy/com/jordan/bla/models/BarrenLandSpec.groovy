package com.jordan.bla.models

import spock.lang.Specification

class BarrenLandSpec extends Specification{

    def 'test getCoordinates' (){
        when:
        def aBarren = new BarrenLand()
        aBarren.setCoordinates([4, 5, 6, 7] as int[])

        then:
        aBarren.getCoordinates() == ([4, 5, 6, 7] as int[])
    }
}
