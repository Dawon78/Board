<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="row justify-content-center">
    <div class="col-md-5">
        <h3 class="mb-4">회원가입</h3>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger"><c:out value="${errorMessage}"/></div>
        </c:if>

        <form:form modelAttribute="signupForm" action="/signup" method="post">
            <div class="mb-3">
                <label class="form-label" for="username">아이디</label>
                <form:input path="username" cssClass="form-control"/>
                <form:errors path="username" cssClass="text-danger small"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="password">비밀번호</label>
                <form:password path="password" cssClass="form-control"/>
                <div class="form-text">영문, 숫자, 특수문자를 포함해 8~20자</div>
                <form:errors path="password" cssClass="text-danger small"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="passwordConfirm">비밀번호 확인</label>
                <form:password path="passwordConfirm" cssClass="form-control"/>
                <form:errors path="passwordConfirm" cssClass="text-danger small"/>
            </div>
            <div class="mb-3">
                <label class="form-label" for="nickname">닉네임</label>
                <form:input path="nickname" cssClass="form-control"/>
                <form:errors path="nickname" cssClass="text-danger small"/>
            </div>
            <button type="submit" class="btn btn-primary w-100">가입하기</button>
        </form:form>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>