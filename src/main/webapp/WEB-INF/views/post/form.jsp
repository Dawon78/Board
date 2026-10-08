<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:choose>
    <c:when test="${not empty postId}">
        <c:set var="formAction" value="/posts/${postId}/edit"/>
        <c:set var="cancelUrl" value="/posts/${postId}"/>
        <c:set var="pageTitle" value="글 수정"/>
    </c:when>
    <c:otherwise>
        <c:set var="formAction" value="/posts"/>
        <c:set var="cancelUrl" value="/posts"/>
        <c:set var="pageTitle" value="글쓰기"/>
    </c:otherwise>
</c:choose>

<h3 class="mb-4">${pageTitle}</h3>

<form:form modelAttribute="postForm" action="${formAction}" method="post">
    <div class="mb-3">
        <label class="form-label" for="title">제목</label>
        <form:input path="title" cssClass="form-control"/>
        <form:errors path="title" cssClass="text-danger small"/>
    </div>
    <div class="mb-3">
        <label class="form-label" for="content">내용</label>
        <form:textarea path="content" cssClass="form-control" rows="12"/>
        <form:errors path="content" cssClass="text-danger small"/>
    </div>
    <div class="d-flex gap-2">
        <button type="submit" class="btn btn-primary">저장</button>
        <a href="${cancelUrl}" class="btn btn-outline-secondary">취소</a>
    </div>
</form:form>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>