package com.dmitrylee.webapp.model;

public class TextSection extends AbstractSection{

    private final String text;

    public TextSection(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
