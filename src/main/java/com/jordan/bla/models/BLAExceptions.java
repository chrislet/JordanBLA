package com.jordan.bla.models;

public class BLAExceptions {
    public static class LandOutOfBoundsException extends Exception {
        public LandOutOfBoundsException(String message) {
            super(message);
        }
    }

    public static class LandInputOutOfOrderException extends Exception {
        public LandInputOutOfOrderException(String message) {
            super(message);
        }
    }

    public static class InsufficientDataPointsException extends Exception {
        public InsufficientDataPointsException(String message) {
            super(message);
        }
    }

    public static class DoneVisitingException extends Throwable {
        public DoneVisitingException(String message) {
            super(message);
        }
    }

    public static class NoFertileLandSurroundingException extends Throwable {
        public NoFertileLandSurroundingException(String message) {
            super(message);
        }
    }

}
