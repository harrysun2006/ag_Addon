package com.agloco.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agloco.exception.FullMemberCodeException;
import com.agloco.exception.InvalidateMaxMemberCodeExcetion;
import com.agloco.model.AGCounter;
import com.agloco.service.CodeService;
import com.agloco.service.dao.util.CounterDaoUtil;
import com.agloco.util.WordUtil;

public class SequenceCodeServiceImpl implements CodeService {

	private static int mcLength = 8; // member code length
	private static int maxNumber = 9999;
	private static String maxText = "ZZZZ";
	private static int A_ASCII = (int) 'A';
	private static int Z_ASCII = (int) 'Z';
	private static Pattern p = Pattern.compile("[A-Z]{4}\\d{4}");

	public String generateCode(String code) {
		StringBuffer sb = new StringBuffer(mcLength);
		if (code == null) {
			throw new InvalidateMaxMemberCodeExcetion();
		}
		Matcher m = p.matcher(code);
		if (!m.matches()) {
			throw new InvalidateMaxMemberCodeExcetion();
		}
		String textPart = code.substring(0, mcLength / 2);
		String numberPart = code.substring(mcLength / 2, mcLength);
		int _numberPart = Integer.parseInt(numberPart);
		while (WordUtil.isBad(textPart) || WordUtil.isReserved(textPart)
				|| _numberPart == maxNumber) {
			if (maxText.equals(textPart))
				throw new FullMemberCodeException();
			textPart = nextTextPart(textPart);
			_numberPart = 0;
		}
		sb.append(textPart).append(nextNumberPart(_numberPart));
		return sb.toString();
	}

	private static String nextTextPart(String textPart) {
		// string convert to byte[],A->65...Z-90
		byte[] b = textPart.getBytes();
		for (int i = b.length - 1; i >= 0; i--) {
			if (b[i] < Z_ASCII) {
				b[i] += 1;
				for (int j = i + 1; j < b.length; j++) {
					b[j] = (byte) A_ASCII;
				}
				break;
			}
		}

		// byte[] convert to char[],65->A...90->Z
		char[] c = new char[mcLength / 2];
		for (int i = 0; i < c.length; i++) {
			c[i] = (char) b[i];
		}
		return new String(c);
	}

	private static String nextNumberPart(int _numberPart) {
		_numberPart++;
		String s = String.valueOf(_numberPart);
		int nLength = s.length();
		for (int i = 0; i < mcLength / 2 - nLength; i++) {
			s = "0" + s;
		}
		return s;

	}
	
	public String generateUserId(){
		return CounterDaoUtil.generateUserId();
	}

	
	//add at 2007-03-05
	public String generateAdminMemberCode(){
		return CounterDaoUtil.generateAdminMemberCode();
	}
	public String generateAdminUserId(){
		return CounterDaoUtil.generateAdminUserId();
	}
	public AGCounter getAGCounter(String pk){
		return CounterDaoUtil.getAGCounter(pk);
	}
}
