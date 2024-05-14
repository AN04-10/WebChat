package com.webChat.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.webChat.constant.Constants;
import com.webChat.mapper.TPermissionMapper;
import com.webChat.mapper.TRoleMapper;
import com.webChat.mapper.TUserMapper;
import com.webChat.model.TPermission;
import com.webChat.model.TRole;
import com.webChat.query.BaseQuery;
import com.webChat.query.UserQuery;
import com.webChat.service.UserService;
import com.webChat.util.JWTUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.webChat.model.TUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private TUserMapper tUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private TRoleMapper tRoleMapper;
    @Resource
    private TPermissionMapper tPermissionMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TUser tUser = tUserMapper.selectByLoginAct(username);
        if (tUser == null){
            throw new UsernameNotFoundException("登录账号不存在");
        }
        // 查角色
        List<TRole> tRoleList = tRoleMapper.selectByUserId(tUser.getId());
        List<String> stringRoleList = new ArrayList();
        tRoleList.forEach( tRole -> {
            stringRoleList.add(tRole.getRole());
        });
        tUser.setRoleList(stringRoleList);

        //查用户菜单权限
        List<TPermission> menuPermissionList = tPermissionMapper.selectMenuPermissionByUserId(tUser.getId());
        tUser.setMenuPermissionList(menuPermissionList);

        //查询用户权限
        List<TPermission> buttonPermissionList = tPermissionMapper.selectButtonPermissionByUserId(tUser.getId());
        List<String> StringPermissionList = new ArrayList<>();
        buttonPermissionList.forEach(tPermission -> {
            StringPermissionList.add(tPermission.getCode());
        });
        tUser.setPermissionList(StringPermissionList);



        return tUser;
    }

    @Override
    public PageInfo<TUser> getUsersByPage(UserQuery userQuery,Integer page) {
        PageHelper.startPage(page, Constants.PAGE_SIZE);
        List<TUser> userList = tUserMapper.selectUsersByPage(userQuery);
        PageInfo<TUser> info = new PageInfo<>(userList);
        return info;
    }

    @Override
    public TUser getUserDetailById(Long id) {
        TUser tUser = tUserMapper.selectDetailByPrimaryKey(id);
        return tUser;
    }

    @Override
    public Integer saveUser(UserQuery userQuery) {
        TUser tUser = new TUser();
        //拷贝对象 属性类型 属性名一致
        BeanUtils.copyProperties(userQuery,tUser);
        //密码加密
        tUser.setLoginPwd(passwordEncoder.encode(userQuery.getLoginPwd()));
        tUser.setCreateTime(new Date());
        //登录人id
        Long loginUserId = JWTUtils.parseUserFromJWT(userQuery.getToken()).getId();
        tUser.setCreateBy(loginUserId);
        return tUserMapper.insertSelective(tUser);
    }

    @Override
    public Integer updateUser(UserQuery userQuery) {
        TUser tUser = new TUser();
        //拷贝对象 属性类型 属性名一致
        BeanUtils.copyProperties(userQuery,tUser);
        if (StringUtils.hasText(userQuery.getLoginPwd())){
            tUser.setLoginPwd(passwordEncoder.encode(userQuery.getLoginPwd()));
        }
        tUser.setEditTime(new Date());
        //登录人id
        Long loginUserId = JWTUtils.parseUserFromJWT(userQuery.getToken()).getId();
        tUser.setEditBy(loginUserId);
        return tUserMapper.updateByPrimaryKeySelective(tUser);
    }


    @Override
    public Integer deleteUser(Long id) {
        return tUserMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer batchDeleteUser(List<String> idList) {
        return tUserMapper.deleteByIds(idList);
    }

    @Override
    public void updateUserLoginTime(TUser tUser) {
        tUserMapper.updateByPrimaryKeySelective(tUser);
    }
}
