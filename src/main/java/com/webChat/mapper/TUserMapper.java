package com.webChat.mapper;

import com.webChat.commons.DataScope;
import com.webChat.model.TUser;
import com.webChat.query.UserQuery;

import java.util.List;

public interface TUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    TUser selectByLoginAct(String username);

    @DataScope(tableAlias = "tu",tableField = "id")
    List<TUser> selectUsersByPage(UserQuery userQuery);

    TUser selectDetailByPrimaryKey(Long id);

    Integer deleteByIds(List<String> idList);
}