<%@ page import="com.dmitrylee.webapp.util.HtmlUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link rel="stylesheet" href="css/style.css">
  <jsp:useBean id="resume" type="com.dmitrylee.webapp.model.Resume" scope="request"/>
  <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
  <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/edit.png"></a></h2>
  <p>
    <c:forEach var="contactEntry" items="${resume.contacts}">
      <jsp:useBean id="contactEntry"
                   type="java.util.Map.Entry<com.dmitrylee.webapp.model.ContactType, java.lang.String>"/>
        <%=HtmlUtil.contactToHtml(contactEntry.getKey(), contactEntry.getValue())%><br/>
    </c:forEach>
  </p>
  <p>
    <c:forEach var="sectionEntry" items="${resume.sections}">
      <jsp:useBean id="sectionEntry" type="java.util.Map.Entry<com.dmitrylee.webapp.model.SectionType, com.dmitrylee.webapp.model.AbstractSection>"/>
      <h3><%=sectionEntry.getKey().getTitle()%></h3><br/>
      <%=HtmlUtil.sectionToHtml(sectionEntry.getKey(), sectionEntry.getValue())%>
    </c:forEach>
  </p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>