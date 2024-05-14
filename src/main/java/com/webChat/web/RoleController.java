package com.webChat.web;

import com.github.pagehelper.PageInfo;
import com.webChat.model.TRole;
import com.webChat.query.RoleQuery;
import com.webChat.result.R;
import com.webChat.service.RoleService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author liuhua
 */
@RequestMapping("/api")
@RestController
public class RoleController {

    @Resource
    private RoleService roleService;


    /**
     * 角色分页
     * @param roleQuery
     * @param page
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role:list')")
    @GetMapping("/roles")
    public R rolePage(RoleQuery roleQuery, @RequestParam(value = "page", required = false, defaultValue = "1")Integer page){
        PageInfo<TRole> rolePageInfo = roleService.getRoleByPage(roleQuery,page);
        return R.OK(rolePageInfo);
    }

    /**
     * 编辑回显
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role:edit')")
    @GetMapping("/role/{id}")
    public R roleInfo(@PathVariable("id") Integer id){
        TRole tRole =roleService.queryRole(id);
        return R.OK(tRole);
    }

    /**
     * 新增角色
     * @param roleQuery
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role:add')")
    @PostMapping("/role")
    public R addRole(RoleQuery roleQuery){
        Integer save =roleService.saveRole(roleQuery);
        return  save >= 1 ? R.OK() : R.FAIL();
    }

    /**
     * 编辑角色
     * @param roleQuery
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role:edit')")
    @PutMapping("/role")
    public R editUser(RoleQuery roleQuery){
        Integer update = roleService.updateRole(roleQuery);
        return  update >= 1 ? R.OK() : R.FAIL();
    }

    /**
     * 删除角色
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role:delete')")
    @DeleteMapping("/role/{id}")
    public R deleteUser(@PathVariable("id") Integer id){
        Integer delete = roleService.deleteUser(id);
        return  delete >= 1 ? R.OK() : R.FAIL();
    }

    /**
     * 批量删除角色
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role:delete')")
    @DeleteMapping("/role")
    public R batchDeleteUser(@RequestParam("ids") String ids){
        List<String> idList = Arrays.asList(ids.split(","));
        Integer batchDelete = roleService.batchDeleteUser(idList);
        return  batchDelete >= idList.size() ? R.OK() : R.FAIL();
    }

}
