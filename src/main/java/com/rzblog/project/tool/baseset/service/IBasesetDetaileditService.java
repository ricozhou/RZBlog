package com.rzblog.project.tool.baseset.service;

import com.rzblog.project.tool.baseset.domain.Baseset;

/**
 * 基础设置详情 服务层
 * 
 * @author ricozhou
 * @date 2018-06-23
 */
public interface IBasesetDetaileditService {

	//保存首页介绍
	public int homeIntroductionSave(Baseset baseset);

	public int spiderCodeExampleSave(Baseset baseset);

	public int spiderManualSave(Baseset baseset);

	public int loginSetsave(Baseset baseset);

}
