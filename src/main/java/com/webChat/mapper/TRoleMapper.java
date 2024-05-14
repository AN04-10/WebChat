package com.webChat.mapper;

import com.webChat.model.TRole;
import com.webChat.query.RoleQuery;

import java.util.List;

public interface TRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TRole record);

    int insertSelective(TRole record);

    TRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TRole record);

    int updateByPrimaryKey(TRole record);

    List<TRole> selectByUserId(Long userId);

    List<TRole> selectRoleByPage(RoleQuery roleQuery);

    Integer deleteByIds(List<String> idList);
}