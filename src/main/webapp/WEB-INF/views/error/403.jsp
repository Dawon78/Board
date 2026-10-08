<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="text-center py-5">
    <h3>권한이 없습니다</h3>
    <p class="text-muted">작성자만 수정·삭제할 수 있습니다.</p>
    <a href="/posts" class="btn btn-primary">목록으로</a>
</div>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>