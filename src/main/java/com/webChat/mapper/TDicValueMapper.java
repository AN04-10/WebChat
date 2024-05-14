package com.webChat.mapper;

import com.webChat.bo.TDicBo;
import com.webChat.model.TDicValue;
import com.webChat.query.DicQuery;

import java.util.List;

public interface TDicValueMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TDicValue record);

    int insertSelective(TDicValue record);

    TDicValue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TDicValue record);

    int updateByPrimaryKey(TDicValue record);

    List<TDicValue> selectDicValueByPage(List<String> typeCodeList);

    List<TDicValue> selectDicValueByTypeCode(String typeCode);
}