package com.example.backend.model;

public enum StatusAffaire {
    EN_CREATION,
    CDP_DECIDE,
    EN_PRODUCTION,
    INTERROMPU,
    TERMINE,
    ANNULE;

    public static final StatusAffaire DEFAULT = EN_CREATION;
}