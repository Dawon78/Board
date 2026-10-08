<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="mb-3">
    <h3><c:out value="${post.title}"/></h3>
    <div class="text-muted small">
        <c:out value="${post.nickname}"/> · ${post.createdAtText}
        <c:if test="${post.edited}">(수정됨)</c:if>
    </div>
</div>

<hr>
<div class="mb-4" style="white-space: pre-wrap;"><c:out value="${post.content}"/></div>

<div class="mb-4">git add .
    <sec:authorize access="isAuthenticated()">
        <button type="button" id="likeBtn" data-post-id="${post.id}"
                class="btn ${liked ? 'btn-danger' : 'btn-outline-danger'}">
            ♥ <span id="likeCount">${post.likeCount}</span>
        </button>
    </sec:authorize>
    <sec:authorize access="isAnonymous()">
        <a href="/login" class="btn btn-outline-danger">♥ ${post.likeCount}</a>
    </sec:authorize>
</div>

<div class="d-flex gap-2">
    <a href="/posts" class="btn btn-outline-secondary">목록</a>
    <sec:authorize access="isAuthenticated()">
        <sec:authentication property="principal.id" var="loginId"/>
        <c:if test="${loginId == post.memberId}">
            <a href="/posts/${post.id}/edit" class="btn btn-outline-primary">수정</a>
            <form action="/posts/${post.id}/delete" method="post" class="m-0"
                  onsubmit="return confirm('정말 삭제할까요?');">
                <sec:csrfInput/>
                <button type="submit" class="btn btn-outline-danger">삭제</button>
            </form>
        </c:if>
    </sec:authorize>
</div>

<script>
    $(function () {
        $("#likeBtn").on("click", function () {
            const $btn = $(this);
            $btn.prop("disabled", true);
            $.post("/posts/" + $btn.data("postId") + "/like")
                .done(function (res) {
                    $btn.toggleClass("btn-danger", res.liked).toggleClass("btn-outline-danger", !res.liked);
                    $("#likeCount").text(res.count);
                })
                .fail(function () {
                    alert("좋아요 처리 중 오류가 발생했습니다.");
                })
                .always(function () {
                    $btn.prop("disabled", false);
                });
        });
    });
</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>