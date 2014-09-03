package com.agloco.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agloco.model.AGBadword;
import com.agloco.service.util.CommonServiceUtil;
import com.liferay.portal.util.ClusterPool;
import com.liferay.util.StringPool;
import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class WordUtil {

	
	private static Pattern[] reservedPatterns = null;
	private static GeneralCacheAdministrator _cache = ClusterPool.getCache();
	private final static String CACHE_BAD_WORDS_LIST_KEY = "bad.words.list.key";
	public static final String GROUP_NAME = WordUtil.class.getName();
	public static final String[] GROUP_NAME_ARRAY = new String[] {GROUP_NAME};

	private WordUtil() {
		
	}
	
	private synchronized static Pattern[] reloadBadPatterns() {
		String key = _encodeKey(CACHE_BAD_WORDS_LIST_KEY);
		Pattern[] badPatterns = null;
		try {
			badPatterns = (Pattern[]) _cache.getFromCache(key);
		}
		catch (NeedsRefreshException e) {
			List badwords = CommonServiceUtil.listAGBadword();
			if(badwords != null && badwords.size() > 0){
				badPatterns = new Pattern[badwords.size()];
				for(int i = 0; i < badwords.size(); i++) {
					AGBadword badword = (AGBadword) badwords.get(i);
					badPatterns[i] = Pattern.compile(badword.getWord());
				}
				_cache.putInCache(key, badPatterns,GROUP_NAME_ARRAY);
			}
		} 
		finally {
			if (badPatterns == null || badPatterns.length < 1) {
				_cache.cancelUpdate(key);
			}
		}
		return badPatterns;
		
	}
	
	protected static void clearWordUtilPool() {
		_cache.flushGroup(GROUP_NAME);
	}

	protected static void clearWordUtilPool(String key) {
		key = _encodeKey(key);
		_cache.flushEntry(key);
	}	
	
	private static String _encodeKey(String key) {
		return GROUP_NAME + StringPool.POUND + key;
	}

	private synchronized static Pattern[] getBadPatterns() {
		return reloadBadPatterns();
	}

	private synchronized static void reloadReservedPatterns() {
		reservedPatterns = new Pattern[] {
				Pattern.compile("AGLO.*"),
		};
	}

	private synchronized static Pattern[] getReservedPatterns() {
		if(reservedPatterns == null) reloadReservedPatterns();
		return reservedPatterns;
	}

	public static boolean isBad(String word) {
		return lookupPatterns(getBadPatterns(), word) >= 0;
	}

	public static boolean isReserved(String word) {
		return lookupPatterns(getReservedPatterns(), word) >= 0;
	}

	private static int lookupPatterns(Pattern[] patterns, String word) {
		if(patterns == null) return -1;
		int i = 0;
		Matcher m;
		for(i = 0; i < patterns.length; i++) {
			m = patterns[i].matcher(word);
			if(m.find()) return i;
		}
		return -1;
	}
}
