var prefix = ctx + "blog/blogtemplate"

$(function() {
	var columns = [
			{
				checkbox : true
			},
			{
				field : 'blogTemplateId',
				align : 'center',
				title : 'ID'
			},
			{
				field : 'templateName',
				align : 'center',
				title : '模板名称'
			},
			{
				field : 'blogType',
				align : 'center',
				title : '模板分类',
				formatter : function(value, row, index) {
					if (value == 0) {
						return '<span class="label label-success">文章</span>';
					} else if (value == 1) {
						return '<span class="label label-primary">软件</span>';
					} else if (value == 2) {
						return '<span class="label label-warning">影视</span>';
					}
				}
			},
			{
				field : 'articleEditor',
				align : 'center',
				title : '编辑器类型',
				formatter : function(value, row, index) {
					if (value == "0") {
						return '<span>HTML编辑器（summernote）</span>';
					} else if (value == "1") {
						return '<span>MarkDown编辑器（simplemde）</span>';
					} else if (value == "2") {
						return '<span>HTML编辑器（simditor）</span>';
					}
				}
			},
			{
				field : 'author',
				align : 'center',
				title : '作者'
			},
			{
				field : 'status',
				align : 'center',
				title : '状态',
				formatter : function(value, row, index) {
					if (value == "0") {
						return '<span class="label label-success">可用</span>';
					} else {
						return '<span class="label label-danger">禁用</span>';
					}
				}
			},
			{
				field : 'createTime',
				align : 'center',
				visible : false,
				title : '创建时间'
			},
			{
				field : 'updateTime',
				align : 'center',
				visible : false,
				title : '修改时间'
			},
			{
				title : '操作',
				align : 'center',
				formatter : function(value, row, index) {
					var actions = [];
					actions
							.push('<a class="btn btn-primary btn-xs '
									+ editFlag
									+ '" href="#" title="编辑" mce_href="#" onclick="edit(\''
									+ row.blogTemplateId
									+ '\')"><i class="fa fa-edit"></i>编辑</a> ');
					actions.push('<a class="btn btn-warning btn-xs '
							+ removeFlag
							+ '" href="#" title="删除" onclick="remove(\''
							+ row.blogTemplateId
							+ '\')"><i class="fa fa-remove"></i>删除</a>');
					return actions.join('');
				}
			} ];
	var url = prefix + "/list";
	$.initTable(columns, url, '请输入模板名称');
});

/* 模板管理-新增 */
function add() {
	var url = prefix + '/add';
	layer_showAutoAll("新增模板", url);
}

/* 模板管理-修改 */
function edit(blogTemplateId) {
	var url = prefix + '/edit/' + blogTemplateId;
	layer_showAutoAll("修改模板", url);
}

// 单条删除
function remove(id) {
	$.modalConfirm("确定要删除选中模板吗？", function() {
		_ajax(prefix + "/remove/" + id, "", "post");
	})
}

// 批量删除
function batchRemove() {
	var rows = $.getSelections("blogTemplateId");
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
