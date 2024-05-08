package com.restropos.systemorder.constants;

public enum ReviewStar {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);


    private final int number;

    ReviewStar(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
