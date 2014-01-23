<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!doctype html>
<html lang="fr">
<head>
	<meta charset="utf-8">
	<title>Document Generator</title>
	<link href="<c:url value="css/style.css" />" rel="stylesheet">
	<script src="<c:url value="js/tinymce/tinymce.min.js" />"></script>
	<script type="text/javascript">
	tinymce.init({
		height: "300",
		selector: "textarea",
		plugins: [
		          "advlist autolink lists link image charmap preview hr anchor pagebreak",
		          "searchreplace wordcount visualblocks visualchars code fullscreen",
		          "insertdatetime media nonbreaking save table contextmenu directionality",
		          "emoticons template paste textcolor"
		      ],
	    toolbar1: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image",
	    toolbar2: "print preview media | forecolor backcolor emoticons",
	    image_advtab: true,
	    templates: [
	        {title: 'Test template 1', content: 'Test 1'},
	        {title: 'Test template 2', content: 'Test 2'}
	    ]
	});
	</script>
</head>
<body>
	<h1>Document Generator</h1>	
	<form method="post">
		<textarea id="html" name="html"></textarea><br/>
		<h3>Implemented</h3>
		<button type="submit" name="extension" value="PDF">PDF</button>
		<h3>Not Implemented yet</h3>
		<button type="submit" name="extension" value="DOC">DOC</button>
		<button type="submit" name="extension" value="DOCX">DOCX</button>
	</form>
</body>
</html>