package com.dmitrylee.webapp.model;

import java.util.List;

public class SectionElement {
    private final String title;
    private final String url;
    private final List<ExpEduElement> elementList;

    public SectionElement(String title, String url, List<ExpEduElement> elementList) {
        this.title = title;
        this.url = url;
        this.elementList = elementList;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public List<ExpEduElement> getElementList() {
        return elementList;
    }
}

