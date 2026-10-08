<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf_header" content="${_csrf.headerName}">
    <title>게시판</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>
<nav class="navbar bg-body-tertiary mb-4">
    <div class="container">
        <a class="navbar-brand" href="/">게시판</a>
        <div class="d-flex align-items-center gap-2">
            <sec:authorize access="isAuthenticated()">
                <span><sec:authentication property="principal.nickname" htmlEscape="true"/>님</span>
                <form action="/logout" method="post" class="m-0">
                    <sec:csrfInput/>
                    <button type="submit" class="btn btn-outline-secondary btn-sm">로그아웃</button>
                </form>
            </sec:authorize>
            <sec:authorize access="isAnonymous()">
                <a class="btn btn-outline-primary btn-sm" href="/login">로그인</a>
                <a class="btn btn-primary btn-sm" href="/signup">회원가입</a>
            </sec:authorize>
        </div>
    </div>
</nav>
<main class="container">