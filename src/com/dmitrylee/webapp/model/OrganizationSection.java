package com.dmitrylee.webapp.model;

import java.util.List;

public class OrganizationSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private List<Organization> organizationList;

    public OrganizationSection() {
    }

    public OrganizationSection(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationSection that = (OrganizationSection) o;

        return organizationList.equals(that.organizationList);
    }

    @Override
    public int hashCode() {
        return organizationList.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Organization organization : organizationList) {
            sb.append(organization.getLink().getName()).append(" ").append(organization.getLink().getUrl()).append("\n");
            for (Organization.Experience experience : organization.getExperienceList()) {
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
