package com.amaxgs.addon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class FileHelper {

	private final static Log _log = LogFactory.getLog(FileHelper.class);

	protected static Set readFile2Set(String fileName) throws IOException {
		Set set = new HashSet();
		try {
			File f = new File(fileName);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String email = null;
			while ((email = br.readLine()) != null) {
				if (StringUtils.isNotBlank(email.trim().toLowerCase())) {
					set.add(email.trim().toLowerCase());
				}
			}
			_log.info("read file: " + fileName + " into list successfully!");
		} catch (FileNotFoundException e) {
			_log.error("file: " + fileName + " not found!");
			throw e;
		} catch (IOException e) {
			_log.error("read file: " + fileName + " failed!");
			throw e;
		}
		return set;
	}

	protected static List readFile2List(String fileName) throws IOException {
		List list = new ArrayList();
		try {
			File f = new File(fileName);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String key = null;
			while ((key = br.readLine()) != null) {
				if (StringUtils.isNotBlank(key.trim().toLowerCase())) {
					list.add(key.trim().toLowerCase());
				}
			}
			fr.close();
			br.close();
			_log.info("Read file: " + fileName + " into list successfully!");
		} catch (FileNotFoundException e) {
			_log.error("File" + fileName + " not found!");
		} catch (IOException e) {
			_log.error("Read file: " + fileName + " failed!");
		}
		return list;
	}

	protected static String getCurrentPath() {
		String path = SendEmail.class.getClassLoader().getResource(".").getPath();
		return path;
	}

	protected static String guessFileName(String fileName) {
		String guessName = fileName;
		File file = new File(guessName);
		if (!file.exists()) {
			String currentPath = FileHelper.getCurrentPath();
			guessName = currentPath + fileName;
		}
		return guessName;
	}
}
