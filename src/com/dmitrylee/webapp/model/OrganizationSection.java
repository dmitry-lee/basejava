package com.dmitrylee.webapp.model;

import java.util.List;

public class OrganizationSection extends AbstractSection {

    private final List<Organization> organizationList;

    public OrganizationSection(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Organization organization : organizationList) {
            sb.append(organization.getLink().getName()).append(" ").append(organization.getLink().getUrl()).append("\n");
            for (Experience experience : organization.getExperienceList()) {
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
