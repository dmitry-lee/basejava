package com.dmitrylee.webapp.util;

import com.dmitrylee.webapp.model.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class HtmlUtil {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    public static String contactToHtml(ContactType contactType, String value) {
        switch (contactType) {
            case TELEPHONE:
                return contactType.getTitle() + ": " + value;
            case SKYPE:
                return contactType.getTitle() + ": " + toLink("skype:" + value, value);
            case EMAIL:
                return toLink("mailto:" + value, value);
            case LINKEDIN:
            case GITHUB:
            case STACKOVERFLOW:
            case HOMEPAGE:
                return toLink(value, contactType.getTitle());
        }
        throw new IllegalStateException("contact type " + contactType + " is invalid");
    }

    public static String toLink(String href, String title) {
        return "<a class=\"contact-link\" href='" + href + "'>" + title + "</a>";
    }

    public static String sectionToHtml(SectionType sectionType, AbstractSection section) {
        StringBuilder sb = new StringBuilder();
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                return sb.append("<div class=\"qualities\"").append(((TextSection) section).getText()).append("</div>").toString();
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                return section.toString();
            case EXPERIENCE:
            case EDUCATION:
                sb.append("<div class=\"section-wrapper\"");
                for (Organization o : ((OrganizationSection) section).getOrganizationList()) {
                    Link link = o.getLink();
                    sb.append("<div class=\"job-name\"<a class=\"contact-link\" href=").append(link.getUrl()).append(">").append(link.getName()).append("</a></div>");
                    for (Organization.Experience e : o.getExperienceList()) {
                        sb.append("<div class=\"period-position\"><div class=\"period\">").
                                append(formatDate(e)).append("</div>");
                        sb.append("<div class=\"position\">").append(e.getTitle()).append("</div></div>");
                        sb.append("<div class=\"description\">").append(e.getDescription()).append("</div>");
                    }
                }
                sb.append("</div>");
                return sb.toString();
            default:
                return "";
        }
    }

    public static String sectionToHtml(SectionType type, Resume resume) {
        AbstractSection section = resume.getSection(type);
        return section != null ? sectionToHtml(type, section) : "";
    }

    public static String formatDate(Organization.Experience experience) {
        return experience == null ? "" : formatDate(experience.getPeriodFrom()) + " - " + formatDate(experience.getPeriodTo());
    }

    public static String formatDate(YearMonth date) {
        return date == null ? "" : date.format(FORMATTER);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static YearMonth parseDate(String date) {
        return isEmpty(date) ? YearMonth.of(3000, 1) : YearMonth.parse(date, FORMATTER);
    }
}

