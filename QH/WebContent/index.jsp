<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

    <!--
...........................................................

      ********      **    **   **          **        **
     **//////      //**  **   //**       /****     /**
    /**             //***      //**     /** /**   /**
    /*********      //**        //**   /**  //** /**
    ////////**     //**          //** /**    //****
           /**    //**            //****      //**
    *********    //**             //**        /**
    ////////     ///              ///         //


• @author 程序编写：孙园玮  	测试人员：猩猩
• email 1601062008@qq.com
• date 2018/9/21
...........................................................
    -->

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>(" °-°)</title>
<link href="${pageContext.request.contextPath}/css/page.css"
	type="text/css" rel="stylesheet">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/QH.js"></script>
</head>
<body>
	<!-- BGM:彩虹猫 -->
	<audio src="mp3/rc.mp3" autoplay="autoplay" loop="loop"
		style="display:none;" id="music"></audio>
	
	<!-- 开始界面 -->
	<div class="owndiv" id="blue">
		<div class="font">
			<span>-&nbsp;孙孙快打&nbsp; -</span><br>
			<br> <span>规则：根据生成内容输入相同的字符</span><br> <span>输入错误或超时则游戏失败自动结束</span><br>
			<span id="click">(点击开始)</span>
		</div>
	</div>

	<!-- 主界面 -->
	<div class="owndiv" id="pink">
		<div class="font" id="xiaoshi">
			<span>【级别：<span id="level"></span>,轮数：<span id="times"></span>】
			</span><br>
			<br> <span>当前积分：<span style="font-size: x-large;"><span
					id="integral"></span></span>， 剩余时间: <span
				style="color: orangered; font-size: x-large;"><span
					id="remainingTime"></span></span></span>
		</div>
		<div class="font" id="input" style="padding-top: 30px;">
			<span>-第<span id="theTimes"></span>轮-
			</span><br>
			<br> <span
				style="font-family: sylfaen; font-size: xx-large; font-weight: bold;"
				id="str"></span>
			<form action="is_iframe" method="post" target="the_iframe"
				style="margin-top: 40px;" onsubmit="return sub();" id="formId">
				<input type="text" placeholder=" Enter提交" id="id_input_text"
					name="the_input_text"
					style="border-radius: 9px; height: 30px; width: 300px; font-size: large;" />
			</form>
			<iframe id="is_iframe" name="the_iframe" style="display: none;"></iframe>	<!-- 提交时不跳转页面 -->
		</div>
		<!-- 结束界面信息 -->
		<div id="message" class="font">
			<span id="mes"></span><br> 最终得分：<span id="endScore"></span><br>
			<br> <a href="${pageContext.request.contextPath}" id="a">重新开始</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<span id="sss">查看排行榜</span>
		</div>
	</div>

	<!-- 提交信息界面 -->
	<div class="owndiv" id="green">
		<br>
		<br>
		<br>
		<span class="font">那啥...你上榜了，留个信息吧</span><br>
		<br>
		<form action="is_iframe2" method="post" target="the_iframe2"
			style="margin-top: 40px;" onsubmit="return sub2();" id="formId2">
			<input type="text" placeholder=" 称号" id="id_input_text2"
				name="the_input_text2" maxlength="6" required="required"
				style="border-radius: 9px; height: 30px; width: 300px; font-size: large;" /><br>
			<br>
			<br> <input type="text" placeholder=" 留言" id="id_input_text3"
				name="the_input_text3" maxlength="20" required="required"
				style="border-radius: 9px; height: 30px; width: 300px; font-size: large;" />
			<input type="hidden" id="hideScore"> <br>	<!-- 隐藏域，用于存储结束得分 -->
			<br>
			<br>
			<input type="submit" value="提交">
		</form>
		<iframe id="is_iframe2" name="the_iframe2" style="display: none;"></iframe>	<!-- 提交时不跳转页面 -->
	</div>

	<!-- 排行榜界面 -->
	<div id="list" class="owndiv">
		<ul style="list-style: none; padding: 10px;">
			<li style="font-weight: bold;"><span class="player">玩家</span><span
				class="score">分数</span><span class="desc">&nbsp;&nbsp;&nbsp;&nbsp;
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;留言</span></li>
			<hr>
			<li><span class="player"></span><span class="score"></span><span
				class="desc"></span></li>
			<hr>
			<li><span class="player"></span><span class="score"></span><span
				class="desc"></span></li>
			<hr>
			<li><span class="player"></span><span class="score"></span><span
				class="desc"></span></li>
			<hr>
			<li><span class="player"></span><span class="score"></span><span
				class="desc"></span></li>
			<hr>
			<li><span class="player"></span><span class="score"></span><span
				class="desc"></span></li>
			<hr>
		</ul>
	</div>
</body>
</html>