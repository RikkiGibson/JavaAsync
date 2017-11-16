package com.microsoft.azure.storage.pipeline;

public enum LogLevel {

    OFF(0),

    FATAL(1),

    ERROR(2),

    WARNING(3),

    INFO(4);


    private final int id;
    LogLevel(int id) { this.id = id; }
    int getValue() { return id; }
}
