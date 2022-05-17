package com.dmitrylee.webapp.web;

import com.dmitrylee.webapp.Config;
import com.dmitrylee.webapp.model.*;
import com.dmitrylee.webapp.storage.Storage;
import com.dmitrylee.webapp.util.HtmlUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init() {
        storage = Config.get().getSQLStorage();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        String action = request.getParameter("action");
        Resume r;
        switch (action) {
            case "add":
                r = new Resume(fullName);
                fillResume(request, fullName, r);
                storage.save(r);
                break;
            case "edit":
                r = storage.get(uuid);
                fillResume(request, fullName, r);
                storage.update(r);
                break;
        }
        response.sendRedirect("resume");
    }

    private void fillResume(HttpServletRequest request, String fullName, Resume r) {
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }
        for (SectionType type : SectionType.values()) {
            String value = request.getParameter(type.name());
            String[] values = request.getParameterValues(type.name());
            if (value != null && (value.trim().length() != 0 || values.length > 1)) {
                switch (type) {
                    case PERSONAL:
                    case OBJECTIVE:
                        r.addSection(type, new TextSection(value));
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        r.addSection(type, new ListSection(Arrays.stream(value.split("\n")).
                                filter(item -> !(HtmlUtil.isEmpty(item))).
                                collect(Collectors.toList())));
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(type.name() + "url");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!HtmlUtil.isEmpty(name)) {
                                List<Organization.Experience> positions = new ArrayList<>();
                                String pfx = type.name() + i;
                                String[] startDates = request.getParameterValues(pfx + "From");
                                String[] endDates = request.getParameterValues(pfx + "To");
                                String[] titles = request.getParameterValues(pfx + "Title");
                                String[] descriptions = request.getParameterValues(pfx + "Description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!HtmlUtil.isEmpty(titles[j])) {
                                        positions.add(new Organization.Experience(
                                                titles[j],
                                                HtmlUtil.parseDate(startDates[j]),
                                                HtmlUtil.parseDate(endDates[j]),
                                                descriptions[j]));
                                    }
                                }
                                orgs.add(new Organization(name, urls[i], positions));
                            }
                        }
                        if (orgs.size() > 0){
                            r.getSections().put(type, new OrganizationSection(orgs));
                        }
                        break;
                }

            } else {
                r.getSections().remove(type);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
                r = storage.get(uuid);
                break;
            case "edit":
                r = storage.get(uuid);
                for (SectionType type : SectionType.values()) {
                    AbstractSection section = r.getSection(type);
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL:
                            if (section == null) {
                                section = TextSection.BLANK;
                            }
                            break;
                        case ACHIEVEMENT:
                        case QUALIFICATIONS:
                            if (section == null) {
                                section = ListSection.BLANK;
                            }
                            break;
                        case EXPERIENCE:
                        case EDUCATION:
                            OrganizationSection orgSection = (OrganizationSection) section;
                            List<Organization> emptyFirstOrganizations = new ArrayList<>();
                            emptyFirstOrganizations.add(Organization.BLANK);
                            if (orgSection != null) {
                                for (Organization org : orgSection.getOrganizationList()) {
                                    List<Organization.Experience> emptyFirstPositions = new ArrayList<>();
                                    emptyFirstPositions.add(Organization.Experience.BLANK);
                                    emptyFirstPositions.addAll(org.getExperienceList());
                                    Link link = org.getLink();
                                    emptyFirstOrganizations.add(new Organization(link.getName(), link.getUrl(), emptyFirstPositions));
                                }
                            }
                            section = new OrganizationSection(emptyFirstOrganizations);
                            break;
                    }
                    r.getSections().put(type, section);
                }
                break;
            case "add":
                r = Resume.BLANK;
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.setAttribute("action", action);
        request.getRequestDispatcher(
                ("view".equals(action) ? "jsp/view.jsp" : "jsp/edit.jsp")
        ).forward(request, response);
    }
}
