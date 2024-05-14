package com.webChat.web;

import com.github.pagehelper.PageInfo;
import com.webChat.model.TLogininfor;
import com.webChat.result.R;
import com.webChat.service.TLogininforService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author liuhua
 */
@RestController
@RequestMapping("/api")
public class OnlineController {

    @Resource
    private TLogininforService tLogininforService;


    @GetMapping("/online")
    public R getOnline(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page){
        PageInfo<TLogininfor> tLogininforPageInfo = tLogininforService.selectOnlineByPage(page);
        return R.OK(tLogininforPageInfo);
    }
}
