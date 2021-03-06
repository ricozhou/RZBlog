var prefix = "/commontool/toolrun";
$("#form-toolrun-run").validate({
	rules : {
		content : {
			required : true,
			maxlength : 20000
		}
	},
	submitHandler : function(form) {
		run();
	}
});
// 初始化显示界面
$(document).ready(function() {
	// 初始化
	init();
});
// 初始化
function init() {
	// 绑定是否插入logo
	$('#status').bind('change', function() {
		var insertLogo = $('#status').is(':checked');
		// 选中则可用
		if (insertLogo) {
			$("#show1").css('visibility', 'visible');
			$("#pic2").css('visibility', 'visible');
			$("#show3").css('display', 'none');
			document.getElementById("radio2").checked = false;
			document.getElementById("radio1").checked = true;
			chanageLabel();
		} else {
			$("#show1").css('visibility', 'hidden');
			$("#pic2").css('visibility', 'hidden');
			$("#show3").css('display', 'none');
		}
	});
	// 绑定复选框
	// 此地方注意，复选框被包裹在label内，所以click事件绑定label的,不是很好，待改进
	$('#clickLabel1').click(function() {
		document.getElementById("radio2").checked = false;
		document.getElementById("radio1").checked = true;
		controImgOnline();
	});
	$('#clickLabel2').click(function() {
		document.getElementById("radio1").checked = false;
		document.getElementById("radio2").checked = true;
		controImgUpload();
	});
}
function controImgOnline() {
	$("#show3").css('display', 'none');
	chanageLabel();
}
function controImgUpload() {
	$("#show3").css('display', 'block');
	chanageLabel();
}
// js选择
function selectImgOnline() {
	// 这种方式有效
	document.getElementById("radio2").checked = false;
	document.getElementById("radio1").checked = true;
	controImgOnline();
}
// 改变选择框颜色
function chanageLabel() {
	$(".i-checks").iCheck({
		checkboxClass : "icheckbox_square-green",
		radioClass : "iradio_square-green",
	})
}
// 保存
function imgORCodeSave() {
	// 先确认是否存在
	if (!checkImgFileExist($('#orCodeFileName').val())) {
		// 刷新
		$.modalAlert('文件已不存', modal_status.FAIL);
		return;
	}
	location.href = prefix + "/downloadImg?imgFileName="
			+ $('#orCodeFileName').val();
	layer.msg('正在下载,请稍后…', {
		icon : 1
	});
}
// 运行
function run() {
	var toolBackId = $('#toolBackId').val();
	var content = $('#content').val();
	var insertLogo = $("#status").is(':checked')
	var radio2 = $("#radio2").is(':checked');
	if (radio2) {
		// 文件上传
		if (imgFileName == null) {
			$.modalAlert('请先上传文件', modal_status.FAIL);
			return;
		}
	}
	var logoImageUrl;
	if (insertLogo) {
		logoImageUrl = $('#pic').val();
		if (logoImageUrl == '') {
			$.modalAlert('请先输入logo链接', modal_status.FAIL);
			return;
		}
	}
	var orCodeSize = $('#orCodeSize').val();
	var orCodeSizeW = orCodeSize;
	var orCodeSizeH = orCodeSize;
	var orCodeImgFormat = $('#orCodeImgFormat').val();
	// 请求后台处理
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix + "/runORCodeCreate",
		data : {
			"toolBackId" : toolBackId,
			"content" : content,
			"insertLogo" : insertLogo,
			"logoImageUrl" : logoImageUrl,
			"orCodeSizeW" : orCodeSizeW,
			"orCodeSizeH" : orCodeSizeH,
			"orCodeImgFormat" : orCodeImgFormat
		},
		async : false,
		error : function(request) {
			$.modalAlert("系统错误", modal_status.FAIL);
		},
		beforeSend : function() {
			// 禁用运行按钮
			document.getElementById("runORCodeCreate").setAttribute("disabled",
					true);
		},
		success : function(data) {
			if (data.code == 0 && data.fileName != null) {
				// 显示
				$("#show4").css('display', 'block');
				document.getElementById("showImg").src = '/cachefiles/'
						+ data.fileName;
				// 把生成的图片名存放一下
				$('#orCodeFileName').val(data.fileName);
				// 显示保存按钮
				document.getElementById("imgSave").removeAttribute("disabled");
			} else {
				$.modalAlert(data.msg, modal_status.FAIL);
			}
			// 可用运行按钮
			document.getElementById("runORCodeCreate").removeAttribute(
					"disabled");
		}
	});

}
// 下载前检查文件是否存在
function checkImgFileExist(fileName) {
	var fileExist = false;
	var toolBackId = $('#toolBackId').val();
	// 请求后台处理
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix + "/checkImgFileExist",
		data : {
			"toolBackId" : toolBackId,
			"content" : fileName
		},
		async : false,
		error : function(request) {
			$.modalAlert("系统错误", modal_status.FAIL);
		},
		success : function(data) {
			if (data.code == 0) {
				fileExist = true;
			} else {
				fileExist = false;
			}
		}
	});
	return fileExist;
}