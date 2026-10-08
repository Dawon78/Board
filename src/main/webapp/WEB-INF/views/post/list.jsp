<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="m-0">게시판</h3>
    <a href="/posts/new" class="btn btn-primary">글쓰기</a>
</div>

<table class="table table-hover">
    <thead>
    <tr>
        <th style="width: 80px">번호</th>
        <th>제목</th>
        <th style="width: 140px">작성자</th>
        <th style="width: 160px">작성일</th>
        <th style="width: 80px">좋아요</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="post" items="${posts}">
        <tr>
            <td>${post.id}</td>
            <td>
                <a href="/posts/${post.id}" class="text-decoration-none"><c:out value="${post.title}"/></a>
                <c:if test="${post.edited}"><span class="text-muted small">(수정됨)</span></c:if>
            </td>
            <td><c:out value="${post.nickname}"/></td>
            <td>${post.createdAtText}</td>
            <td>${post.likeCount}</td>
        </tr>
    </c:forEach>
    <c:if test="${empty posts}">
        <tr><td colspan="5" class="text-center text-muted py-5">게시글이 없습니다.</td></tr>
    </c:if>
    </tbody>
</table>

<nav>
    <ul class="pagination justify-content-center">
        <c:forEach var="p" begin="1" end="${totalPages}">
            <li class="page-item ${p == currentPage ? 'active' : ''}">
                <a class="page-link" href="/posts?page=${p}">${p}</a>
            </li>
        </c:forEach>
    </ul>
</nav>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>