package com.agloco.util;

/*
 * Copyright 2001,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtil {

	private static Log log = LogFactory.getLog(StringUtil.class);

	public static final String EMPTY_STRING="";
	public static final char DOT='.';
	public static final char UNDERSCORE='_';
	public static final String COMMA_SPACE=", ";
	public static final String COMMA = ",";
	public static final String OPEN_PAREN = "(";
	public static final String CLOSE_PAREN = ")";
	public static final char SINGLE_QUOTE = '\'';

	/**
	 * Line separator for the OS we are operating on.
	 */
	private static final String EOL = System.getProperty("line.separator");
    
	/**
	 * Length of the line separator.
	 */
	private static final int EOL_LENGTH = EOL.length();

	/* static methods only - hide constructor */
	public StringUtil() {}

	/**
	 * Concatenates a list of objects as a String.
	 *
	 * @param list The list of objects to concatenate.
	 * @return     A text representation of the concatenated objects.
	 */
	private String concat(List list)
	{
		StringBuffer sb = new StringBuffer();
		int size = list.size();

		for (int i = 0; i < size; i++)
		{
			sb.append(list.get(i).toString());
		}
		return sb.toString();
	}

	/**
	 * Return a package name as a relative path name
	 *
	 * @param String package name to convert to a directory.
	 * @return String directory path.
	 */
	static public String getPackageAsPath(String pckge)
	{
		return pckge.replace( '.', File.separator.charAt(0) ) + File.separator;
	}

	/**
	 * <p>
	 * Remove underscores from a string and replaces first
	 * letters with capitals.  Other letters are changed to lower case. 
	 * </p>
	 *
	 * <p> 
	 * For example <code>foo_bar</code> becomes <code>FooBar</code>
	 * but <code>foo_barBar</code> becomes <code>FooBarbar</code>.
	 * </p>
	 *
	 * @param data string to remove underscores from.
	 * @return String 
	 * @deprecated Use the org.apache.commons.util.StringUtils class
	 * instead.  Using its firstLetterCaps() method in conjunction
	 * with a StringTokenizer will achieve the same result.
	 */
	static public String removeUnderScores (String data)
	{
		String temp = null;
		StringBuffer out = new StringBuffer();
		temp = data;

		StringTokenizer st = new StringTokenizer(temp, "_");
       
		while (st.hasMoreTokens())
		{
			String element = (String) st.nextElement();
			out.append ( firstLetterCaps(element));
		}

		return out.toString();
	}

	/**
	 * <p> 
	 *  'Camels Hump' replacement of underscores.
	 * </p>
	 *
	 * <p> 
	 * Remove underscores from a string but leave the capitalization of the
	 * other letters unchanged.
	 * </p>
	 *
	 * <p> 
	 * For example <code>foo_barBar</code> becomes <code>FooBarBar</code>.
	 * </p>
	 *
	 * @param data string to hump
	 * @return String 
	 */
	static public String removeAndHump (String data)
	{
		return removeAndHump(data,"_");
	}

	/**
	 * <p>
	 * 'Camels Hump' replacement.
	 * </p>
	 *
	 * <p> 
	 * Remove one string from another string but leave the capitalization of the
	 * other letters unchanged.
	 * </p>
	 *
	 * <p>
	 * For example, removing "_" from <code>foo_barBar</code> becomes <code>FooBarBar</code>.
	 * </p>
	 *
	 * @param data string to hump
	 * @param replaceThis string to be replaced
	 * @return String 
	 */
	static public String removeAndHump (String data,String replaceThis)
	{
		String temp = null;
		StringBuffer out = new StringBuffer();
		temp = data;

		StringTokenizer st = new StringTokenizer(temp, replaceThis);
       
		while (st.hasMoreTokens())
		{
			String element = (String) st.nextElement();
			out.append ( capitalizeFirstLetter(element));
		}//while
        
		return out.toString();
	}

	/**
	 * <p> 
	 *  Makes the first letter caps and the rest lowercase.
	 * </p>
	 *
	 * <p> 
	 *  For example <code>fooBar</code> becomes <code>Foobar</code>.
	 * </p>
	 *
	 * @param data capitalize this
	 * @return String
	 */
	static public String firstLetterCaps ( String data )
	{
		String firstLetter = data.substring(0,1).toUpperCase();
		String restLetters = data.substring(1).toLowerCase();
		return firstLetter + restLetters;
	}

	/**
	 * <p> 
	 * Capitalize the first letter but leave the rest as they are. 
	 * </p>
	 *
	 * <p> 
	 *  For example <code>fooBar</code> becomes <code>FooBar</code>.
	 * </p>
	 *
	 * @param data capitalize this
	 * @return String
	 */
	static public String capitalizeFirstLetter ( String data )
	{
		String firstLetter = data.substring(0,1).toUpperCase();
		String restLetters = data.substring(1);
		return firstLetter + restLetters;
	}

	/**
	 * Chop i characters off the end of a string.
	 * This method assumes that any EOL characters in String s 
	 * and the platform EOL will be the same.
	 * A 2 character EOL will count as 1 character. 
	 *
	 * @param string String to chop.
	 * @param i Number of characters to chop.
	 * @return String with processed answer.
	 */
	public static String chop(String s, int i)
	{
		return chop(s, i, EOL);
	}

	/**
	 * Chop i characters off the end of a string. 
	 * A 2 character EOL will count as 1 character. 
	 *
	 * @param string String to chop.
	 * @param i Number of characters to chop.
	 * @param eol A String representing the EOL (end of line).
	 * @return String with processed answer.
	 */
	public static String chop(String s, int i, String eol)
	{
		if ( i == 0 || s == null || eol == null )
		{
		   return s;
		}

		int length = s.length();

		/*
		 * if it is a 2 char EOL and the string ends with
		 * it, nip it off.  The EOL in this case is treated like 1 character
		 */
		if ( eol.length() == 2 && s.endsWith(eol ))
		{
			length -= 2;
			i -= 1;
		}

		if ( i > 0)
		{
			length -= i;
		}

		if ( length < 0)
		{
			length = 0;
		}

		return s.substring( 0, length);
	}

	public static StringBuffer stringSubstitution( String argStr,
												   Hashtable vars )
	{
		return stringSubstitution( argStr, (Map) vars );
	}

	/**
	 * Perform a series of substitutions. The substitions
	 * are performed by replacing $variable in the target
	 * string with the value of provided by the key "variable"
	 * in the provided hashtable.
	 *
	 * @param String target string
	 * @param Hashtable name/value pairs used for substitution
	 * @return String target string with replacements.
	 */
	public static StringBuffer stringSubstitution(String argStr,
			Map vars)
	{
		StringBuffer argBuf = new StringBuffer();

		for (int cIdx = 0 ; cIdx < argStr.length();)
		{
			char ch = argStr.charAt(cIdx);

			switch (ch)
			{
				case '$':
					StringBuffer nameBuf = new StringBuffer();
					for (++cIdx ; cIdx < argStr.length(); ++cIdx)
					{
						ch = argStr.charAt(cIdx);
						if (ch == '_' || Character.isLetterOrDigit(ch))
							nameBuf.append(ch);
						else
							break;
					}

					if (nameBuf.length() > 0)
					{
						String value =
								(String) vars.get(nameBuf.toString());

						if (value != null)
						{
							argBuf.append(value);
						}
					}
					break;

				default:
					argBuf.append(ch);
					++cIdx;
					break;
			}
		}

		return argBuf;
	}
    
	/**
	 * Read the contents of a file and place them in
	 * a string object.
	 *
	 * @param String path to file.
	 * @return String contents of the file.
	 */
	public static String fileContentsToString(String file)
	{
		String contents = "";
        
		File f = new File(file);
        
		if (f.exists())
		{
			try
			{
				FileReader fr = new FileReader(f);
				int size = (int)f.length();
				int len = 0, off = 0;
				char[] template = new char[size];
				do
				{
					len = fr.read(template, off, size - off);
					if(len > 0) off += len;
				} while(len > 0);
				contents = new String(template);
			}
			catch (Exception e)
			{
				System.out.println(e);
				e.printStackTrace();
			}
		}
        
		return contents;
	}
    
	/**
	 * Remove/collapse multiple newline characters.
	 *
	 * @param String string to collapse newlines in.
	 * @return String
	 */
	public static String collapseNewlines(String argStr)
	{
		char last = argStr.charAt(0);
		StringBuffer argBuf = new StringBuffer();

		for (int cIdx = 0 ; cIdx < argStr.length(); cIdx++)
		{
			char ch = argStr.charAt(cIdx);
			if (ch != '\n' || last != '\n')
			{
				argBuf.append(ch);
				last = ch;
			}
		}

		return argBuf.toString();
	}

	/**
	 * Remove/collapse multiple spaces.
	 *
	 * @param String string to remove multiple spaces from.
	 * @return String
	 */
	public static String collapseSpaces(String argStr)
	{
		char last = argStr.charAt(0);
		StringBuffer argBuf = new StringBuffer();

		for (int cIdx = 0 ; cIdx < argStr.length(); cIdx++)
		{
			char ch = argStr.charAt(cIdx);
			if (ch != ' ' || last != ' ')
			{
				argBuf.append(ch);
				last = ch;
			}
		}

		return argBuf.toString();
	}

	/**
	  * Replaces all instances of oldString with newString in line.
	  * Taken from the Jive forum package.
	  *
	  * @param String original string.
	  * @param String string in line to replace.
	  * @param String replace oldString with this.
	  * @return String string with replacements.
	  */
	public static final String sub(String line, String oldString,
			String newString)
	{
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0)
		{
			char [] line2 = line.toCharArray();
			char [] newString2 = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j = i;
			while ((i = line.indexOf(oldString, i)) > 0)
			{
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
				j = i;
			}
			buf.append(line2, j, line2.length - j);
			return buf.toString();
		}
		return line;
	}
    
	/**
	 * Returns the output of printStackTrace as a String.
	 *
	 * @param e A Throwable.
	 * @return A String.
	 */
	public static final String stackTrace(Throwable e)
	{
		String foo = null;
		try
		{
			// And show the Error Screen.
			ByteArrayOutputStream ostr = new ByteArrayOutputStream();
			e.printStackTrace( new PrintWriter(ostr,true) );
			foo = ostr.toString();
		}
		catch (Exception f)
		{
			// Do nothing.
		}
		return foo;
	}

	/**
	 * Return a context-relative path, beginning with a "/", that represents
	 * the canonical version of the specified path after ".." and "." elements
	 * are resolved out.  If the specified path attempts to go outside the
	 * boundaries of the current context (i.e. too many ".." path elements
	 * are present), return <code>null</code> instead.
	 *
	 * @param path Path to be normalized
	 * @return String normalized path
	 */
	public static final String normalizePath(String path)
	{
		// Normalize the slashes and add leading slash if necessary
		String normalized = path;
		if (normalized.indexOf('\\') >= 0)
		{
			normalized = normalized.replace('\\', '/');
		}

		if (!normalized.startsWith("/"))
		{
			normalized = "/" + normalized;
		}
        
		// Resolve occurrences of "//" in the normalized path
		while (true)
		{
			int index = normalized.indexOf("//");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) +
			normalized.substring(index + 1);
		}

		// Resolve occurrences of "%20" in the normalized path
		while (true)
		{
			int index = normalized.indexOf("%20");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + " " +
			normalized.substring(index + 3);
		}

		// Resolve occurrences of "/./" in the normalized path
		while (true)
		{
			int index = normalized.indexOf("/./");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) +
			normalized.substring(index + 2);
		}

		// Resolve occurrences of "/../" in the normalized path
		while (true)
		{
			int index = normalized.indexOf("/../");
			if (index < 0)
				break;
			if (index == 0)
				return (null);  // Trying to go outside our context
			int index2 = normalized.lastIndexOf('/', index - 1);
			normalized = normalized.substring(0, index2) +
			normalized.substring(index + 3);
		}

		// Return the normalized path that we have completed
		return (normalized);
	}

	/**
	 * If state is true then return the trueString, else
	 * return the falseString.
	 *
	 * @param boolean 
	 * @param String trueString
	 * @param String falseString
	 */
	private String select(boolean state, String trueString, String falseString)
	{
		if (state)
		{
			return trueString;
		}            
		else
		{
			return falseString;
		}            
	}            

	/**
	 * Check to see if all the string objects passed
	 * in are empty.
	 *
	 * @param list A list of {@link java.lang.String} objects.
	 * @return     Whether all strings are empty.
	 */
	private boolean allEmpty(List list)
	{
		int size = list.size();
        
		for (int i = 0; i < size; i++)
		{
			if (list.get(i) != null && list.get(i).toString().length() > 0)
			{
				return false;
			}
		}            
		return true;
	}

	public static void main(String[] args)
	{
		final String s = "Re: Re: QQ''<&amp;gt;>\"Da Vinci Code\"";
		log.debug("s = " + s);
		log.debug("replace(s, & ==> &amp; < ==> &lt; > ==> &gt; \\r\\n ==> <BR>) = " + 
			replace(s, new String[]{"&", "&amp;", "<", "&lt;", ">", "&gt;", "\r\n", "<BR>"}));
		log.debug("replace(s, &gt; ==> > &lt; ==> < &amp; ==> &) = " + 
			replace(s, new String[]{"&gt;", ">", "&lt;", "<", "&amp;", "&"}));
	}

	public static String URLEncode(String s)
	{
		String result = null;
		try{
			result = URLEncoder.encode(s, "UTF-8");
		}
		catch(UnsupportedEncodingException uee) {
    		
		}
		finally {
			return result;
		}
	}

	public static String URLDecode(String s)
	{
		String result = null;
		try {
			result = URLDecoder.decode(s, "UTF-8");
		}
		catch(UnsupportedEncodingException uee) {
		}
		finally {
			return result;
		}
	}

	private static final String[] HTML_ENCODE_PATTERNS = {"&", "&amp;", "<", "&lt;", ">", "&gt;", "\r\n", "<BR>"};
	private static final String[] HTML_DECODE_PATTERNS = {"&gt;", ">", "&lt;", "<", "&nbsp;", " ", "&amp;", "&"};
	private static final String[] VALUE_ENCODE_PATTERNS = {"&", "&amp;", "<", "&lt;", ">", "&gt;", "\"", "&quot;"};
	private static final String[] VALUE_DECODE_PATTERNS = {"&quot;", "\"", "&gt;", ">", "&lt;", "<", "&amp;", "&"};
	private static final String[] SQL_ENCODE_PATTERNS = {"'", "\""};
	private static final String[] DQM_ENCODE_PATTERNS = {"\"", "\\\""};
	private static final String[] SQM_ENCODE_PATTERNS = {"'", "\\\'"};
	private static final String[] JS_ENCODE_PATTERNS = {"\"", "\\\"", "\r", "", "\n", "\\n", "/", "\\/"};

	public static String HTMLEncode(final String s)
	{
		return replace(s, HTML_ENCODE_PATTERNS);
	}

	public static String HTMLDecode(final String s)
	{
		return replace(s, HTML_DECODE_PATTERNS);
	}

	public static String ValueEncode(final String s)
	{
		return replace(s, VALUE_ENCODE_PATTERNS);
	}

	public static String ValueDecode(final String s)
	{
		return replace(s, VALUE_DECODE_PATTERNS);
	}

	public static String SQLEncode(final String s)
	{
		return replace(s, SQL_ENCODE_PATTERNS);
	}

	public static String DQMEncode(final String s)
	{
		return replace(s, DQM_ENCODE_PATTERNS);
	}

	public static String SQMEncode(final String s)
	{
		return replace(s, SQM_ENCODE_PATTERNS);
	}

	public static String JSEncode(final String s)
	{
		return replace(s, JS_ENCODE_PATTERNS);
	}

	/**
	 * ���ַ��н��ж��ģʽ�Ĳ����滻
	 * @param s ��Ҫ���в����滻��Դ�ַ�
	 * @param patterns ģʽ: �����ַ�, �滻�ַ�, ... ...���
	 * @return
	 */
	public static String replace(final String s, List patterns)
	{
		if(s == null) return "";
		String[] search = null;
		String[] replace = null;
		int i, count;
		if(patterns != null)
		{
			count = (patterns.size() + 1) / 2;
			search = new String[count];
			replace = new String[count];
			replace[count-1] = "";
			for(i = 0; i < patterns.size(); i++)
			{
				if(i % 2 == 0) search[i/2] = patterns.get(i).toString();
				else replace[i/2] = patterns.get(i).toString();
			}
		}
		return replace(s, search, replace);
	}

	/**
	 * ���ַ��н��ж��ģʽ�Ĳ����滻
	 * @param s ��Ҫ���в����滻��Դ�ַ�
	 * @param patterns ģʽ: �����ַ�, �滻�ַ�, ... ...���
	 * @return
	 */
	public static String replace(final String s, String[] patterns)
	{
		if(s == null) return "";
		String[] search = null;
		String[] replace = null;
		int i, count;
		if(patterns != null)
		{
			count = (patterns.length + 1) / 2;
			search = new String[count];
			replace = new String[count];
			replace[count-1] = "";
			for(i = 0; i < patterns.length; i++)
			{
				if(i % 2 == 0) search[i/2] = patterns[i];
				else replace[i/2] = patterns[i];
			}
		}
		return replace(s, search, replace);
	}

	private static String replace(final String s, String[] search, String[] replace)
	{
		if(s == null) return "";
		StringBuffer sb = new StringBuffer(s.length());
		int i, index;
		if(search != null)
		{
			i = 0;
			while(i < s.length())
			{
				index = lookup(s, i, search);
				if(index >= 0)
				{
					sb.append(replace[index]);
					i += search[index].length();
				} else {
					sb.append(s.charAt(i));
					i++;
				} 
			}
		}
		return sb.toString();
	}

	private static int lookup(final String s, int index, String[] patterns)
	{
		int count = 0;
		boolean[] flags;
		char c;
		if(patterns != null && patterns.length > 0)
		{
			flags = new boolean[patterns.length];
			for(int i = 0; i < flags.length; i++) flags[i] = true;
			int pos = 0;
			int max = s.length() - index;
			while(count < patterns.length && pos < max)
			{
				c = s.charAt(pos + index);
				for(int k = 0 ; k < patterns.length; k++)
				{
					if(flags[k])
					{
						if(pos < patterns[k].length() && c == patterns[k].charAt(pos))
						{
							if(pos == patterns[k].length() - 1) return k;
						} else {
							flags[k] = false;
							count ++;
						} 
					}
				}
				pos++;
			}
		}
		return -1;
	}

	public static String join(String seperator, String[] strings) {
		int length = strings.length;
		if (length==0) return EMPTY_STRING;
		StringBuffer buf = new StringBuffer( length * strings[0].length() )
		.append(strings[0]);
		for (int i=1; i<length; i++) {
			buf.append(seperator).append(strings[i]);
		}
		return buf.toString();
	}

	public static String join(String seperator, Iterator objects) {
		StringBuffer buf = new StringBuffer();
		if ( objects.hasNext() ) buf.append( objects.next() );
		while ( objects.hasNext() ) {
			buf.append(seperator).append( objects.next() );
		}
		return buf.toString();
	}

	public static String[] add(String[] x, String sep, String[] y) {
		String[] result = new String[ x.length ];
		for ( int i=0; i<x.length; i++ ) {
			result[i] = x[i] + sep + y[i];
		}
		return result;
	}

	public static String repeat(String string, int times) {
		StringBuffer buf = new StringBuffer( string.length() * times );
		for (int i=0; i<times; i++) buf.append(string);
		return buf.toString();
	}

	public static String replace(String template, String placeholder, String replacement) {
		return replace(template, placeholder, replacement, false);
	}

	public static String replace(String template, String placeholder, String replacement, boolean wholeWords) {
		int loc = template.indexOf(placeholder);
		if (loc<0) {
			return template;
		}
		else {
			final boolean actuallyReplace = !wholeWords ||
				loc + placeholder.length() == template.length() ||
				!Character.isJavaIdentifierPart( template.charAt( loc + placeholder.length() ) );
			String actualReplacement = actuallyReplace ? replacement : placeholder;
			return new StringBuffer( template.substring(0, loc) )
				.append(actualReplacement)
				.append( replace(
					template.substring( loc + placeholder.length() ),
					placeholder,
					replacement,
					wholeWords
				) ).toString();
		}
	}

	public static String replaceOnce(String template, String placeholder, String replacement) {
		int loc = template.indexOf(placeholder);
		if ( loc<0 ) {
			return template;
		}
		else {
			return new StringBuffer( template.substring(0, loc) )
			.append(replacement)
			.append( template.substring( loc + placeholder.length() ) )
			.toString();
		}
	}

	public static String[] split(String seperators, String list) {
		return split(seperators, list, false);
	}

	public static String[] split(String seperators, String list, boolean include) {
		StringTokenizer tokens = new StringTokenizer(list, seperators, include);
		String[] result = new String[ tokens.countTokens() ];
		int i=0;
		while ( tokens.hasMoreTokens() ) {
			result[i++] = tokens.nextToken();
		}
		return result;
	}

	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, ".");
	}

	public static String unqualify(String qualifiedName, String seperator) {
		return qualifiedName.substring( qualifiedName.lastIndexOf(seperator) + 1 );
	}

	public static String qualifier(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf(".");
		if ( loc<0 ) {
			return EMPTY_STRING;
		}
		else {
			return qualifiedName.substring(0, loc);
		}
	}

	public static String[] suffix( String[] columns, String suffix) {
		if (suffix==null) return columns;
		String[] qualified = new String[columns.length];
		for ( int i=0; i<columns.length; i++ ) {
			qualified[i] = suffix( columns[i], suffix);
		}
		return qualified;
	}

	public static String suffix(String name, String suffix) {
		return (suffix==null) ? name : name + suffix;
	}

	public static String[] prefix( String[] columns, String prefix) {
		if (prefix==null) return columns;
		String[] qualified = new String[columns.length];
		for ( int i=0; i<columns.length; i++ ) {
			qualified[i] = prefix + columns[i];
		}
		return qualified;
	}

	public static String root(String qualifiedName) {
		int loc = qualifiedName.indexOf(".");
		return (loc<0) ? qualifiedName : qualifiedName.substring(0, loc);
	}

	public static boolean booleanValue(String tfString) {
		String trimmed = tfString.trim().toLowerCase();
		return trimmed.equals("true") || trimmed.equals("t");
	}

	public static String toString(Object[] array) {
		int len = array.length;
		if (len==0) return StringUtil.EMPTY_STRING;
		StringBuffer buf = new StringBuffer(len * 12);
		for ( int i=0; i<len-1; i++ ) {
			buf.append( array[i] ).append(StringUtil.COMMA_SPACE);
		}
		return buf.append( array[len-1] ).toString();
	}

	public static String[] multiply(String string, Iterator placeholders, Iterator replacements) {
		String[] result = new String[] { string };
		while ( placeholders.hasNext() ) {
			result = multiply( result, (String) placeholders.next(), (String[]) replacements.next() );
		}
		return result;
	}

	private static String[] multiply(String[] strings, String placeholder, String[] replacements) {
		String[] results = new String[ replacements.length * strings.length ];
		int n=0;
		for ( int i=0; i<replacements.length; i++ ) {
			for ( int j=0; j<strings.length; j++ ) {
				results[n++] = replaceOnce( strings[j], placeholder, replacements[i] );
			}
		}
		return results;
	}

	/*public static String unQuote(String name) {
		return ( Dialect.QUOTE.indexOf( name.charAt(0) ) > -1 ) ?
		name.substring(1, name.length()-1) :
		name;
	}

	public static void unQuoteInPlace(String[] names) {
		for ( int i=0; i<names.length; i++ ) names[i] = unQuote( names[i] );
	}

	public static String[] unQuote(String[] names) {
		String[] unquoted = new String[ names.length ];
		for ( int i=0; i<names.length; i++ ) unquoted[i] = unQuote( names[i] );
		return unquoted;
	}*/

	public static int count(String string, char character) {
		int n=0;
		for ( int i=0; i<string.length(); i++ ) {
			if ( string.charAt(i)==character ) n++;
		}
		return n;
	}

	public static int countUnquoted(String string, char character) {
		if ( SINGLE_QUOTE == character ) {
			throw new IllegalArgumentException("Unquoted count of quotes is invalid");
		}
		// Impl note: takes advantage of the fact that an escpaed single quote
		// embedded within a quote-block can really be handled as two seperate
		// quote-blocks for the purposes of this method...
		int count=0;
		int stringLength = string == null ? 0 : string.length();
		boolean inQuote = false;
		for ( int indx=0; indx<stringLength; indx++ ) {
			if ( inQuote ) {
				if ( SINGLE_QUOTE == string.charAt(indx) ) {
					inQuote = false;
				}
			}
			else if ( SINGLE_QUOTE == string.charAt(indx) ) {
				inQuote = true;
			}
			else if ( string.charAt(indx)==character ) {
				count++;
			}
		}
		return count;
	}

	public static boolean isNotEmpty(String string) {
		return string!=null && string.length() > 0;
	}

	public static String qualify(String prefix, String name) {
		if ( name.startsWith("'") ) return name;

		return new StringBuffer( prefix.length() + name.length() + 1 )
			.append(prefix)
			.append(DOT)
			.append(name)
			.toString();
	}

	public static String[] qualify(String prefix, String[] names) {
		if (prefix==null) return names;
		int len = names.length;
		String[] qualified = new String[len];
		for ( int i=0; i<len; i++) {
			qualified[i] = qualify( prefix, names[i] );
		}
		return qualified;
	}

	public static int firstIndexOfChar(String sqlString, String string, int startindex) {
		int matchAt=-1;
		for (int i = 0; i < string.length(); i++) {
			int curMatch  = sqlString.indexOf( string.charAt(i), startindex );
			if (curMatch >=0) {
				if (matchAt==-1) { // first time we find match!
					matchAt = curMatch;
				} 
				else {
					matchAt = Math.min(matchAt, curMatch);
				}
			}
		}
		return matchAt;
	}
	
	public static String truncate(String string, int length) {
		if (string.length()<=length) {
			return string;
		}
		else {
			return string.substring(0, length);
		}
	}

	public static String find(String source, String regex, int index)
	{
		String result = null;
		if(source != null && regex != null)
		{
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(source);
			boolean find = m.find(0);
			result = (find && m.groupCount() >= index) ? m.group(index) : null;
		}
		return result;
	}

	public static String replace(String source, String regex, int index, String replacement)
	{
		StringBuffer result = new StringBuffer();
		if(source != null && regex != null)
		{
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(source);
			boolean find = m.find(0);
			if(find && m.groupCount() >= index)
			{
				if(m.start(index) > 0) result.append(source.substring(0, m.start(index)));
				result.append(replacement);
				if(m.end(index) < source.length()) result.append(source.substring(m.end(index)));
			} else {
				result.append(source);
			}
		}
		return result.toString();
	}

	public static int indexOf(String[] ss, String s)
	{
		return indexOf(ss, s, true);
	}

	public static int indexOf(String[] ss, String s, boolean ignoreCase)
	{
		int i = -1;
		if(ss != null && s != null)
		{
			i = 0;
			while(i < ss.length)
			{
				if(ignoreCase)
					if(s.equalsIgnoreCase(ss[i])) break;
				else
					if(s.equals(ss[i])) break;
				i++;
			}
		}
		if(i == ss.length) i = -1;
		return i;
	}

	private static final int DEFAULT_RANDOM_STRING_LENGTH = 16;
	private static final String DEFAULT_RANDOM_STRING_BASE = "0123456789-abcdef@_.#$%^&*()";

	public static String random(String base, int length)
	{
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		if(length <= 0) length = DEFAULT_RANDOM_STRING_LENGTH;
		if(base == null || base.trim().length() < 2) base = DEFAULT_RANDOM_STRING_BASE;
		for(int i = 0; i < length; i++)
		{
			sb.append(base.charAt(r.nextInt(base.length())));
		}
		return sb.toString();
	}

	public static String trim(String src, char[] trims) {
		if(src == null) return null;
		if(trims == null || trims.length == 0) return src;
		String pattern = String.valueOf(trims);
		int begin = 0;
		int end = src.length() - 1;
		char c;
		while(begin < end) {
			c = src.charAt(begin);
			if(pattern.indexOf(c) >= 0) begin++;
			else break;
		}
		while(end > begin) {
			c = src.charAt(end);
			if(pattern.indexOf(c) >= 0) end--;
			else break;
		}
		return src.substring(begin, end + 1);
	}

	public static String format(Object o, String format) {
		if(o == null) return "";
		else if(Number.class.isInstance(o)) return format((Number)o, format);
		else if(Date.class.isInstance(o)) return format((Date)o, format);
		else if(Calendar.class.isInstance(o)) return format((Calendar)o, format);
		else return format(o.toString(), format);
	}

	private static String format(Number n, String format) {
		if(n == null) return "";
		NumberFormat f = new DecimalFormat(format);
		return f.format(n.doubleValue());
	}

	private static String format(Date d, String format) {
		if(d == null) return "";
		DateFormat f = new SimpleDateFormat(format);
		return f.format(d);
	}

	private static String format(Calendar c, String format) {
		return format(c.getTime(), format);
	}

	private static String format(String s, String format) {
		if(s == null) return "";
		MessageFormat f = new MessageFormat(format);
		return f.format(s);
	}
	
	public static String convertNullToBlank(String in){
		if(in == null){
			return StringUtils.EMPTY;
		}
		return in;
	}
}
