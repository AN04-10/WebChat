package com.webChat.service;

import com.github.pagehelper.PageInfo;
import com.webChat.model.TRole;
import com.webChat.query.RoleQuery;
import com.webChat.query.UserQuery;

import java.util.List;

/**
 * @author liuhua
 */
public interface RoleService {
    PageInfo<TRole> getRoleByPage(RoleQuery roleQuery, Integer page);

    Integer saveRole(RoleQuery roleQuery);

    Integer updateRole(RoleQuery roleQuery);

    Integer deleteUser(Integer id);

    Integer batchDeleteUser(List<String> idList);

    TRole queryRole(Integer id);
}
