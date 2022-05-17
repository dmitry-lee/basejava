<%@ page import="com.dmitrylee.webapp.util.HtmlUtil" %>
<%@ page import="com.dmitrylee.webapp.model.TextSection" %>
<%@ page import="com.dmitrylee.webapp.model.ListSection" %>
<%@ page import="com.dmitrylee.webapp.model.OrganizationSection" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/light.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/resume-view.css">
    <link rel="icon" href="img/fav.png">
    <jsp:useBean id="resume" type="com.dmitrylee.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<div class="scrollable-panel">
    <div class="form-wrapper">
        <div class="full-name">
            ${resume.fullName}<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/edit.png"></a>
        </div>
        <div class="contacts">
            <c:forEach var="contactEntry" items="${resume.contacts}">
                <jsp:useBean id="contactEntry"
                             type="java.util.Map.Entry<com.dmitrylee.webapp.model.ContactType, java.lang.String>"/>
                <%=HtmlUtil.contactToHtml(contactEntry.getKey(), contactEntry.getValue())%><br/>
            </c:forEach>
        </div>
        <div class="spacer"></div>
        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.dmitrylee.webapp.model.SectionType, com.dmitrylee.webapp.model.AbstractSection>"/>
            <c:set var="type" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="com.dmitrylee.webapp.model.AbstractSection"/>
            <div class="section">${type.title}</div>
            <c:choose>
                <c:when test="${type=='OBJECTIVE'}">
                    <div class="position"><%=((TextSection) section).getText()%>
                    </div>
                </c:when>
                <c:when test="${type=='PERSONAL'}">
                    <div class="qualities"><%=((TextSection) section).getText()%>
                    </div>
                </c:when>
                <c:when test="${type=='QUALIFICATIONS' || type=='ACHIEVEMENT'}">
                    <ul class="list">
                        <c:forEach var="item" items="<%=((ListSection) section).getList()%>">
                            <li>${item}</li>
                        </c:forEach>
                    </ul>
                </c:when>
                <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                    <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizationList()%>">
                        <div class="section-wrapper">
                            <c:choose>
                                <c:when test="${empty org.link.url}">
                                    <div class="job-name">${org.link.name}</div>
                                </c:when>
                                <c:otherwise>
                                    <div class="job-name"><a class="contact-link"
                                                             href="${org.link.url}">${org.link.name}</a></div>
                                </c:otherwise>
                            </c:choose>
                            <c:forEach var="position" items="${org.experienceList}">
                                <jsp:useBean id="position" type="com.dmitrylee.webapp.model.Organization.Experience"/>
                                <div class="period-position">
                                    <div class="period"><%=HtmlUtil.formatDate(position)%>
                                    </div>
                                    <div class="position">${position.title}</div>
                                </div>
                                <c:choose>
                                    <c:when test="${empty position.description}">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="description">${position.description}</div>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                    </c:forEach>
                </c:when>
            </c:choose>
        </c:forEach>
        <div class="footer-spacer"></div>
    </div>
</div>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>