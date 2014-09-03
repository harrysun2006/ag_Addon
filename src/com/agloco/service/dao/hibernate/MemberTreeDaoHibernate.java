package com.agloco.service.dao.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.agloco.Constants;
import com.agloco.model.AGMember;
import com.agloco.model.AGMemberTree;
import com.agloco.model.AGMemberTreePK;
import com.agloco.service.dao.MemberTreeDao;

public class MemberTreeDaoHibernate extends HibernateDaoSupport implements
		MemberTreeDao {

	public MemberTreeDaoHibernate() {

	}

	public void addAGMemberTree(AGMemberTree mTree) {
		getHibernateTemplate().save(mTree);
	}

	public void updateAGMemberTree(AGMemberTree mTree) {
		getHibernateTemplate().update(mTree);
	}

	public void deleteAGMemberTree(AGMemberTree mTree) {
		getHibernateTemplate().delete(mTree);
	}

	public AGMemberTree getAGMemberTree(Long memberId) {
		return (AGMemberTree) getHibernateTemplate().get(AGMemberTree.class, memberId);
	}

	/**
	 * add a descendant member under memberId
	 * 
	 * @param member
	 *          the parent member(the referral)
	 * @param descendant
	 *          the sub member
	 */
	public void addAGMember(AGMember member, AGMember descendant) {
		List ancestors = getAncestors(member, AGMemberTree.STRING_AGMEMBER_STATUS_N);
		AGMember ancestor;
		AGMemberTree mTree;
		AGMemberTreePK mTreeKey;
		Long l;
		long level;
		for(Iterator it = ancestors.iterator(); it.hasNext(); ) {
			ancestor = (AGMember) it.next();
			mTree = new AGMemberTree();
			mTreeKey = new AGMemberTreePK();
			l = (ancestor.getMemberTree() == null) ? null : ancestor.getMemberTree().getLevel();
			level = (l == null) ? 0 : l.longValue();
			if(level >= Constants.DEF_REFERRAL_TREE_MAXLEVEL) continue;
			mTreeKey.setMemberId(ancestor.getMemberId());
			mTreeKey.setDescendantId(descendant.getMemberId());
			mTree.setLevel(new Long(++level));
			mTree.setStatus(AGMemberTree.STRING_AGMEMBER_STATUS_N);
			mTree.setPrimaryKey(mTreeKey);
			addAGMemberTree(mTree);
		}
		mTree = new AGMemberTree();
		mTreeKey = new AGMemberTreePK();
		mTreeKey.setMemberId(member.getMemberId());
		mTreeKey.setDescendantId(descendant.getMemberId());
		mTree.setLevel(new Long(1));
		mTree.setStatus(AGMemberTree.STRING_AGMEMBER_STATUS_N);
		mTree.setPrimaryKey(mTreeKey);
		addAGMemberTree(mTree);
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
	public List getAncestors(AGMember member, String status) {
		// select M.* from AG_Member M, AG_M_Tree MT where M.memberId = MT.memberId
		// and MT.level <= :l and MT.descendantId = :n
		List ancestors = new ArrayList();
		Session session = getSession();
		StringBuffer hql = new StringBuffer();
		Query query = null;
		hql
			.append("select M, MT from AGMember M, AGMemberTree MT")
			.append(" where M.memberId = MT.primaryKey.memberId")
			.append(" and MT.primaryKey.descendantId = :did");
		if(status != null && status.trim().length() > 0) hql.append(" and MT.status = :status");
		hql.append(" order by MT.level desc");
		query = session.createQuery(hql.toString());
		query.setParameter("did", member.getMemberId());
		if(status != null && status.trim().length() > 0) query.setParameter("status", status);
		List temp = query.list();
		Object[] values = null;
		AGMember ancestor;
		AGMemberTree mTree;
		for(Iterator it = temp.iterator(); it.hasNext(); ) {
			values = (Object[]) it.next();
			ancestor = (AGMember) values[0];
			mTree = (AGMemberTree) values[1];
			ancestor.setMemberTree(mTree);
			ancestors.add(ancestor);
		}
		return ancestors;
	}

	/**
	 * get the count of given member's children
	 * 
	 * @param member
	 *          the given member, use the follow componding criterions:
	 *          <li>memberId: parent's id, mandatory</li>
	 *          <li>memberCode: children's code, used while non-null</li>
	 *          <li>emailAddress: children's emailAddress, used while non-null</li>
	 *          <li>createDate: children's createDate begin, used while non-null</li>
	 * @param level
	 *          the max level
	 * @param status
	 *          children's status
	 * @return
	 * @throws Exception
	 */
	public long getChildrenCount(AGMember member, long level, String status) {
		// select count(*) from AG_Member M, AG_M_Tree MT where M.memberId =
		// MT.descendantId and MT.level <= :l and MT.memberId = :n
		Session session = getSession();
		StringBuffer hql = new StringBuffer();
		Query query = null;
		hql
			.append("select count(M) from AGMember M, AGMemberTree MT")
			.append(" where M.memberId = MT.primaryKey.descendantId")
			.append(" and MT.primaryKey.memberId = :mid")
			.append(" and MT.level <= :level");
		if(level < 0 || level > Constants.DEF_REFERRAL_TREE_CALCLEVEL) level = Constants.DEF_REFERRAL_TREE_CALCLEVEL;
		if(status != null && status.trim().length() > 0) hql.append(" and MT.status = :status");
		if(member.getMemberCode() != null && member.getMemberCode().trim().length() > 0) hql.append(" and M.memberCode like :mcode");
		if(member.getEmailAddress() != null && member.getEmailAddress().trim().length() > 0) hql.append(" and M.emailAddress like :email");
		if(member.getCreateDate() != null) hql.append(" and M.createDate >= :cdate");
		hql.append(" order by MT.level");
		query = session.createQuery(hql.toString());
		query.setParameter("mid", member.getMemberId());
		query.setParameter("level", new Long(level));
		if(status != null && status.trim().length() > 0) query.setParameter("status", status);
		if(member.getMemberCode() != null && member.getMemberCode().trim().length() > 0) query.setParameter("mcode", "%" + member.getMemberCode() + "%");
		if(member.getEmailAddress() != null && member.getEmailAddress().trim().length() > 0) query.setParameter("email", "%" + member.getEmailAddress() + "%");
		if(member.getCreateDate() != null) query.setParameter("cdate", member.getCreateDate());
		List list = query.list();
		Long count = (list.size() > 0) ? (Long) list.get(0) : null;
		return (count == null) ? 0 : count.longValue();
	}

	/**
	 * get children count group by level, return a list like: {[1, 5], [2, 14],
	 * ... ... [5, 69]}
	 * 
	 * @param member
	 *          the given member, use the follow componding criterions:
	 *          <li>memberId: parent's id, mandatory</li>
	 *          <li>memberCode: children's code, used while non-null</li>
	 *          <li>emailAddress: children's emailAddress, used while non-null</li>
	 *          <li>createDate: children's createDate begin, used while non-null</li>
	 * @param children's
	 *          status
	 * @return
	 */
	public List getChildrenCountByLevel(AGMember member, long level, String status) {
		// select count(*) from AG_M_Tree where MT.memberId = :n group by level
		Session session = getSession();
		StringBuffer hql = new StringBuffer();
		Query query = null;
		hql
			.append("select MT.level, count(MT.level) from AGMember M, AGMemberTree MT")
			.append(" where M.memberId = MT.primaryKey.descendantId")
			.append(" and MT.primaryKey.memberId = :mid");
		if(status != null && status.trim().length() > 0) hql.append(" and MT.status = :status");
		if(member.getMemberCode() != null && member.getMemberCode().trim().length() > 0) hql.append(" and M.memberCode like :mcode");
		if(member.getEmailAddress() != null && member.getEmailAddress().trim().length() > 0) hql.append(" and M.emailAddress like :email");
		if(member.getCreateDate() != null) hql.append(" and M.createDate >= :cdate");
		if(level <= 0 || level > Constants.DEF_REFERRAL_TREE_MAXLEVEL) level = Constants.DEF_REFERRAL_TREE_MAXLEVEL;
		hql.append(" and MT.level <= :level");
		hql
			.append(" group by MT.level")
			.append(" order by MT.level");
		query = session.createQuery(hql.toString());
		query.setParameter("mid", member.getMemberId());
		if(status != null && status.trim().length() > 0) query.setParameter("status", status);
		if(member.getMemberCode() != null && member.getMemberCode().trim().length() > 0) query.setParameter("mcode", "%" + member.getMemberCode() + "%");
		if(member.getEmailAddress() != null && member.getEmailAddress().trim().length() > 0) query.setParameter("email", "%" + member.getEmailAddress() + "%");
		if(member.getCreateDate() != null) query.setParameter("cdate", member.getCreateDate());
		query.setParameter("level", new Long(level));
		return query.list();
	}

	/**
	 * get a partial subtree of the given member
	 * 
	 * @param member
	 *          the given member, use the follow componding criterions:
	 *          <li>memberId: parent's id, mandatory</li>
	 *          <li>memberCode: children's code, used while non-null</li>
	 *          <li>emailAddress: children's emailAddress, used while non-null</li>
	 *          <li>createDate: children's createDate begin, used while non-null</li>
	 * @param level
	 *          the scope of the subtree, if <= 0, return max-scope subtree else
	 *          return 1~level subtree
	 * @param status
	 *          children's status criteria
	 * @return
	 * @throws Exception
	 */
	public List getChildren(AGMember member, long level, String status) {
		// select M.* from AG_Member M, AG_M_Tree MT where M.memberId =
		// MT.descendantId and MT.level <= :l and MT.memberId = :n
		Session session = getSession();
		StringBuffer hql = new StringBuffer();
		Query query = null;
		hql
			.append("select M from AGMember M, AGMemberTree MT")
			.append(" where M.memberId = MT.primaryKey.descendantId")
			.append(" and MT.primaryKey.memberId = :mid")
			.append(" and MT.level <= :level");
		if(level < 0 || level > Constants.DEF_REFERRAL_TREE_CALCLEVEL) level = Constants.DEF_REFERRAL_TREE_CALCLEVEL;
		if(status != null && status.trim().length() > 0) hql.append(" and MT.status = :status");
		if(member.getMemberCode() != null && member.getMemberCode().trim().length() > 0) hql.append(" and M.memberCode like :mcode");
		if(member.getEmailAddress() != null && member.getEmailAddress().trim().length() > 0) hql.append(" and M.emailAddress like :email");
		if(member.getCreateDate() != null) hql.append(" and M.createDate >= :cdate");
		hql.append(" order by MT.level");
		query = session.createQuery(hql.toString());
		query.setParameter("mid", member.getMemberId());
		query.setParameter("level", new Long(level));
		if(status != null && status.trim().length() > 0) query.setParameter("status", status);
		if(member.getMemberCode() != null && member.getMemberCode().trim().length() > 0) query.setParameter("mcode", "%" + member.getMemberCode() + "%");
		if(member.getEmailAddress() != null && member.getEmailAddress().trim().length() > 0) query.setParameter("email", "%" + member.getEmailAddress() + "%");
		if(member.getCreateDate() != null) query.setParameter("cdate", member.getCreateDate());
		return query.list();
	}

	/**
	 * get level-subtree of the given member
	 * 
	 * @param member
	 *          the given member, use the follow componding criterions:
	 *          <li>memberId: parent's id, mandatory</li>
	 *          <li>memberCode: children's code, used while non-null</li>
	 *          <li>emailAddress: children's emailAddress, used while non-null</li>
	 *          <li>createDate: children's createDate begin, used while non-null</li>
	 * @param level
	 *          the level of the children, must >= 0
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List getChildrenOfLevel(AGMember member, long level, String status) {
		// select M.* from AG_Member M, AG_M_Tree MT where M.memberId =
		// MT.descendantId and MT.level = :l and MT.memberId = :n
		Session session = getSession();
		StringBuffer hql = new StringBuffer();
		Query query = null;
		hql
			.append("select M from AGMember M, AGMemberTree MT")
			.append(" where M.memberId = MT.primaryKey.descendantId")
			.append(" and MT.primaryKey.memberId = :mid")
			.append(" and MT.level = :level");
		if(level < 0 || level > Constants.DEF_REFERRAL_TREE_CALCLEVEL) level = Constants.DEF_REFERRAL_TREE_CALCLEVEL;
		if(status != null && status.trim().length() > 0) hql.append(" and MT.status = :status");
		if(member.getMemberCode() != null && member.getMemberCode().trim().length() > 0) hql.append(" and M.memberCode like :mcode");
		if(member.getEmailAddress() != null && member.getEmailAddress().trim().length() > 0) hql.append(" and M.emailAddress like :email");
		if(member.getCreateDate() != null) hql.append(" and M.createDate >= :cdate");
		hql.append(" order by M.memberId");
		query = session.createQuery(hql.toString());
		query.setParameter("mid", member.getMemberId());
		query.setParameter("level", new Long(level));
		if(status != null && status.trim().length() > 0) query.setParameter("status", status);
		if(member.getMemberCode() != null && member.getMemberCode().trim().length() > 0) query.setParameter("mcode", "%" + member.getMemberCode() + "%");
		if(member.getEmailAddress() != null && member.getEmailAddress().trim().length() > 0) query.setParameter("email", "%" + member.getEmailAddress() + "%");
		if(member.getCreateDate() != null) query.setParameter("cdate", member.getCreateDate());
		return query.list();
	}

}
