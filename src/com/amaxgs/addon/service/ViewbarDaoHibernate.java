package com.amaxgs.addon.service;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.amaxgs.addon.model.VBFile;
import com.amaxgs.addon.model.VBViewbar;

class ViewbarDaoHibernate extends HibernateDaoSupport {

	public void saveViewbar(VBViewbar viewbar) throws Exception {
		getHibernateTemplate().save(viewbar);
	}

	public void saveViewbarFile(VBFile file) throws Exception {
		getHibernateTemplate().save(file);
	}

}
