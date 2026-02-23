package com.edorastech.vitalguard.model;

public enum SeverityLevel {
    MILD(1),
    MODERATE(2),
    SEVERE(3);

    private final int points;

    SeverityLevel(int points){
        this.points = points;
    }

    public int getPoints(){
        return points;
    }
}
