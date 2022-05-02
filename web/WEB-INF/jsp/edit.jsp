<%@ page import="com.dmitrylee.webapp.model.ContactType" %>
<%@ page import="com.dmitrylee.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.dmitrylee.webapp.model.Resume" scope="request"/>
    <jsp:useBean id="action" type="java.lang.String" scope="request"></jsp:useBean>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <input type="hidden" name="action" value="${action}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 required pattern="^[^\s]+(\s.*)?$" value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <c:forEach var="sectionType" items="<%=SectionType.values()%>">
<%--            <jsp:useBean id="sectionEntry" type="java.util.Map.Entry<com.dmitrylee.webapp.model.SectionType, com.dmitrylee.webapp.model.AbstractSection>"/>--%>
            <dl>
                <dt>${sectionType.title}</dt>
                <dd><textarea cols="100" rows="5" name="${sectionType.name()}">${resume.getSection(sectionType)}</textarea></dd>
            </dl>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()" type="reset">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>