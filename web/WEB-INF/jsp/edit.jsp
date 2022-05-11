<%@ page import="com.dmitrylee.webapp.model.ContactType" %>
<%@ page import="com.dmitrylee.webapp.model.SectionType" %>
<%@ page import="com.dmitrylee.webapp.model.OrganizationSection" %>
<%@ page import="com.dmitrylee.webapp.model.ListSection" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.dmitrylee.webapp.util.HtmlUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/light.css">
    <link rel="stylesheet" href="css/resume-edit.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="icon" href="img/fav.png">
    <jsp:useBean id="resume" type="com.dmitrylee.webapp.model.Resume" scope="request"/>
    <jsp:useBean id="action" type="java.lang.String" scope="request"></jsp:useBean>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<form method="post" action="resume" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="uuid" value="${resume.uuid}">
    <input type="hidden" name="action" value="${action}">
    <div class="scrollable-panel">
        <div class="form-wrapper">
            <div class="section">Имя</div>
            <input class="field" type="text" name="fullName" size=50 placeholder="ФИО" required
                   pattern="^[^\s]+(\s.*)?$"
                   value="${resume.fullName}">
            <div class="section">Контакты</div>
            <c:forEach var="type" items="<%=ContactType.values()%>">
                <input class="field" type="text" name="${type.name()}" size=30 placeholder="${type.title}"
                       value="${resume.getContact(type)}">
            </c:forEach>
            <div class="spacer"></div>
            <div class="section">Секции</div>
            <c:forEach var="sectionType" items="<%=SectionType.values()%>">
                <jsp:useBean id="sectionType" type="com.dmitrylee.webapp.model.SectionType"></jsp:useBean>
                <div class="field-label">${sectionType.title}</div>
                <c:choose>
                    <c:when test="${sectionType=='PERSONAL' || sectionType=='OBJECTIVE'}">
                        <textarea class="textarea-field"
                                  name="${sectionType.name()}">${resume.getSection(sectionType)}
                        </textarea>
                    </c:when>
                    <c:when test="${sectionType=='ACHIEVEMENT' || sectionType=='QUALIFICATIONS'}">
                        <textarea class="textarea-field"
                                  name="${sectionType.name()}"><%=HtmlUtil.sectionToHtml(sectionType, resume)%>
                        </textarea>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="org"
                                   items="<%=((OrganizationSection) resume.getSection(sectionType)).getOrganizationList()%>"
                                   varStatus="counter">
                            <div class="spacer"></div>
                            <input class="field" type="text" name="${sectionType.name()}" size="75"
                                   placeholder="Название" value="${org.link.name}">
                            <input class="field" type="text" name="${sectionType.name()}url" size="75"
                                   placeholder="Ссылка" value="${org.link.url}">
                            <c:forEach var="exp" items="${org.experienceList}">
                                <jsp:useBean id="exp"
                                             type="com.dmitrylee.webapp.model.Organization.Experience"></jsp:useBean>
                                <div class="date-section">
                                    <input class="field date" size="20"
                                           name="${sectionType}${counter.index}From"
                                           placeholder="Начало ММ/ГГГГ"
                                           value="<%=HtmlUtil.formatDate(exp.getPeriodFrom())%>">
                                    <input class="field date" size="20"
                                           name="${sectionType}${counter.index}To"
                                           placeholder="Окончание ММ/ГГГГ"
                                           value="<%=HtmlUtil.formatDate(exp.getPeriodTo())%>">
                                </div>
                                <input class="field" size="75" name="${sectionType}${counter.index}Title"
                                       value="${exp.title}">
                                <textarea class="textarea-field"
                                          name="${sectionType}${counter.index}Description">${exp.description}
                                </textarea>
                            </c:forEach>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <div class="spacer"></div>
            <div class="button-section">
                <button class="green-submit-button" type="submit">Сохранить</button>
                <button class="red-cancel-button" onclick="window.history.back()" type="reset">Отменить</button>
            </div>
        </div>
    </div>
</form>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>