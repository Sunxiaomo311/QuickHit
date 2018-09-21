// 获取指定长度字符串
function getStr(num) { 
	if (num > 7) {
		var allStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~!@#$%^&*()_+-={}[]|\:;<>,.?/";
	} else {
		var allStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890,.;";
	}
	var index;
	var str = "";
	for (var i = 0; i < num; i++) {
		index = Math.floor(Math.random() * allStr.length);
		str += allStr.substring(index, index + 1);
	}
	return str;
}

// 初始界面
$(function() {
	// 初始值
	$("#level").text(1);
	$("#times").text(3);
	$("#integral").text(0);
	$("#remainingTime").text(30);
	$("#theTimes").text(1);
	$("#str").text(getStr(3));

	$("#blue").click(function() {	// 点击开始
		$(this).fadeOut(1000);
		$("#pink").fadeIn(2000); // 切换动画
	});

})

// 点击开始后自动计时
$(function() {
	$("#blue").click(function() {
		var wait = parseInt($("#remainingTime").text());
		timeOut(wait);

	});
})

// 计时器，根据传入参数倒数计时
function timeOut(wait) {
	if (wait != 0) {
		cce = setTimeout(function() {
			$('#remainingTime').text(--wait);
			timeOut(wait);
		}, 1000);
	}
}

var newTime = 30;	// 初始时间
// 提交比较输入与自动生成的字符串
function sub() {
	var remainingTime = parseInt($('#remainingTime').text());
	if (remainingTime == 0) { // 超时
		error();
		$("#mes").text("超出时间，游戏结束");
	} else {
		var input = $("#id_input_text").val();
		var str = $("#str").text();
		if (str == input) {	// 输入匹配
			var integral = parseInt($("#integral").text());
			var strLen = str.length;
			integral += strLen;
			$("#integral").text(integral);

			var theTimes = parseInt($("#theTimes").text());
			var times = parseInt($("#times").text());
			if (theTimes == times) { // 轮数达到后升级
				newTime += 6; // 升级加时
				upgrade(newTime);
				strLen++;
			} else {
				theTimes += 1;
				$("#theTimes").text(theTimes);
			}

			$("#str").text(getStr(strLen));
			$("#id_input_text").val(""); // 清空输入

		} else { // 失败
			error();
			$("#mes").text("输入错误，游戏结束");
		}
	}
	return false;	// 返回false，否则页面会提交两次刷新
}

// 输入错误/超时
function error() {
	$("#message").css("display", "block")
	$("#pink").css("background-color", "rgba(255, 59, 0, 0.69)");
	$("#pink").css("opacity", "0.7");
	$("#xiaoshi").hide();
	$("#endScore").text($("#integral").text());
	$("#id_input_text").attr("disabled", "disabled");
	$("#music").attr("src", "mp3/hmbbed.mp3");	// BGM切换：海绵宝宝ED

	// 排行榜
	var score = parseInt($("#integral").text());	// 结束时分数

	$.ajax({
		url : "score?score=" + score,
		dataType : "text",
		success : function(data) { // 判断是否可以入榜单
			if (data == "in") { // 加入榜单
				intoList(score);
			}
		}
	})
}

// 上榜时触发动画
function intoList(score) {
	$("#pink").css("transform", "rotate3d(0,1,0,360deg");
	$("#pink").css("transition", "transform 1s ease-in-out");
	$('#pink').animate({
		opacity : '0'
	}, 500);
	setTimeout(function() {
		$('#pink').css("display", "none");
	}, 1000);
	$('#green').css("display", "block");
	$('#green').animate({
		opacity : '0.6'
	}, 1000);
	$("#green").css("transform", "rotate3d(0,1,0,360deg");
	$("#green").css("transition", "transform 1s ease-in-out");

	$("#hideScore").text(score);	// 隐藏域存储结束得分
}

// 级别难度控制
function upgrade(newTime) { // 级别参数控制
	var level = $("#level").text();
	level++;
	$("#level").text(level);
	var times = $("#times").text();
	times++;
	$("#times").text(times);
	$("#theTimes").text(1);
	clearTimeout(cce); // 重新计时
	timeOut(newTime);
}

// 点击查看排行榜 动画
$(function() {
	$("#sss").toggle(function() {
		$("#list").css("display", "block");
		$("#pink").animate({
			left : '-250px'
		});
		$("#list").animate({
			left : '250px'
		});
		flush();
	}, function() {
		$("#pink").animate({
			left : '0px'
		});
		$("#list").animate({
			left : '0px'
		});
		setTimeout(function() {
			$('#list').css("display", "none");
		}, 350);
	});
})

// 排行榜动态刷新数据
function flush() {
	$.ajax({
		url : "list",
		dataType : "json",
		success : function(data) {
			for (var i = 0; i < 5; i++) {
				$(".player:eq(" + (i + 1) + ")").text(data[i].name);
				$(".score:eq(" + (i + 1) + ")").text(data[i].score);
				$(".desc:eq(" + (i + 1) + ")").text(data[i].message);
			}
		}
	});
}

// 上榜信息提交
function sub2() {
	var score = parseInt($("#hideScore").text());
	var name = $("#id_input_text2").val();
	var desc = $("#id_input_text3").val();

	if (!name.match("^[a-zA-Z0-9_\u4e00-\u9fa5]+$")
			|| !desc.match("^[a-zA-Z0-9_\u4e00-\u9fa5]+$")) {	// 特殊字符过滤，否则web层无法正常接收
		alert("请不要输入特殊字符!");
		$("#id_input_text2").val("");
		$("#id_input_text3").val("");
		return;
	}

	$.ajax({
		url : "changeList?score=" + score + "&name=" + name + "&desc=" + desc,
		dataType : "json",
		success : function(data) {
		}
	});

	$('#green').animate({	// 信息提交后 回转页面动画
		opacity : '0'
	}, 500);
	setTimeout(function() {
		$('#green').css("display", "none");
	}, 1000);
	$('#pink').css("display", "block");
	$('#pink').animate({
		opacity : '0.6'
	}, 1000);
	return false;
}