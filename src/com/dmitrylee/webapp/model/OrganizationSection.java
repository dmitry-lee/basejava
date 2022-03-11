package com.dmitrylee.webapp.model;

import java.util.List;

public class OrganizationSection extends AbstractSection {

    private final List<Organization> organizationList;

    public OrganizationSection(List<Organization> elementList) {
        this.organizationList = elementList;
    }

    public List<Organization> getSectionElementList() {
        return organizationList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Organization organization : organizationList) {
            sb.append(organization.getTitle()).append(" ").append(organization.getUrl()).append("\n");
            for (Experience experience : organization.getElementList()) {
                sb.append(experience.getPeriodFrom())
                        .append(" - ")
                        .append(experience.getPeriodTo())
                        .append(" ")
                        .append(experience.getTitle());
                if (!experience.getDescription().equals("")) {
                    sb.append(experience.getDescription());
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
