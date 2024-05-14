package com.webChat.web;

import com.github.pagehelper.PageInfo;
import com.webChat.model.TUser;
import com.webChat.query.UserQuery;
import com.webChat.result.R;
import com.webChat.service.UserService;
import com.webChat.util.IPUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Resource
    private UserService userService;


    /**
     * 获取登录人信息
     *
     * @param authentication
     * @return
     */
    @GetMapping("/login/info")
    public R loginInfo(Authentication authentication) {
        TUser tUser = (TUser) authentication.getPrincipal();
        return R.OK(tUser);
    }

    /**
     * 免登录
     *
     * @return
     */
    @GetMapping("/login/free")
    public R freeLogin() {
        return R.OK();
    }

    /**
     * 分页查询用户列表
     *
     * @param page
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user:list')")
    @GetMapping("/users")
    public R userPage(UserQuery userQuery, @RequestParam(value = "page", required = false, defaultValue = "1") Integer page) {
        PageInfo<TUser> usersByPage = userService.getUsersByPage(userQuery, page);
        return R.OK(usersByPage);
    }

    /**
     * 查询用户详情
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user:view')")
    @GetMapping("/user/{id}")
    public R userDetail(@PathVariable("id") Long id) {
        TUser tUser = userService.getUserDetailById(id);
        return R.OK(tUser);
    }

    /**
     * 新增用户
     *
     * @param userQuery
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user:add')")
    @PostMapping("/user")
    public R addUser(UserQuery userQuery, @RequestHeader("Authorization") String token) {
        userQuery.setToken(token);
        Integer save = userService.saveUser(userQuery);
        return save >= 1 ? R.OK() : R.FAIL();
    }

    /**
     * 编辑用户
     *
     * @param userQuery
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user:edit')")
    @PutMapping("/user")
    public R editUser(UserQuery userQuery, @RequestHeader("Authorization") String token) {
        userQuery.setToken(token);
        Integer update = userService.updateUser(userQuery);
        return update >= 1 ? R.OK() : R.FAIL();
    }

    /**
     * 删除用户
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user:delete')")
    @DeleteMapping("/user/{id}")
    public R deleteUser(@PathVariable("id") Long id) {
        Integer delete = userService.deleteUser(id);
        return delete >= 1 ? R.OK() : R.FAIL();
    }

    /**
     * 批量删除用户
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('user:delete')")
    @DeleteMapping("/user")
    public R batchDeleteUser(@RequestParam("ids") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        Integer batchDelete = userService.batchDeleteUser(idList);
        return batchDelete >= idList.size() ? R.OK() : R.FAIL();
    }

}
