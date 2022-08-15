package com.swetroye.idgenerator.enums;

/**
 * WorkerNodeType
 * <li>CONTAINER: Such as Docker
 * <li>ACTUAL: Actual machine
 */
public enum WorkerType {
    CONTAINER(0), ACTUAL(1);

    // Lock enum type
    private final int value;

    // Constructor
    private WorkerType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
