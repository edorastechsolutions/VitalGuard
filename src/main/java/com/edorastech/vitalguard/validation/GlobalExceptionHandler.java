package com.edorastech.vitalguard.validation;

public class GlobalExceptionHandler {

    public static void handle(Exception ex) {

        System.out.println("\n[SECURITY BLOCK]");
        System.out.println("Request denied for safety.");

    }
}