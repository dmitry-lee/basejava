package com.dmitrylee.webapp.web;

import com.dmitrylee.webapp.Config;
import com.dmitrylee.webapp.model.*;
import com.dmitrylee.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

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
                r = new Resume(uuid, fullName);
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
            if (value != null && value.trim().length() != 0
                    && !value.equals(SectionType.ACHIEVEMENT.name()) && !value.equals(SectionType.EDUCATION.name())) {
                addSection(r, type, value);
            } else {
                r.getSections().remove(type);
            }
        }
    }

    private void addSection(Resume r, SectionType type, String value) {
        switch (type) {
            case PERSONAL:
            case OBJECTIVE:
                r.addSection(type, new TextSection(value));
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                r.addSection(type, new ListSection(Collections.singletonList(String.join("\n", value))));
                break;
            case EXPERIENCE:
                break;
            case EDUCATION:
                break;
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
            case "edit":
                r = storage.get(uuid);
                break;
            case "add":
                r = new Resume("");
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.setAttribute("action", action);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/jsp/view.jsp" : "/jsp/edit.jsp")
        ).forward(request, response);
    }

    private String getResumesTableHtml(Storage storage) {
        StringBuilder html = new StringBuilder();
        html.append("<table><tr><th>UUID</th><th>FULL NAME</th></tr>");
        for (Resume r : storage.getAllSorted()) {
            html.append("<tr><td>").
                    append(r.getUuid()).append("</td>").
                    append("<td>").append(r.getFullName()).
                    append("</td></tr>");
        }
        html.append("</table>");
        return html.toString();
    }
}
