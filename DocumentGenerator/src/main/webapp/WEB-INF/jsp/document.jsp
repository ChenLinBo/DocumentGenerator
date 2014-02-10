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
			setup : function(ed)
		    {
		        ed.on('init', function() 
		        {
		            this.getDoc().body.style.fontSize = '10px';
		            this.getDoc().body.style.fontFamily = 'Arial';
		        });
		    },
		height: "300",
		selector: "textarea",
		plugins: [
		          "advlist autolink lists link image charmap preview hr anchor pagebreak",
		          "searchreplace wordcount visualblocks visualchars code fullscreen",
		          "media table contextmenu directionality",	
		          "paste textcolor"
		      ],
	    toolbar1: "undo redo | fontselect | fontsizeselect | bold italic underline | forecolor backcolor | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image media",
	    image_advtab: true
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