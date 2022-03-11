package com.dmitrylee.webapp.model;

import java.util.List;

public class Organization {
    private final String title;
    private final String url;
    private final List<Experience> elementList;

    public Organization(String title, String url, List<Experience> elementList) {
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

    public List<Experience> getElementList() {
        return elementList;
    }
}

