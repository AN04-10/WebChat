package com.webChat.service;

import com.github.pagehelper.PageInfo;
import com.webChat.model.TUser;
import com.webChat.query.UserQuery;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    PageInfo<TUser> getUsersByPage(UserQuery userQuery,Integer page);

    TUser getUserDetailById(Long id);

    Integer saveUser(UserQuery userQuery);

    Integer updateUser(UserQuery userQuery);

    Integer deleteUser(Long id);

    Integer batchDeleteUser(List<String> idList);

    void updateUserLoginTime(TUser tUser);
}
