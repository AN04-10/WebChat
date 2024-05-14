package com.webChat.mapper;

import com.webChat.model.TDicType;
import com.webChat.query.DicQuery;

import java.util.List;

public interface TDicTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TDicType record);

    int insertSelective(TDicType record);

    TDicType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TDicType record);

    int updateByPrimaryKey(TDicType record);

    List<TDicType> selectDicTypeByPage(DicQuery dicQuery);
}