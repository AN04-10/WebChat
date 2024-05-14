package com.webChat.mapper;

import com.webChat.model.TLogininfor;

import java.util.List;

public interface TLogininforMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TLogininfor record);

    int insertSelective(TLogininfor record);

    TLogininfor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TLogininfor record);

    int updateByPrimaryKey(TLogininfor record);

    List<TLogininfor> selectOnlineByPage();

    Integer deleteByTokenIdAndUserName(String tokenId, String userName);

}