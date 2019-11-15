$().ready(function() {
	initSimplemde();
	initSummernote();
	initSimditor();
})

// 加载文本编辑器
var imgArray = new Array();
var simplemde;
var simeditor;
// 所有模板上传图片放在这里，删除模板不再删除图片
var blogFileName = 'blogtemplate';
var articleEditor = $("#articleEditor2").val();
function initSimplemde() {
	// 初始化编辑器
	simplemde = new SimpleMDE({
		// textarea的DOM对象
		element : document.getElementById("articleField"),
		// 自动下载FontAwesome，设为false为不下载
		autoDownloadFontAwesome : false,
		placeholder : "请输入文章内容",
		autosave : {
			// 启用自动保存功能
			enabled : false,
			// 自动保存的间隔，以毫秒为单位。默认为10000（10s）
			delay : 15000,
			// 唯一的字符串标识符(保证每个SimpleMDE编辑器的uniqueId唯一)
			uniqueId : "editor-1"
		},
		renderingConfig : {
			// 如果设置为true，将使用highlight.js高亮显示。默认为false
			codeSyntaxHighlighting : true
		},
		showIcons : [ "code", "table", "clean-block", "horizontal-rule" ],
		tabSize : 4,
		// 编辑器底部的状态栏
		status : false,
		status : [ "lines", "words" ]
	});
	$(".editor-preview-side").addClass("markdown-body");
}
function initSummernote() {
	// 编辑器初始化
	$('.summernote').summernote({
		height : '220px',
		lang : 'zh-CN',
		callbacks : {
			onImageUpload : function(files, editor, $editable) {
				// 最后的true表示打水印按照配置信息中是否打为准
				sendBlogFile(files, imgArray, blogFileName, true);
			}
		}
	});
	if (articleEditor == 0) {
		var content2 = $("#content").val();
		$('#content_sn').summernote('code', content2);
	}
}
function initSimditor() {
	// 初始化编辑器
	simeditor = new Simditor({
		textarea : $('#articleField2'),
		placeholder : '',
		toolbar : [ 'title', 'bold', 'italic', 'underline', 'strikethrough',
				'color', '|', 'ol', 'ul', 'blockquote', 'code', 'table', '|',
				'link', 'image', 'hr', '|', 'indent', 'outdent' ],
		upload : {
			url : ctx + "blog/blogcontent/uploadBlogImg?blogFileName="
					+ blogFileName + "&addWaterMark=true", // 文件上传的接口地址
			params : null, // 键值对,指定文件上传接口的额外参数,上传的时候随文件一起提交
			fileKey : 'file', // 服务器端获取文件数据的参数名
			connectionCount : 1,
			leaveConfirm : '正在上传文件'
		}
	});
}
function setEditorSelectShow(articleEditor) {
	if (articleEditor == 0) {
		// html
		$("#summernote").css('display', 'block');
		$("#simplemde").css('display', 'none');
		$("#simditor").css('display', 'none');
	} else if (articleEditor == 1) {
		// markdown
		$("#simplemde").css('display', 'block');
		$("#summernote").css('display', 'none');
		$("#simditor").css('display', 'none');
	} else if (articleEditor == 2) {
		// html
		$("#simditor").css('display', 'block');
		$("#summernote").css('display', 'none');
		$("#simplemde").css('display', 'none');
	}
}
// 绑定监听事件
$(function() {
	// 初始化
	setEditorSelectShow(articleEditor);

	// 监听编辑器选择
	$('#articleEditor').bind('change', function() {
		var articleEditor = $(this).val();
		setEditorSelectShow(articleEditor);
	});
});
// 表单验证
$("#form-blogtemplate-edit").validate({
	rules : {
		templateName : {
			required : true,
			maxlength : 100
		},
		author : {
			maxlength : 50
		}
	},
	messages : {
		"templateName" : {
			maxlength : '最长50字'
		},
		"author" : {
			maxlength : "最长20字",
		},
	},
	submitHandler : function(form) {
		update();
	}
})

function update() {
	var blogTemplateId = $("#blogTemplateId").val();
	var templateName = $("#templateName").val();
	// 作者
	var author = $("#author").val();

	var articleEditor = $("#articleEditor").val();
	// 内容
	var content = '';
	var contentMd = '';
	var text = '';
	if (articleEditor == 1) {
		content = simplemde.markdown(simplemde.value());
		contentMd = simplemde.value();
		// text = removeHTMLTag(content);
		if (content == "" || content.length > 1000000) {
			$.modalAlert("文章不能为空且字数不能超过50万", modal_status.FAIL);
			return;
		}
	} else if (articleEditor == 2) {
		// simditor
		content = simeditor.getValue();
		if (content == "" || content.length > 1000000) {
			$.modalAlert("文章不能为空且字数不能超过50万", modal_status.FAIL);
			return;
		}
	} else if (articleEditor == 0) {
		content = $("#content_sn").summernote('code');
		if (content == "<p><br></p>" || content.length > 1000000) {
			$.modalAlert("文章不能为空且字数不能超过50万", modal_status.FAIL);
			return;
		}
		// text = $($("#content_sn").summernote("code")).text();
	}

	// 状态
	var status = $("#status").is(':checked') == true ? 0 : 1;

	// 类型
	var blogType = $("#blogType").val();
	var remark = $("#remark").val();

	$.ajax({
		cache : true,
		type : "POST",
		url : ctx + "blog/blogtemplate/save",
		data : {
			"blogTemplateId" : blogTemplateId,
			"templateName" : templateName,
			"blogFileName" : blogFileName,
			"author" : author,
			"articleEditor" : articleEditor,
			"content" : content,
			"contentMd" : contentMd,
			"blogType" : blogType,
			"status" : status,
			"remark" : remark
		},
		async : false,
		error : function(request) {
			$.modalAlert("系统错误", modal_status.FAIL);
		},
		success : function(data) {
			if (data.code == 0) {
				layer.msg("修改成功,正在刷新数据请稍后……", {
					icon : 1,
					time : 500,
					shade : [ 0.1, '#fff' ]
				}, function() {
					$.parentReload();
				});
			} else {
				$.modalAlert(data.msg, modal_status.FAIL);
			}

		}
	});
}

/** 关闭弹出框口 */
function layer_close_to_back() {
	// 发送到后台删除缓存文件夹
	$.ajax({
		cache : true,
		type : "POST",
		url : ctx + "blog/blogcontent/deleteCacheFile",
		data : {
			"blogFileName" : blogFileName,
		},
		async : false,
		error : function(request) {
		},
		success : function(data) {
		}
	});
	var index = parent.layer.getFrameIndex(window.name);
	parent.layer.close(index);
}

// 获取html纯文本
function removeHTMLTag(str) {
	str = str.replace(/<\/?[^>]*>/g, ''); // 去除HTML tag
	str = str.replace(/[ | ]*\n/g, '\n'); // 去除行尾空白
	// str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
	str = str.replace(/&nbsp;/ig, '');// 去掉&nbsp;
	str = str.replace(/\s/g, ''); // 将空格去掉
	return str;
}