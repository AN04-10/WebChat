package com.webChat.web;

import com.github.pagehelper.PageInfo;
import com.webChat.bo.TDicBo;
import com.webChat.query.DicQuery;
import com.webChat.result.R;
import com.webChat.service.DicService;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * @author liuhua
 */
@RequestMapping("/api")
@RestController
public class DicController {

    @Resource
    private DicService dicService;


    /**
     * 角色分页
     * @param dicQuery
     * @param page
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dic:list')")
    @GetMapping("/dics")
    public R dicPage(DicQuery dicQuery, @RequestParam(value = "page", required = false, defaultValue = "1")Integer page){
        PageInfo<TDicBo> tDicBoPageInfo = dicService.getDicByPage(dicQuery,page);
        return R.OK(tDicBoPageInfo);
    }


    /**
     * 新增dic
     * @param dicQuery
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dic:add')")
    @PostMapping("/dic")
    public R addDic(DicQuery dicQuery){
        Integer save =dicService.saveRole(dicQuery);
        return  save >= 1 ? R.OK() : R.FAIL();
    }

    /**
     * 编辑回显
     * @param typeId
     * @param valueId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dic:edit')")
    @GetMapping("/dic/{typeId}/{valueId}")
    public R editInfo(@PathVariable("typeId") Integer typeId,@PathVariable("valueId") Integer valueId){
        TDicBo tDicBo =dicService.queryDic(typeId,valueId);
        return R.OK(tDicBo);
    }

    /**
     * 编辑
     * @param dicQuery
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dic:edit')")
    @PutMapping("/dic")
    public R editDic(DicQuery dicQuery){
        Integer edit =  dicService.updateDic(dicQuery);
        return  edit >= 1 ? R.OK() : R.FAIL();
    }

    /**
     * 删除
     * @param valueId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('dic:delete')")
    @DeleteMapping("/dic/{typeId}/{valueId}")
    public R deleteDic(@PathVariable("typeId") Integer typeId,@PathVariable("valueId") Integer valueId){
        Integer delete = dicService.deleteDic(typeId,valueId);
        return  delete >= 1 ? R.OK() : R.FAIL();
    }



}
