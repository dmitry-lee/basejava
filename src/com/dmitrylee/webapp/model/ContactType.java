package com.dmitrylee.webapp.model;

public enum ContactType {
    TELEPHONE("Тел."),
    SKYPE("Skype"),
    EMAIL("e-mail"),
    LINKEDIN("Профиль LinkedIn"),
    GITHUB("Профиль Github"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMEPAGE("Домашняя страница");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
