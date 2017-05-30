<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>

<link rel="stylesheet" type="text/css" href="/css/frontline.css" />

</head>
<body>
	<div class="container">
        <h1>Derek Dupuis<br>Frontline Coding Demo</h1>
	
		<c:choose>
			<c:when test="${status=='error'}">
				<div>
					<h2>An exception occurred. Here is the message:</h2>
					<p>${message}</p>
				</div>
			</c:when>
			<c:otherwise>
				<h2>Input string:</h2>
				<p>${input}</p>
				<p />
				<h2>Converted string:</h2>
		
				<c:forEach items="${convertedString}" var="str">
					<p>${str}</p>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>

</body>

</html>