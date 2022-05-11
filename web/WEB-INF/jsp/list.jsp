<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.dmitrylee.webapp.model.ContactType" %>
<%@ page import="com.dmitrylee.webapp.util.HtmlUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/light.css">
    <link rel="stylesheet" href="css/styles.css">
    <link rel="stylesheet" href="css/resume-list.css">
    <link rel="icon" href="img/fav.png">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="/jsp/fragments/header.jsp"/>
<div class="scrollable-panel">
    <div class="table-wrapper">
        <div class="add-resume">
            <a class="no-underline-anchor" href="resume?action=add"><img src="img/add.png"></a>
            <a class="text-anchor" href="resume?action=add">
                <p class="add-resume-title">Добавить резюме</p>
            </a>
        </div>
        <div class="resumes-list">
            <table>
                <tbody>
                <tr class="t-header">
                    <th class="name-column">Имя</th>
                    <th class="info-column">Email</th>
                    <th class="img-column">Удалить</th>
                    <th class="img-column">Изменить</th>
                </tr>
                <c:forEach items="${resumes}" var="resume">
                    <jsp:useBean id="resume" type="com.dmitrylee.webapp.model.Resume"/>
                    <tr class="t-body">
                        <td class="name-column">
                            <a class="contact-link" href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a>
                        </td>
                        <td class="name-column">
                            <%=HtmlUtil.contactToHtml(ContactType.EMAIL, resume.getContact(ContactType.EMAIL))%>
                        </td>
                        <td class="img-column">
                            <a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"></a>
                        </td>
                        <td class="img-column">
                            <a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/edit.png"></a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/jsp/fragments/footer.jsp"/>
</body>
</html>