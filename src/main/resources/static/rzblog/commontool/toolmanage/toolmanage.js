var prefix = ctx + "commontool/toolmanage"

$(function() {
	var columns = [
			{
				checkbox : true
			},
			{
				field : 'toolId',
				title : '工具id'
			},
			{
				field : 'toolName',
				title : '工具名称',
				width : 220,
			},
			{
				visible : false,
				field : 'toolDes',
				title : '工具描述',
				width : 220,
			},
			{
				field : 'toolType',
				title : '工具类型',
				align : 'center',
				formatter : function(value, row, index) {
					if (value == '0') {
						if (row.createType == 0) {
							return '<span>网页版（内置）</span>';
						} else {
							return '<span>网页版（后置）</span>';
						}
					} else if (value == '1') {
						if (row.createType == 0) {
							return '<span>客户端版（内置）</span>';
						} else {
							return '<span>客户端版（后置）</span>';
						}
					}
				}
			},
			{
				field : 'toolCodeType',
				title : '代码类型',
				align : 'center',
				formatter : function(value, row, index) {
					if (value == '0') {
						return '<span>JAVA</span>';
					} else if (value == '1') {
						return '<span>JAVASCRIPT</span>';
					} else if (value == '2') {
						return '<span>PYTHON</span>';
					} else if (value == '3') {
						return '<span>其他</span>';
					}
				}
			},
			{
				field : 'srcDownloadStatus',
				title : '源码可下载',
				align : 'center',
				formatter : function(value, row, index) {
					if (value == '0') {
						return '<span class="label label-primary">是</span>';
					} else if (value == '1') {
						return '<span class="label label-danger">否</span>';
					}
				}
			},
			{
				field : 'execexeDownloadStatus',
				title : '可执行exe可下载',
				align : 'center',
				formatter : function(value, row, index) {
					if (value == '0') {
						return '<span class="label label-primary">是</span>';
					} else if (value == '1') {
						return '<span class="label label-danger">否</span>';
					}
				}
			},
			{
				field : 'setupexeDownloadStatus',
				title : '安装版exe可下载',
				align : 'center',
				formatter : function(value, row, index) {
					if (value == '0') {
						return '<span class="label label-primary">是</span>';
					} else if (value == '1') {
						return '<span class="label label-danger">否</span>';
					}
				}
			},
			{
				field : 'status',
				title : '可用状态',
				align : 'center',
				formatter : function(value, row, index) {
					if (value == '0') {
						return '<span class="label label-primary">是</span>';
					} else if (value == '1') {
						return '<span class="label label-danger">否</span>';
					}
				}
			},
			{
				title : '操作',
				align : 'center',
				formatter : function(value, row, index) {
					var actions = [];
					actions
							.push('<a class="btn btn-success btn-xs '
									+ editFlag
									+ '" href="#" title="编辑" mce_href="#" onclick="edit(\''
									+ row.toolId
									+ '\')"><i class="fa fa-edit"></i>编辑</a> ');
					if (row.createType == 1) {
						actions.push('<a class="btn btn-warning btn-xs '
								+ removeFlag
								+ '" href="#" title="删除" onclick="remove(\''
								+ row.toolId
								+ '\')"><i class="fa fa-remove"></i>删除</a>');
					} else if (row.createType == 0) {
						actions.push('<a class="btn btn-info btn-xs ' + runFlag
								+ '" href="#" title="预览" onclick="preview(\''
								+ row.toolBackId + '\',\'' + row.toolName
								+ '\')"><i class="fa fa-play"></i>预览</a>');
					}
					return actions.join('');
				}
			} ];
	var url = prefix + "/list";
	$.initTable(columns, url,'请输入工具名称');
});

/* 通用工具管理-新增 */
function add() {
	var url = prefix + '/add';
	layer_showAuto("新增通用工具", url);
}

/* 通用工具管理-修改 */
function edit(toolId) {
	var url = prefix + '/edit/' + toolId;
	layer_showAuto("修改通用工具", url);
}
/* 预览工具 */
function preview(toolBackId, toolName) {
	var url = prefix + '/preview/' + toolBackId;
	createMenuItem(url, "预览工具（" + toolName + "）");
}

// 单条删除
function remove(id) {
	$.modalConfirm("确定要删除选中通用工具吗？", function() {
		_ajax(prefix + "/remove/" + id, "", "post");
	})
}

// 批量删除
function batchRemove() {
	var rows = $.getSelections("toolId");
	if (rows.length == 0) {
		$.modalMsg("请选择要删除的数据", "warning");
		return;
	}
	$.modalConfirm("确认要删除选中的" + rows.length + "条数据吗?", function() {
		_ajax(prefix + '/batchRemove', {
			"ids" : rows
		}, "post");
	});
}
/* 通用工具管理-其他设置 */
function ocrSet() {
	var url = prefix + '/toolset/ocrSet';
	layer_showAuto("OCR设置", url);
}
