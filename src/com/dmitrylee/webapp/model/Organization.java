package com.dmitrylee.webapp.model;

import java.util.List;

public class Organization {
    private final Link link;
    private final List<Experience> experienceList;

    public Organization(String name, String url, List<Experience> experienceList) {
        this.link = new Link(name, url);
        this.experienceList = experienceList;
    }

    public Link getLink() {
        return link;
    }

    public List<Experience> getExperienceList() {
        return experienceList;
    }
}

