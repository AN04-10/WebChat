package com.webChat.config.handler;

import com.webChat.constant.Constants;
import com.webChat.manager.RedisManager;
import com.webChat.model.TUser;
import com.webChat.result.CodeEnum;
import com.webChat.result.R;
import com.webChat.service.RedisService;
import com.webChat.service.TLogininforService;
import com.webChat.util.JSONUtils;
import com.webChat.util.ResponseUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 退出成功处理器
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private RedisService redisService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private TLogininforService tLogininforService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        R result = R.OK(CodeEnum.USER_LOGOUT_SUCCESS);
        //退出成功，执行该方法，在该方法中返回json给前端，就行了
        TUser tUser = (TUser) authentication.getPrincipal();

        Boolean delete = redisService.delete(Constants.REDIS_JWT_KEY + tUser.getId());
        if (!delete) {
            result = R.OK(CodeEnum.USER_LOGOUT_FAIL);
        }
        // 清除登录记录信息
        String tokenId = Constants.REDIS_JWT_KEY + tUser.getId();
        String userName = tUser.getName();
        Integer integer = tLogininforService.deleteByTokenIdAndUserName(tUser.getId().toString(), userName);
        if (integer < 1) {
            result = R.OK(CodeEnum.USER_LOGOUT_FAIL);
        }

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把R以json返回给前端
        ResponseUtils.write(response, resultJSON);
    }
}
