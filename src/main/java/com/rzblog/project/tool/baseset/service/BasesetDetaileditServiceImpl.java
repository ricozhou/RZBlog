package com.rzblog.project.tool.baseset.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rzblog.common.utils.security.ShiroUtils;
import com.rzblog.project.tool.baseset.mapper.BasesetMapper;
import com.rzblog.project.tool.baseset.domain.Baseset;

/**
 * 基础设置详情 服务层实现
 * 
 * @author ricozhou
 * @date 2018-06-23
 */
@Service
public class BasesetDetaileditServiceImpl implements IBasesetDetaileditService {
	@Autowired
	private BasesetMapper basesetMapper;

	/**
	 * 保存首页介绍示例详情
	 */
	@Override
	public int loginSetsave(Baseset baseset) {
		baseset.setUpdateBy(ShiroUtils.getLoginName());
		int rows = basesetMapper.updateLoginsetEdit(baseset);
		return rows;
	}

	/**
	 * 保存首页介绍示例详情
	 */
	@Override
	public int homeIntroductionSave(Baseset baseset) {
		baseset.setUpdateBy(ShiroUtils.getLoginName());
		int rows = basesetMapper.updateBasesetEdit(baseset);
		return rows;
	}

	/**
	 * 保存代码示例详情
	 */
	@Override
	public int spiderCodeExampleSave(Baseset baseset) {
		baseset.setUpdateBy(ShiroUtils.getLoginName());
		int rows = basesetMapper.updateBasesetEdit(baseset);
		return rows;
	}

	// 保存手册
	@Override
	public int spiderManualSave(Baseset baseset) {
		baseset.setUpdateBy(ShiroUtils.getLoginName());
		int rows = basesetMapper.updateManualEdit(baseset);
		return rows;
	}

}
