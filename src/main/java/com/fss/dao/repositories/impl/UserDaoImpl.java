package com.fss.dao.repositories.impl;

import com.eshore.fss.enums.Department;
import com.eshore.fss.enums.UserRole;
import com.eshore.fss.sysmgr.dao.UserDao;
import com.eshore.fss.sysmgr.pojo.User;
import com.eshore.fss.vo.UserSearchKeys;
import com.eshore.khala.common.model.PageConfig;
import com.eshore.khala.common.utils.type.StringUtils;
import com.eshore.khala.core.data.jpa.dao.impl.JpaDaoImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository public class UserDaoImpl extends JpaDaoImpl<User> implements UserDao {

    @Override public User findByUsername(String username) {
        String hql = "from User u where u.username = ?";
        return this.getPojo(hql, new Object[] { username });
    }

    @Override public User findOne(String userId) {
        String hql = "from User u where u.id = ?";
        return this.getPojo(hql, new Object[] { userId });
    }

    @Override @Transactional public boolean saveOrUpdate(User user) {
        try {
            if (user.getId() == null) {
                String userId = UUID.randomUUID().toString();
                user.setId(userId);
                this.save(user);
                return true;
            } else {
                this.update(user);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override @Transactional public boolean deleteUser(String userId) {
        try {
            User user = this.get(userId);
            user.setUsable(false);
            this.update(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override public List<User> queryUserPage(UserSearchKeys userSearchKeys, PageConfig pageConfig) {
        StringBuilder hql = new StringBuilder("from User u where 1=1");
        List params = new ArrayList();
        if (StringUtils.isNotEmpty(userSearchKeys.getDepartmentKey())) {
            int index = Integer.valueOf(userSearchKeys.getDepartmentKey());
            Department department = Department.values()[index];
            hql.append(" and u.department=?");
            params.add(department);
        }
        if (StringUtils.isNotEmpty(userSearchKeys.getRoleKey())) {
        	int index = Integer.valueOf(userSearchKeys.getRoleKey());
            UserRole role = UserRole.values()[index];
            hql.append(" and u.role=?");
            params.add(role);
        }
        if (StringUtils.isNotEmpty(userSearchKeys.getNameKey())) {
            hql.append(" and u.name like ?");
            params.add("%" + userSearchKeys.getNameKey() + "%");
        }
        if (StringUtils.isNotEmpty(userSearchKeys.getUsernameKey())) {
            hql.append(" and u.username like ?");
            params.add("%" + userSearchKeys.getUsernameKey() + "%");
        }

        hql.append(" and u.usable = true");
        hql.append(" order by name");
        return this.queryPage(hql.toString(), pageConfig, params.toArray());
    }

    @Override public List<User> getAll() {
        String hql = "from User";
        return this.query(hql, new Object[] {});
    }
}
