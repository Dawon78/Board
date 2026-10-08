<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="row justify-content-center">
    <div class="col-md-5">
        <h3 class="mb-4">로그인</h3>

        <c:if test="${param.error != null}">
            <div class="alert alert-danger">아이디 또는 비밀번호가 올바르지 않습니다.</div>
        </c:if>
        <c:if test="${param.logout != null}">
            <div class="alert alert-info">로그아웃되었습니다.</div>
        </c:if>
        <c:if test="${param.signup != null}">
            <div class="alert alert-success">회원가입이 완료되었습니다. 로그인해 주세요.</div>
        </c:if>

        <form action="/login" method="post">
            <sec:csrfInput/>
            <div class="mb-3">
                <label class="form-label" for="username">아이디</label>
                <input class="form-control" id="username" name="username" required>
            </div>
            <div class="mb-3">
                <label class="form-label" for="password">비밀번호</label>
                <input class="form-control" type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn btn-primary w-100">로그인</button>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>