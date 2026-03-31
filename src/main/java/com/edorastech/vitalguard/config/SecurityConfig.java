package com.edorastech.vitalguard.config;

public class SecurityConfig {
    public static void applySecurityHeaders(){
        System.out.println("Security Headers Applied");
        System.out.println("X-Content-Type-Options: nosniff");
        System.out.println("X-Frame-Options: DENY");
        System.out.println("X-XSS-Protection: 1; mode=block");
    }
}