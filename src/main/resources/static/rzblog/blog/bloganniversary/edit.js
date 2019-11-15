$("#form-bloganniversary-edit").validate({
	rules : {
		anniversaryName : {
			required : true,
			maxlength : 200
		},
		anniversaryDesc : {
			required : true,
			maxlength : 8000
		},
	},
	submitHandler : function(form) {
		update();
	}
});
// 绑定监听事件
$(function() {
	$('#anniversaryDateMonth').bind('change', function() {
		var anniversaryDateMonth = $(this).val();
		setDateSelect(anniversaryDateMonth);
	});

	// 设置初始值
	var anniversaryDate2 = $("#anniversaryDate").val();
	if (anniversaryDate2 != null && anniversaryDate2 != '') {
		var aDates = anniversaryDate2.split('-');
		setDateSelect(aDates[0]);
		setSelectDefault(aDates[0], 'anniversaryDateMonth');
		setSelectDefault(aDates[1], 'anniversaryDateDay');
	}
});
function setDateSelect(anniversaryDateMonth) {
	document.getElementById('anniversaryDateDay').innerHTML = '';
	var month1 = [ "1", "3", "5", "7", "8", "10", "12" ];
	var month2 = [ "4", "6", "9", "11" ];
	var dayAddHtml = '<option value="1">1</option><option value="2">2</option><option value="3">3</option><option value="4">4</option><option value="5">5</option><option value="6">6</option><option value="7">7</option><option value="8">8</option><option value="9">9</option><option value="10">10</option><option value="11">11</option><option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option><option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option><option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option><option value="24">24</option><option value="25">25</option><option value="26">26</option><option value="27">27</option><option value="28">28</option><option value="29">29</option>';
	if (month1.indexOf(anniversaryDateMonth) >= 0) {
		dayAddHtml = dayAddHtml
				+ '<option value="30">30</option><option value="31">31</option>';
	} else if (month2.indexOf(anniversaryDateMonth) >= 0) {
		dayAddHtml = dayAddHtml + '<option value="30">30</option>';
	}
	document.getElementById('anniversaryDateDay').insertAdjacentHTML(
			'afterBegin', dayAddHtml)
}
function update() {
	var blogAnniversaryId = $("#blogAnniversaryId").val();
	var anniversaryDate = $("#anniversaryDateMonth").val() + '-'
			+ $("#anniversaryDateDay").val();
	var anniversaryName = $("#anniversaryName").val();
	var anniversaryDesc = $("#anniversaryDesc").val();
	var anniversaryWebsiteOperate = $("#anniversaryWebsiteOperate").val();
	var anniversaryWebsiteShow = $("#anniversaryWebsiteShow").val();
	var status = $("input[name='status']").is(':checked') == true ? 0 : 1;
	var remark = $("input[name='remark']").val();
	$.ajax({
		cache : true,
		type : "POST",
		url : ctx + "blog/bloganniversary/save",
		data : {
			"blogAnniversaryId" : blogAnniversaryId,
			"anniversaryDate" : anniversaryDate,
			"anniversaryName" : anniversaryName,
			"anniversaryDesc" : anniversaryDesc,
			"anniversaryWebsiteOperate" : anniversaryWebsiteOperate,
			"anniversaryWebsiteShow" : anniversaryWebsiteShow,
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