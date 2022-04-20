package com.dmitrylee.webapp.web;

import com.dmitrylee.webapp.Config;
import com.dmitrylee.webapp.model.Resume;
import com.dmitrylee.webapp.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResumeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Storage storage = Config.get().getSQLStorage();
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<head><link rel=\"stylesheet\" href=\"css/style.css\"></head>");
        writer.println("<body>");
        writer.println(getResumesTableHtml(storage));
        writer.println("</body></html>");
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
