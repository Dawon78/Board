<%@ page pageEncoding="UTF-8" %>
</main>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    $(function () {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function (e, xhr) {
            xhr.setRequestHeader(header, token);
        });

        $("form").on("submit", function (e) {
            if (e.isDefaultPrevented()) {
                return;
            }
            $(this).find("[type=submit]").prop("disabled", true);
        });
    });
</script>
</body>
</html>