package com.webChat.config.handler;

import com.webChat.result.CodeEnum;
import com.webChat.result.R;
import com.webChat.util.JSONUtils;
import com.webChat.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 没有权限时的处理器
 *
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //登录失败的统一结果
        R result = R.FAIL(CodeEnum.ACCESS_DENTED);

        //把R对象转成json
        String resultJSON = JSONUtils.toJSON(result);

        //把R以json返回给前端
        ResponseUtils.write(response, resultJSON);
    }
}
