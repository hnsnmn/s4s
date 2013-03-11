<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page session="false" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Upload File</title>
	<script type="text/javascript" src="<c:url value='/js/jquery-1.5.1.min.js' />"></script>
	<script type="text/javascript">
	<c:if test="${! empty param.domain}">
	document.domain="${param.domain}";
	</c:if>

	<c:if test="${param.p == 't'}">
	var p = true;
	</c:if>
	<c:if test="${param.p != 't'}">
	var p = false;
	</c:if>
	
	function trigger() {
		var e = jQuery.Event("click");
		jQuery("#file").trigger(e);
	}
	
	function send() {
		var form = document.uploadForm;
		form.target = '_self';
		form.submit();
	}
	</script>
</head>
<body>
<form name="uploadForm" id="uploadForm"
	action="<c:url value='/upload/${serviceId}' />?p=${param.p}&domain=${param.domain}" method="post" enctype="multipart/form-data">
	<input type="hidden" name="view" id="view" value="form" />
	<input type="file" id="file" name="file" onchange="send()" />
</form>
</body>
</html>