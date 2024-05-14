package com.webChat.service;

import com.github.pagehelper.PageInfo;
import com.webChat.model.TLogininfor;

/**
 * @author liuhua
 */
public interface TLogininforService {

    Integer saveUserLogininfor(TLogininfor tLogininfor);

    PageInfo<TLogininfor> selectOnlineByPage(Integer page);

    Integer deleteByTokenIdAndUserName(String tokenId, String userName);
}
