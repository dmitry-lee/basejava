package com.dmitrylee.webapp.model;

import java.time.YearMonth;
import java.util.Objects;

public class Experience {
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
