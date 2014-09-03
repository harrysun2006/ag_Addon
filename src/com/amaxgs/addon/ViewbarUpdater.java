package com.amaxgs.addon;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;

import com.amaxgs.addon.model.VBFile;
import com.amaxgs.addon.model.VBViewbar;
import com.amaxgs.addon.service.AddonService;
import com.amaxgs.addon.service.ViewbarService;
import com.liferay.portal.spring.util.SpringUtil;

/**
 * @author author E-mail:zhaon12@gmail.com
 * @version 1.0
 * @createDate createDate:Apr 6, 2007 9:52:02 AM
 * @content
 */
class ViewbarUpdater {

	private static Log _log = LogFactory.getLog(ViewbarUpdater.class);

	private final static String PROP_UPDATE_TARGET_DIR = "viewbar.update.target.dir";
	private final static String PROP_UPDATE_ZIP_FILE = "viewbar.update.zip.file";
	private final static String PROP_UPDATE_ROOT_PATH = "viewbar.update.root.path";
	private final static String DEFAULT_UPDATE_TARGET_DIR = "/home/agloco/viewbar/download/";
	private final static String DEFAULT_UPDATE_ZIP_FILE = "viewbara.zip";
	private final static String DEFAULT_UPDATE_ROOT_PATH = "http://viewbar.agloco.com/download";

	private final static ApplicationContext _ctx = SpringUtil.getContext();

	public static void update() throws Exception {
		String answer, viewbarId;
		String targetDir = Agloco.ADDON_PROPERTIES.getProperty(PROP_UPDATE_TARGET_DIR, DEFAULT_UPDATE_TARGET_DIR);
		String zipFile = Agloco.ADDON_PROPERTIES.getProperty(PROP_UPDATE_ZIP_FILE, DEFAULT_UPDATE_ZIP_FILE);
		String rootPath = Agloco.ADDON_PROPERTIES.getProperty(PROP_UPDATE_ROOT_PATH, DEFAULT_UPDATE_ROOT_PATH);
		System.out.print("Please input the target directory[" + targetDir + "]: ");
		answer = Agloco.in.readLine();
		if (!answer.equals("")) targetDir = answer;
		do {
			System.out.print("Please input the viewbarId: ");
			viewbarId = Agloco.in.readLine();
		} while(viewbarId.equals(""));
		System.out.print("Please input the patch zip filename[" + zipFile + "]: ");
		answer = Agloco.in.readLine();
		if (!answer.equals("")) zipFile = answer;
		System.out.print("Please input the root path of viewbar update[" + rootPath + "]: ");
		answer = Agloco.in.readLine();
		if (!answer.equals("")) rootPath = answer;

		ViewbarService service = (ViewbarService) _ctx.getBean(ViewbarService.class.getName());
		VBViewbar viewbar = new VBViewbar();
		viewbar.setViewbarId(viewbarId);
		viewbar.setRootPath(rootPath);
		viewbar.setOsVersion("windows");
		viewbar.setCreateDate(new Date());
		viewbar.setReleaseDate(new Date());
		viewbar.setExpireDate(null);
		viewbar.setDownloadCount(new Long(0));
		viewbar.setDownloadSCount(new Long(0));
		service.updateViewbar(viewbar, zipFile, targetDir);
	}
}
