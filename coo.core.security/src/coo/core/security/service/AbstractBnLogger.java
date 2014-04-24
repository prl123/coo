package coo.core.security.service;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.lucene.search.SortField;
import org.springframework.transaction.annotation.Transactional;

import coo.base.model.Page;
import coo.core.hibernate.dao.Dao;
import coo.core.hibernate.search.FullTextCriteria;
import coo.core.model.SearchModel;
import coo.core.security.entity.BnLogEntity;
import coo.core.security.entity.UserEntity;

/**
 * 业务日志组件抽象基类。
 * 
 * @param <T>
 *            业务日志类型
 */
public abstract class AbstractBnLogger<T extends BnLogEntity> {
	@Resource
	private AbstractSecurityService<?, ? extends UserEntity<?, ?, ?>, ?, ?, ?> securityService;
	@Resource
	private Dao<T> bnLogDao;

	/**
	 * 创建业务日志对象。
	 * 
	 * @return 返回业务日志对象。
	 */
	public abstract T newBnLog();

	/**
	 * 记录普通日志。
	 * 
	 * @param message
	 *            日志信息
	 */
	@Transactional
	public void log(String message) {
		log(getCurrentUsername(), message);
	}

	/**
	 * 记录普通日志，登录时用。
	 * 
	 * @param username
	 *            操作人的用户名
	 * @param message
	 *            日志信息
	 */
	@Transactional
	public void log(String username, String message) {
		T bnLog = newBnLog();
		bnLog.setMessage(message);
		bnLog.setCreator(username);
		bnLog.setCreateDate(new Date());
		bnLogDao.save(bnLog);
	}

	/**
	 * 记录高级日志。
	 * 
	 * @param bnLog
	 *            日志对象
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void log(BnLogEntity bnLog) {
		bnLog.setCreator(getCurrentUsername());
		bnLog.setCreateDate(new Date());
		bnLogDao.save((T) bnLog);
	}

	/**
	 * 分页全文搜索日志记录。
	 * 
	 * @param searchModel
	 *            搜索条件
	 * @return 返回符合条件的日志分页对象。
	 */
	@Transactional(readOnly = true)
	public Page<T> searchLog(SearchModel searchModel) {
		FullTextCriteria criteria = bnLogDao.createFullTextCriteria();
		criteria.addSortDesc("createDate", SortField.LONG);
		criteria.setKeyword(searchModel.getKeyword());
		return bnLogDao.searchPage(criteria, searchModel.getPageNo(),
				searchModel.getPageSize());
	}

	/**
	 * 获取指定ID的日志记录。
	 * 
	 * @param logId
	 *            日志ID
	 * @return 返回指定ID的日志记录。
	 */
	@Transactional(readOnly = true)
	public T getLog(String logId) {
		return bnLogDao.get(logId);
	}

	/**
	 * 获取当前登录用户的用户名。
	 * 
	 * @return 返回当前登录用户的用户名。
	 */
	private String getCurrentUsername() {
		return securityService.getCurrentUser().getUsername();
	}
}