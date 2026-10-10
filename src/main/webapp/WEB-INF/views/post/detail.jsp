<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/header.jsp" %>

<div class="mb-3">
    <h3 class="text-break"><c:out value="${post.title}"/></h3>
    <div class="text-muted small">
        <c:out value="${post.nickname}"/> · ${post.createdAtText}
        <c:if test="${post.edited}">(수정됨)</c:if>
    </div>
</div>

<hr>
<div class="mb-4 text-break" style="white-space: pre-wrap;"><c:out value="${post.content}"/></div>

<div class="mb-4">
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
    <c:url var="listUrl" value="/posts">
        <c:if test="${not empty param.page}">
            <c:param name="page" value="${param.page}"/>
        </c:if>
        <c:if test="${not empty param.keyword}">
            <c:param name="type" value="${param.type}"/>
            <c:param name="keyword" value="${param.keyword}"/>
        </c:if>
    </c:url>
    <a href="${listUrl}" class="btn btn-outline-secondary">목록</a>
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

<hr class="my-4">
<h5 id="comments" class="mb-3">댓글</h5>

<c:if test="${not empty commentError}">
    <div class="alert alert-danger"><c:out value="${commentError}"/></div>
</c:if>

<c:if test="${empty comments}">
    <p class="text-muted">첫 댓글을 남겨보세요.</p>
</c:if>

<c:forEach var="comment" items="${comments}">
    <div id="comment-${comment.id}" class="py-2 border-bottom ${comment.reply ? 'ms-5' : ''}">
        <c:choose>
            <c:when test="${comment.deleted}">
                <div class="text-muted">삭제된 댓글입니다.</div>
            </c:when>
            <c:otherwise>
                <div class="small text-muted">
                    <c:if test="${comment.reply}">↳ </c:if>
                    <strong class="text-body"><c:out value="${comment.nickname}"/></strong> · ${comment.createdAtText}
                    <c:if test="${comment.edited}">(수정됨)</c:if>
                </div>
                <div class="text-break" style="white-space: pre-wrap;"><c:out value="${comment.content}"/></div>

                <sec:authorize access="isAuthenticated()">
                    <div class="mt-1 d-flex gap-2 small">
                        <a href="#" class="reply-toggle" data-target="#reply-form-${comment.id}">답글</a>
                        <c:if test="${loginId == comment.memberId}">
                            <a href="#" class="edit-toggle" data-target="#edit-form-${comment.id}">수정</a>
                            <form action="/posts/${post.id}/comments/${comment.id}/delete" method="post" class="m-0"
                                  onsubmit="return confirm('댓글을 삭제할까요?');">
                                <sec:csrfInput/>
                                <button type="submit" class="btn btn-link btn-sm p-0 text-danger">삭제</button>
                            </form>
                        </c:if>
                    </div>

                    <form id="edit-form-${comment.id}" action="/posts/${post.id}/comments/${comment.id}/edit"
                          method="post" class="mt-2 d-none">
                        <sec:csrfInput/>
                        <textarea name="content" class="form-control mb-2" rows="2"><c:out value="${comment.content}"/></textarea>
                        <button type="submit" class="btn btn-sm btn-primary">수정</button>
                    </form>

                    <form id="reply-form-${comment.id}" action="/posts/${post.id}/comments"
                          method="post" class="mt-2 d-none">
                        <sec:csrfInput/>
                        <input type="hidden" name="parentId" value="${comment.id}">
                        <textarea name="content" class="form-control mb-2" rows="2"
                                  placeholder="답글을 입력하세요"><c:if test="${comment.reply}">@<c:out value="${comment.nickname}"/> </c:if></textarea>
                        <button type="submit" class="btn btn-sm btn-primary">답글 등록</button>
                    </form>
                </sec:authorize>
            </c:otherwise>
        </c:choose>
    </div>
</c:forEach>

<sec:authorize access="isAuthenticated()">
    <form action="/posts/${post.id}/comments" method="post" class="mt-3">
        <sec:csrfInput/>
        <textarea name="content" class="form-control mb-2" rows="3" placeholder="댓글을 입력하세요"></textarea>
        <button type="submit" class="btn btn-primary">댓글 등록</button>
    </form>
</sec:authorize>
<sec:authorize access="isAnonymous()">
    <p class="text-muted mt-3"><a href="/login">로그인</a>하고 댓글을 남겨보세요.</p>
</sec:authorize>

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
                .fail(function (xhr) {
                    if (xhr.status === 401 || xhr.status === 403) {
                        alert("로그인이 필요합니다. 다시 로그인해 주세요.");
                        location.href = "/login";
                        return;
                    }
                    alert("좋아요 처리 중 오류가 발생했습니다.");
                })
                .always(function () {
                    $btn.prop("disabled", false);
                });
        });
    });

    $(".reply-toggle, .edit-toggle").on("click", function (e) {
        e.preventDefault();
        $($(this).data("target")).toggleClass("d-none");
    });

</script>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>