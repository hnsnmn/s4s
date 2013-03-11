<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Uploaded</title>
	<script type="text/javascript" src="<c:url value='/js/jquery-1.5.1.min.js' />"></script>
	<script type="text/javascript">
	<c:if test="${! empty param.domain}">
	document.domain="${param.domain}";
	</c:if>
	<c:if test="${param.p == 't'}">
	$(document).ready(function() {
		parent.uploaded();
	});
	</c:if>
	</script>
</head>
<body>

<form id="uploadResult" name="uploadResult" onsubmit="return false;">
<input type="hidden" name="code" id="code" value="<c:out value='${uploadResult.code}'/>" />
<input type="hidden" name="fileName" id="fileName" value="<c:out value='${uploadResult.fileName}'/>" />
<input type="hidden" name="fileId" id="fileId" value="${uploadResult.fileId}" />
<input type="hidden" name="metadataId" id="metadataId" value="${uploadResult.metadataId}" />
<input type="hidden" name="mimeType" id="mimeType" value="${uploadResult.mimeType}" />
<input type="hidden" name="length" id="length" value="${uploadResult.length}" />
<input type="hidden" name="fileUrl" id="fileUrl" value="${uploadResult.fileUrl}" />
<input type="hidden" name="imageUrl" id="imageUrl" value="${uploadResult.imageUrl}" />
</form>

</body>
</html>