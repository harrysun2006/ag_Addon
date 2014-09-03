package com.amaxgs.addon.service;

import com.amaxgs.addon.model.VBFile;
import com.amaxgs.addon.model.VBViewbar;

public interface ViewbarService {

	public void updateViewbar(VBViewbar viewbar, String zipFile, String targetDir) throws Exception;

	public void saveViewbar(VBViewbar viewbar) throws Exception;

	public void saveViewbarFile(VBFile file) throws Exception;
}
