package com.agloco.util;

import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * @author harry.sun
 * mail: hysun@thorn-bird.com
 */
public class VelocityUtil {

	private static Log log = LogFactory.getLog(VelocityUtil.class);

	private static VelocityUtil instance = new VelocityUtil();

	private VelocityUtil() {
	}

	private static String doMerge(String in, VelocityContext context) {
		String r = "";
		try {
			StringWriter writer = new StringWriter();
			Velocity.evaluate(context, writer, VelocityUtil.class.getName(), in);
			r = writer.toString();			
		}	catch(Exception e) {
			log.error("Template evaluate error!", e);
			throw new RuntimeException(e);
		}
		return r;
	}

	public static String evaluate(String in, Map map) {
		String r = "";
		VelocityContext context = new VelocityContext();
		String key;
		for(Iterator keys = map.keySet().iterator(); keys.hasNext(); ) {
			key = (String) keys.next();
			context.put(key, map.get(key));
		}
		r = doMerge(in, context); 
		return r;
	}

	public static String evaluate(String in, String[] names, Object[] values) {
		Map map = new Hashtable();
		int	i, nl, vl;
		nl = (names == null) ? 0 : names.length;
		vl = (values == null) ? 0 : values.length;
		for(i = 0; i < nl; i++) {
			if(i < vl) map.put(names[i], values[i]);
		}
		return evaluate(in, map);
	}

	public static String evaluate(String in, String name, Object value) {
		return evaluate(in, new String[]{name}, new Object[]{value});
	}

}
