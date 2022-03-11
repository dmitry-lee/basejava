package com.dmitrylee.webapp.model;

import java.time.YearMonth;

public class Experience {
    private final String title;
    private final YearMonth periodFrom;
    private final YearMonth periodTo;
    private final String description;

    public Experience(String title, YearMonth period, YearMonth periodTo, String description) {
        this.title = title;
        this.periodFrom = period;
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
}
