package com.webChat.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.webChat.constant.Constants;
import com.webChat.mapper.TRoleMapper;
import com.webChat.model.TRole;
import com.webChat.query.RoleQuery;
import com.webChat.query.UserQuery;
import com.webChat.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liuhua
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Resource
    private TRoleMapper tRoleMapper;
    @Override
    public PageInfo<TRole> getRoleByPage(RoleQuery roleQuery, Integer page) {
        PageHelper.startPage(page, Constants.PAGE_SIZE);
        List<TRole> tRoleList = tRoleMapper.selectRoleByPage(roleQuery);
        PageInfo<TRole> info = new PageInfo(tRoleList);
        return info;
    }

    @Override
    public Integer saveRole(RoleQuery roleQuery) {
        TRole tRole = new TRole();
        BeanUtils.copyProperties(roleQuery,tRole);
        return tRoleMapper.insertSelective(tRole);
    }

    @Override
    public Integer updateRole(RoleQuery roleQuery) {
        TRole tRole = new TRole();
        BeanUtils.copyProperties(roleQuery,tRole);
        return tRoleMapper.updateByPrimaryKey(tRole);
    }

    @Override
    public Integer deleteUser(Integer id) {
        return tRoleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer batchDeleteUser(List<String> idList) {
        return tRoleMapper.deleteByIds(idList);
    }

    @Override
    public TRole queryRole(Integer id) {
        TRole tRole = tRoleMapper.selectByPrimaryKey(id);
        return tRole;
    }
}
