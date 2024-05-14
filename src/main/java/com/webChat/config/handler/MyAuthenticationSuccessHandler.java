package com.webChat.config.handler;

import cn.hutool.core.util.IdUtil;
import com.webChat.constant.Constants;
import com.webChat.model.TLogininfor;
import com.webChat.model.TUser;
import com.webChat.result.R;
import com.webChat.service.RedisService;
import com.webChat.service.TLogininforService;
import com.webChat.service.UserService;
import com.webChat.util.IPUtil;
import com.webChat.util.JSONUtils;
import com.webChat.util.JWTUtils;
import com.webChat.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private RedisService redisService;

    @Resource
    private UserService userService;

    @Resource
    private TLogininforService tLogininforService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //登录成功，执行该方法，在该方法中返回json给前端，就行了
        TUser tUser = (TUser) authentication.getPrincipal();
        //1、生成一个jwt字符串
        String userJSON = JSONUtils.toJSON(tUser);
        String jwt = JWTUtils.createJWT(userJSON);

        //2、jwt字符串写入redis
        Long userId = tUser.getId();
        redisService.setValue(Constants.REDIS_JWT_KEY + userId, jwt); //要设置jwt不同的过期时间，选择记住我是7天过期，否则是30分钟过期

        String rememberMe = request.getParameter("rememberMe"); //true，false，undefined
        if (Boolean.parseBoolean(rememberMe)) {
            redisService.expire(Constants.REDIS_JWT_KEY + userId, Constants.EXPIRE_TIME, TimeUnit.SECONDS);
        } else {
            redisService.expire(Constants.REDIS_JWT_KEY + userId, Constants.DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        //更新最近登录时间 (同步操作、异步操作)，这里使用异步操作，异步操作可以提高接口返回数据的速度
        threadPoolTaskExecutor.execute(() -> {
            TUser u = new TUser();
            u.setId(userId);
            u.setLastLoginTime(new Date());
            userService.updateUserLoginTime(u);
        });


        //3、把jwt字符串返回给前端， 向前端返回json数据
        R result = R.OK("登录成功", jwt);
        //记录登录信息
        String tokenId = tUser.getId().toString();
        String userName = tUser.getName();
        //获取用户ip
        String ipAddr = IPUtil.getIpAddr(request);
        //根据ip2region解析ip地址
        String ip2region = IPUtil.getIp2region(ipAddr);
        //城市信息
        String cityInfo = IPUtil.getCityInfo(ipAddr);
        // 获取登录地址城市信息
        String loginLocation = IPUtil.getLoginLocation(ipAddr, ipAddr);
        //获取浏览器信息
        String browser = IPUtil.getUserAgentBrowser(request);
        //获取访问设备os
        String os = IPUtil.getUserAgentOs(request);
        Date date = new Date();
        threadPoolTaskExecutor.execute(() -> {
            TLogininfor tLogininfor = new TLogininfor(null, tokenId, userName, ipAddr, loginLocation, browser, os, "1", null, date);
            tLogininforService.saveUserLogininfor(tLogininfor);
        });

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把json写出去，写到浏览器
        ResponseUtils.write(response, resultJSON);
    }
}
