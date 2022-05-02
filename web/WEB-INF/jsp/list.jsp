<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.dmitrylee.webapp.model.ContactType" %>
<%@ page import="com.dmitrylee.webapp.util.HtmlUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <title>Список всех резюме</title>
</head>
<body>
<jsp:include page="/jsp/fragments/header.jsp"/>
<section>
    <p class="p1"><a href="resume?action=add"><img src="img/add.png"></a></p>
    <table border="1" cellpadding="8" cellspacing="0">
        <tr>
            <th>Имя</th>
            <th>Email</th>
            <th></th>
            <th></th>
        </tr>
        <c:forEach items="${resumes}" var="resume">
            <jsp:useBean id="resume" type="com.dmitrylee.webapp.model.Resume"/>
            <tr>
                <td><a href="resume?uuid=${resume.uuid}&action=view">${resume.fullName}</a></td>
                <td><%=HtmlUtil.contactToHtml(ContactType.EMAIL, resume.getContact(ContactType.EMAIL))%></td>
                <td><a href="resume?uuid=${resume.uuid}&action=delete"><img src="img/delete.png"></a></td>
                <td><a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/edit.png"></a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<jsp:include page="/jsp/fragments/footer.jsp"/>
</body>
</html>