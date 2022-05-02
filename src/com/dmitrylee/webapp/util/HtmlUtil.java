package com.dmitrylee.webapp.util;

import com.dmitrylee.webapp.model.*;

public class HtmlUtil {

    public static String contactToHtml(ContactType contactType, String value) {
        switch (contactType) {
            case TELEPHONE:
                return contactType.getTitle() + ": " + value;
            case SKYPE:
                return contactType.getTitle() + ": " + toLink("skype:" + value, value);
            case EMAIL:
                return contactType.getTitle() + ": " + toLink("mailto:" + value, value);
            case LINKEDIN:
            case GITHUB:
            case STACKOVERFLOW:
            case HOMEPAGE:
                return toLink(value, contactType.getTitle());
        }
        throw new IllegalStateException("contact type " + contactType + " is invalid");
    }

    public static String toLink(String href, String title) {
        return "<a href='" + href + "'>" + title + "</a>";
    }

    public static String sectionToHtml(SectionType sectionType, AbstractSection section) {
        switch (sectionType) {
            case PERSONAL:
            case OBJECTIVE:
                return ((TextSection) section).getText();
            case ACHIEVEMENT:
            case QUALIFICATIONS:
            case EXPERIENCE:
            case EDUCATION:
                return section.toString().replace("\n", "<br/>");
            default: return "";
        }
    }
}

