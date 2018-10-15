<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>激活成功</title>
</head>
<body>
	<div style="width:400px;height:250px;margin:auto;margin-top:200px;padding:20px;text-align:center;">
		激活成功！系统将在 <span id="time" style="color:red">5</span> 秒钟后自动跳转至登陆页面，如果未能跳转，
		<a href="${pageContext.request.contextPath}/login.jsp" title="点击访问">请点击此处</a>
	</div>
	<script type="text/javascript">
		delayURL();   
		function delayURL() {
		    var delay = document.getElementById("time").innerHTML;
			var t = setTimeout("delayURL()", 1000);
		    if (delay > 0) {
		        delay--;
		        document.getElementById("time").innerHTML = delay;
		    } else {
		 		clearTimeout(t); 
		        window.location.href = "${pageContext.request.contextPath}/login.jsp";
		    }       
		} 
	</script>
</body>
</html>