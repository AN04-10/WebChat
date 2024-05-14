package com.webChat.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.webChat.constant.Constants;
import com.webChat.model.TLogininfor;
import com.webChat.service.TLogininforService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import com.webChat.mapper.TLogininforMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liuhua
 */
@Service
@Transactional
public class TLogininforServiceImpl implements TLogininforService {

    @Resource
    private TLogininforMapper tLogininforMapper;

    @Override
    public Integer saveUserLogininfor(TLogininfor tLogininfor) {

        return  tLogininforMapper.insertSelective(tLogininfor);
    }

    @Override
    public PageInfo<TLogininfor> selectOnlineByPage(Integer page) {
        PageHelper.startPage(page, Constants.PAGE_SIZE);
        List<TLogininfor> tLogininfors = tLogininforMapper.selectOnlineByPage();
        PageInfo<TLogininfor> info = new PageInfo<>(tLogininfors);
        return info;
    }

    @Override
    public Integer deleteByTokenIdAndUserName(String tokenId, String userName) {
        return tLogininforMapper.deleteByTokenIdAndUserName(tokenId,userName);
    }
}
