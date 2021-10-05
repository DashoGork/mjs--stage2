package com.epam.esm.enums;

public enum SortOrder {
    ASC(1),
    DESC(-1);

    private int value;

    SortOrder(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
