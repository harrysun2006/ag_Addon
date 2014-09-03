package com.amaxgs.addon;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.amaxgs.addon.service.AddonService;
import com.liferay.portal.spring.util.SpringUtil;

interface AGMemberLister {

	public final static Long DEFAULT_PAGE_SIZE = new Long(500);

	public Long getTotalNumber();
	public List getList(Long pageNumber) throws Exception;

	public class DBLister implements AGMemberLister {

		private String countHql;
		private String listHql;
		private String[] parameters;
		private Object[] values;
		private Long pageSize;
		private boolean bHql;
		private Class clazz;
		private AddonService service;
		private final static ApplicationContext _ctx = SpringUtil.getContext();

		public DBLister(String countHql, String listHql, String[] parameters, Object[] values, 
				Long pageSize, boolean bHql, Class clazz) {
			this.countHql = countHql;
			this.listHql = listHql;
			this.parameters = parameters;
			this.values = values;
			this.pageSize = pageSize;
			this.bHql = bHql;
			this.clazz = clazz;
			this.service = (AddonService) _ctx.getBean(AddonService.class.getName());
		}

		public Long getTotalNumber() {
			return service.getTotalNumber(countHql, parameters, values, bHql);
		}

		public List getList(Long pageNumber) throws Exception {
			return service.listObject(listHql.toString(), parameters, values, 
					pageNumber, pageSize, bHql, clazz);
		}

	}

	public class FileLister implements AGMemberLister {

		private String fileName;

		public FileLister(String fileName) throws FileNotFoundException {
			this.fileName = fileName;
		}

		public Long getTotalNumber() {
			return null;
		}

		public List getList(Long pageNumber) throws Exception {
			if (pageNumber != null && pageNumber.longValue() == 1) {
				String name = FileHelper.guessFileName(fileName);
				return FileHelper.readFile2List(name);
			} else return null;
		}

	}
}
