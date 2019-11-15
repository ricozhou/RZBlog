$("#form-blogadvertisement-edit").validate({
	rules : {
		adName : {
			required : true,
			maxlength : 100
		},
		// 校验时间格式
		startDateTime : {
			required : true,
			checkDateTime : true,
			// 自定义方法
			checkDateSize : true
		},
		endDateTime : {
			required : true,
			checkDateTime : true,
			// 自定义方法
			checkDateSize : true
		},
		adCost : {
			digits : true,
			max : 999999999999,
			min : 0
		},
	},
	submitHandler : function(form) {
		update();
	}
});
// 一开始便加载
$(document).ready(function() {
	// 设置时间条件
	if (adStartIme != null && adEndIme != null) {
		$("#startDateTime").val(formatDate(adStartIme, 'yyyy-MM-dd'));
		$("#endDateTime").val(formatDate(adEndIme, 'yyyy-MM-dd'));
	}
})
function update() {
	var blogAdId = $("#blogAdId").val();
	var adName = $("#adName").val();
	var adContent = $("#adContent").val();
	var adCompany = $("#adCompany").val();
	var adCost = $("#adCost").val();
	var adType = $("#adType").val();
	var adPlacementMain = $("#adPlacementMain").is(':checked') == true ? 0 : 1;
	var adPlacementConcent = $("#adPlacementConcent").is(':checked') == true ? 0
			: 1;
	var adPlacementSider = $("#adPlacementSider").is(':checked') == true ? 0
			: 1;

	var adUrl = $("#adUrl").val();
	var adStartIme = new Date($("#startDateTime").val());
	var adEndIme = new Date($("#endDateTime").val());

	var status = $("input[name='status']").is(':checked') == true ? 0 : 1;
	var remark = $("input[name='remark']").val();
	$.ajax({
		cache : true,
		type : "POST",
		url : ctx + "blog/blogadvertisement/save",
		data : {
			"blogAdId" : blogAdId,
			"adName" : adName,
			"adContent" : adContent,
			"adCompany" : adCompany,
			"adCost" : adCost,
			"adType" : adType,
			"adPlacementMain" : adPlacementMain,
			"adPlacementConcent" : adPlacementConcent,
			"adPlacementSider" : adPlacementSider,
			"adUrl" : adUrl,
			"adStartIme" : adStartIme,
			"adEndIme" : adEndIme,
			"adImage" : fileBase64_1,
			"status" : status,
			"remark" : remark
		},
		async : false,
		error : function(request) {
			$.modalAlert("系统错误", modal_status.FAIL);
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("修改成功,正在刷新数据请稍后……", {
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