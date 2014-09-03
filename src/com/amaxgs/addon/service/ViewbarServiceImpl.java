package com.amaxgs.addon.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.amaxgs.addon.MD5HashUtil;
import com.amaxgs.addon.model.VBFile;
import com.amaxgs.addon.model.VBViewbar;

public class ViewbarServiceImpl implements ViewbarService {

	private ViewbarDaoHibernate viewbarDao;

	public void setViewbarDao(ViewbarDaoHibernate viewbarDao) {
		this.viewbarDao = viewbarDao;
	}

	public void updateViewbar(VBViewbar viewbar, String zipFile, String targetDir) throws Exception {
		String viewbarId = viewbar.getViewbarId();
		ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
		ZipEntry entry;
		// 
		File target = new File(targetDir + "/" + viewbarId);
		if (!target.exists()) {
			target.mkdirs();
			System.out.println(target.getPath() + " directory is created!");
		}
		while ((entry = zin.getNextEntry()) != null) {
			if (entry.isDirectory()) {
				File directory = new File(targetDir + "/" + viewbarId, entry.getName());
				if (!directory.exists()) directory.mkdirs();
			} else {
				File myFile = new File(entry.getName());
				File createFile = new File(targetDir + "/" + viewbarId + "/" + myFile.getPath() + ".tmp");
				System.out.print(createFile.getPath() + ": ");
				FileOutputStream fout = new FileOutputStream(createFile);
				DataOutputStream dout = new DataOutputStream(fout);
				DataInputStream dis = new DataInputStream(zin);
				byte[] b = new byte[(int) entry.getSize()];
				dis.readFully(b, 0, (int) entry.getSize());
				dout.write(b);
				dout.close();
				fout.close();
				System.out.print("unzipped...");
				VBFile file = new VBFile();
				file.setViewbarId(viewbarId);
				file.setFileName("/" + myFile.getPath().replace("\\", "/"));
				file.setFilePath("/" + viewbarId + "/" + myFile.getPath().replace("\\", "/") + ".tmp");
				file.setMd5(MD5HashUtil.hashCode(b));
				file.setDescription("");
				saveViewbarFile(file);
				System.out.println("saved!");
			}
			zin.closeEntry();
		}
		saveViewbar(viewbar);
		System.out.println("viewbar has been upgraded to " + viewbarId + " successfully!");
	}

	public void saveViewbar(VBViewbar viewbar) throws Exception {
		viewbarDao.saveViewbar(viewbar);
	}

	public void saveViewbarFile(VBFile file) throws Exception {
		viewbarDao.saveViewbarFile(file);
	}

	
}