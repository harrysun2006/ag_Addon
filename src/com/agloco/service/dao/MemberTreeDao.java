package com.agloco.service.dao;

import java.util.List;

import com.agloco.model.AGMember;
import com.agloco.model.AGMemberTree;

public interface MemberTreeDao {

	public void addAGMemberTree(AGMemberTree mTree);

	public void updateAGMemberTree(AGMemberTree mTree);

	public void deleteAGMemberTree(AGMemberTree mTree);

	public AGMemberTree getAGMemberTree(Long memberId);

	/**
	 * add a descendant member under memberId
	 * 
	 * @param member
	 *          the parent member(the referral)
	 * @param descendant
	 *          the sub member
	 */
	public void addAGMember(AGMember member, AGMember descendant);

	/**
	 * get all ancestors of the given member
	 * 
	 * @param member
	 *          the given member
	 * @param status
	 *          criterion: ancestors' status
	 * @return
	 */
	public List getAncestors(AGMember member, String status);

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
	public long getChildrenCount(AGMember member, long level, String status);

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
	public List getChildrenCountByLevel(AGMember member, long level, String status);

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
	public List getChildren(AGMember member, long level, String status);

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
	public List getChildrenOfLevel(AGMember member, long level, String status);

}
