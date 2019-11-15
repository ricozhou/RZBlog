$("#form-manualPushBaidu").validate({
	rules : {
		site : {
			required : true
		},
		token : {
			required : true
		}
	},
	submitHandler : function(form) {
		manialPushBaidu();
	}
});

// 绑定参数添加
function addUrl(obj) {
	html = '<div class="input-group inputParams">'
			+ '<label class="input-group-addon">URL</label>'
			+ '<input type="text" class="form-control" id="valueInput">'
			+ '<span class="input-group-btn">'
			+ '<button class="btn btn-info" type="button" data-toggle="tooltip" title="删除" id="delUrl"><span class="glyphicon glyphicon-minus"></span></button>'
			+ '</span>' + '</div>'
	obj.insertAdjacentHTML('beforebegin', html)
}

// 绑定删除
$(document).on('click', '#delUrl', function() {
	var el = this.parentNode.parentNode
	$.modalConfirm("确定要删除此目标链接？", function(e) {
		if (e) {
			el.parentNode.removeChild(el)
		}
		// 关闭弹窗
		layer.closeAll('dialog');
	})
})

function manialPushBaidu() {
	var site = $('#site').val();
	var token = $('#token').val();
	var type = $('#type').val();

	var regUrl = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;

	// 验证站点
	if (!regUrl.test(site)) {
		$.modalAlert('网址格式不正确', modal_status.FAIL);
		return;
	}
	// 以下开始参数获取
	var urls = '';
	var value = '';
	var success = true;
	// 遍历所有的参数
	$(".input-group.inputParams").each(function() {
		value = $(this).find("#valueInput").val();
		if (value == '') {
			$.modalAlert('URL不能为空！', modal_status.FAIL);
			success = false;
			return;
		}

		// 验证url
		if (!regUrl.test(value)) {
			$.modalAlert('网址格式不正确！', modal_status.FAIL);
			success = false;
			return;
		}
		urls += value + "\r\n";
	});

	if (!success) {
		return;
	}

	if (urls == null || urls == '') {
		$.modalAlert('URL不能为空！', modal_status.FAIL);
		return;
	}

	// 提交
	$.ajax({
		cache : true,
		type : "POST",
		url : ctx + "blog/blogcontent/manualPushBaidu",
		data : {
			"site" : site,
			"token" : token,
			"type" : type,
			"urls" : urls
		},
		async : false,
		error : function(request) {
			$.modalAlert("系统错误", modal_status.FAIL);
		},
		success : function(data) {
			if (data.code == 0) {
				var dataJson = JSON.parse(data.msg);
				var msg = "成功";
				if (type == 1) {
					msg = "更新";
				} else if (type == 2) {
					msg = "删除";
				}
				/**
				 * success int 成功推送的url条数 remain int 当天剩余的可推送url条数 not_same_site
				 * array 由于不是本站url而未处理的url列表 not_valid array 不合法的url列表
				 */
				var successNum = dataJson.success;
				var remain = dataJson.remain;
				var notSameSite = dataJson.not_same_site;
				var notValid = dataJson.not_valid;
				var message = msg + '推送' + successNum + '条链接！\r\n';
				if (notValid) {
					message += '不合法的链接：' + notValid + '条\r\n';
				}
				message += '今日剩余' + remain + '条可推送的链接。';
				$.modalAlert(message, modal_status.SUCCESS);
			} else {
				$.modalAlert(data.msg, modal_status.FAIL);
			}

		}
	});
}