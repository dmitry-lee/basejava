package com.dmitrylee.webapp.model;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!link.equals(that.link)) return false;
        return experienceList.equals(that.experienceList);
    }

    @Override
    public int hashCode() {
        int result = link.hashCode();
        result = 31 * result + experienceList.hashCode();
        return result;
    }

    public static class Experience implements Serializable{
        private static final long serialVersionUID = 1L;

        private final String title;
        private final YearMonth periodFrom;
        private final YearMonth periodTo;
        private final String description;

        public Experience(String title, YearMonth periodFrom, YearMonth periodTo, String description) {
            Objects.requireNonNull(title, "title must not be null");
            Objects.requireNonNull(periodFrom, "periodFrom must not be null");
            Objects.requireNonNull(periodTo, "periodTo must not be null;");
            this.title = title;
            this.periodFrom = periodFrom;
            this.periodTo = periodTo;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public YearMonth getPeriodFrom() {
            return periodFrom;
        }

        public YearMonth getPeriodTo() {
            return periodTo;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Experience that = (Experience) o;

            if (!title.equals(that.title)) return false;
            if (!periodFrom.equals(that.periodFrom)) return false;
            if (!periodTo.equals(that.periodTo)) return false;
            return description != null ? description.equals(that.description) : that.description == null;
        }

        @Override
        public int hashCode() {
            int result = title.hashCode();
            result = 31 * result + periodFrom.hashCode();
            result = 31 * result + periodTo.hashCode();
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }
    }
}

