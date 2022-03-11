package com.dmitrylee.webapp.model;

import java.util.List;

public class ListElementSection extends AbstractSection {

    private final List<SectionElement> sectionElementList;

    public ListElementSection(List<SectionElement> elementList) {
        this.sectionElementList = elementList;
    }

    public List<SectionElement> getSectionElementList() {
        return sectionElementList;
    }

    @Override
    public void printSection() {
        for (SectionElement sectionElement : sectionElementList) {
            System.out.println(sectionElement.getTitle() + " " + sectionElement.getUrl());
            for (ExpEduElement expEduElement : sectionElement.getElementList()) {
                System.out.println(expEduElement.getPeriodFrom() + " - " + expEduElement.getPeriodTo() + " " + expEduElement.getTitle());
                if (!expEduElement.getDescription().equals("")) {
                    System.out.println(expEduElement.getDescription());
                }
            }
        }
    }
}
