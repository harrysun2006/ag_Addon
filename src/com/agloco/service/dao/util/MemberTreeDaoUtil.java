package com.agloco.service.dao.util;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.agloco.model.AGMember;
import com.agloco.model.AGMemberTree;
import com.agloco.service.dao.MemberTreeDao;
import com.liferay.portal.spring.util.SpringUtil;

public class MemberTreeDaoUtil {

	private MemberTreeDao memberTreeDao;

	public static void addAGMemberTree(AGMemberTree mTree) {
		getMemberTreeDao().addAGMemberTree(mTree);
	}

	public static void updateAGMemberTree(AGMemberTree mTree) {
		getMemberTreeDao().updateAGMemberTree(mTree);
	}

	public static void deleteAGMemberTree(AGMemberTree mTree) {
		getMemberTreeDao().deleteAGMemberTree(mTree);
	}

	public static AGMemberTree getAGMemberTree(Long memberId) {
		return getMemberTreeDao().getAGMemberTree(memberId);
	}

	/**
	 * add a descendant member under memberId
	 * 
	 * @param member
	 *          the parent member(the referral)
	 * @param descendant
	 *          the sub member
	 */
	public static void addAGMember(AGMember member, AGMember descendant) {
		getMemberTreeDao().addAGMember(member, descendant);
	}

	/**
	 * get all ancestors of the given member
	 * 
	 * @param member
	 *          the given member
	 * @param status
	 *          criterion: ancestors' status
	 * @return
	 */
	public static List getAncestors(AGMember member, String status) {
		return getMemberTreeDao().getAncestors(member, status);
	}

	/**
	 * get the count of given member's children
	 * 
	 * @param member
	 *          the given member
	 * @param level
	 *          the max level
	 * @param children's
	 *          status
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public static long getChildrenCount(AGMember member, long level, String status) {
		return getMemberTreeDao().getChildrenCount(member, level, status);
	}

	/**
	 * get children count group by level, return a list like: {[1, 5], [2, 14],
	 * ... ... [5, 69]}
	 * 
	 * @param member
	 *          the given member
	 * @param children's
	 *          status
	 * @return
	 */
	public static List getChildrenCountByLevel(AGMember member, long level, String status) {
		return getMemberTreeDao().getChildrenCountByLevel(member, level, status);
	}

	/**
	 * get a partial subtree of the given member
	 * 
	 * @param member
	 *          the given member, use the follow componding criterions:
	 *          <li>memberId: parent's id, mandatory</li>
	 * @param level
	 *          the scope of the subtree, if <= 0, return max-scope subtree else
	 *          return 1~level subtree
	 * @param status
	 *          children's status criteria
	 * @param session
	 *          hibernate session
	 * @return
	 * @throws Exception
	 */
	public static List getChildren(AGMember member, long level, String status) {
		return getMemberTreeDao().getChildren(member, level, status);
	}

	/**
	 * get level-subtree of the given member
	 * 
	 * @param member
	 *          the given member, use the follow componding criterions:
	 *          <li>memberId: parent's id, mandatory</li>
	 * @param level
	 *          the level of the children, must >= 0
	 * @param status
	 *          children's status criteria
	 * @param session
	 *          hibernate session
	 * @return
	 * @throws Exception
	 */
	public static List getChildrenOfLevel(AGMember member, long level, String status) {
		return getMemberTreeDao().getChildrenOfLevel(member, level, status);
	}
	
	public static MemberTreeDao getMemberTreeDao(){
		ApplicationContext ctx = SpringUtil.getContext();
		MemberTreeDaoUtil util = (MemberTreeDaoUtil) ctx.getBean(MemberTreeDaoUtil.class.getName());
		return util.memberTreeDao;
	}
	
	public void setMemberTreeDao(MemberTreeDao memberTreeDao) {
		this.memberTreeDao = memberTreeDao;
	}

}
