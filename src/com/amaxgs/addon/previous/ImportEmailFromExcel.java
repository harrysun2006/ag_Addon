package com.amaxgs.addon.previous;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.agloco.Constants;
import com.agloco.model.AGMemberTemp;
import com.agloco.service.util.MemberServiceUtil;
import com.agloco.util.Generator;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.spring.util.SpringUtil;

class ImportEmailFromExcel {

	private final static String COMPANY_ID = "agloco.com";

	private final static Locale locale = Locale.getDefault();

	private final static String SESSION_FACTORY = "liferaySessionFactory";

	private static String getPath() {
		String path = ImportEmailFromExcel.class.getClassLoader().getResource(".").getPath();
		return path;
	}

	public static void importEmail() throws Exception {
		String path = getPath() + "emailList.xls";
		
		SessionFactory sessionFactory = (SessionFactory) SpringUtil.getContext()
				.getBean(SESSION_FACTORY);
		Session session = null;
		try {
			
			session = SessionFactoryUtils.getSession(sessionFactory, true);
			session.setFlushMode(FlushMode.NEVER);
			TransactionSynchronizationManager.bindResource(sessionFactory,
					new SessionHolder(session));

			Workbook book = Workbook.getWorkbook(new File(path));
			System.out.println("open " + path + " successfully");
			// get first sheet
			Sheet sheet = book.getSheet(0);
			int rows = sheet.getRows();
			int columns = sheet.getColumns();
			System.out.println("sheet rows: " + rows + " column is :" + columns);
			if (rows < 2 || columns < 1) {
				return;
			}
			if (columns != 5) {
				System.out.println("invalidate data!");
			}

			int referralNum = 0;
			int insertNum = 0;
			insertNum = insert(sheet, rows, columns, referralNum, insertNum);

		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		} finally {
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			SessionFactoryUtils.releaseSession(session, sessionFactory);
		}
	}

	private static int insert(Sheet sheet, int rows, int columns, int referralNum,
			int insertNum) throws Exception {

		for (int i = 1; i < rows; i++) {
			AGMemberTemp temp = new AGMemberTemp();
			temp.setLastMailTime(Calendar.getInstance());
			temp.setPassword(Generator.generatePassword());
			temp.setMemberCode(Generator.generateMemberCode(AGMemberTemp.class
					.getName()));
			temp.setCreateDate(Calendar.getInstance());
			temp.setMailCount(new Integer(1));
			for (int j = 0; j < columns; j++) {
				Cell c = sheet.getCell(j, i);
				String contents = c.getContents();

				if (j == 0) {
					temp.setCountry(contents);
				} else if (j == 2) {
					String[] names = StringUtils.splitPreserveAllTokens(contents, " ");
					if (names.length == 1) {
						temp.setFirstName(names[0]);
						temp.setLastName("agloco");
					} else if (names.length == 2) {
						temp.setFirstName(names[0]);
						temp.setLastName(names[1]);
					} else {
						temp.setFirstName(names[0]);
						temp.setMiddleName(names[1]);
						temp.setLastName(names[2]);
					}
				} else if (j == 3) {
					temp.setEmailAddress(contents);
				} else if (j == 4) {

					if (!"direct".equals(contents)) {
						temp.setReferralCode(getReferralMemberCode(sheet, contents,
								referralNum));
						++referralNum;
					}
				}

			}

			User user = MemberServiceUtil.addUserMemberTemp(COMPANY_ID, true, "",
					false, "agloco", "agloco", false, Constants.DEFAULT_MAIL, locale,
					temp.getFirstName(), temp.getMiddleName(), temp.getLastName(), "",
					"", "", true, 0, 0, 0, "", "", "", true, temp);

			MemberServiceUtil.updateAgreedToTermsOfUse(user.getUserId(), true);
			System.out.println("insert member success, the number is: "
					+ (++insertNum));
			System.out.println(temp.getUserId() + " " + temp.getEmailAddress()
					+ " " + temp.getReferralCode());
		}
		System.out.println("insert member total number is: " + (insertNum));
		System.out
				.println("have referral member total number is: " + (referralNum));
		return insertNum;

	}

	private static String getReferralMemberCode(Sheet sheet, String referralId,
			int referralNum) {

		int rows = sheet.getRows();
		String emailAddress = null;
		for (int i = 1; i < rows; i++) {

			Cell c = sheet.getCell(1, i);
			String contents = c.getContents();
			if (StringUtils.isNotBlank(referralId) && referralId.equals(contents)) {
				emailAddress = sheet.getCell(3, i).getContents();
				break;
			}

		}
		if (StringUtils.isNotBlank(emailAddress)) {
			if (MemberServiceUtil.getAGMemberByEmail(emailAddress) != null) {
				return MemberServiceUtil.getAGMemberByEmail(emailAddress)
						.getMemberCode();
			}
			if (MemberServiceUtil.getAGMemberTempByEmail(emailAddress) != null) {
				return MemberServiceUtil.getAGMemberTempByEmail(emailAddress)
						.getMemberCode();
			}
		}
		return null;
	}
}
