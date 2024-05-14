package com.webChat.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.webChat.bo.TDicBo;
import com.webChat.constant.Constants;
import com.webChat.mapper.TDicTypeMapper;
import com.webChat.mapper.TDicValueMapper;
import com.webChat.model.TDicType;
import com.webChat.model.TDicValue;
import com.webChat.query.DicQuery;
import com.webChat.service.DicService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author liuhua
 */
@Service
@Transactional
public class DicServiceImpl implements DicService {

    @Resource
    private TDicTypeMapper tDicTypeMapper;
    @Resource
    private TDicValueMapper tDicValueMapper;

    @Override
    public PageInfo<TDicBo> getDicByPage(DicQuery dicQuery, Integer page) {

        PageHelper.startPage(page, Constants.PAGE_SIZE);
        List<TDicType> tDicTypeList = tDicTypeMapper.selectDicTypeByPage(dicQuery);
        if (tDicTypeList == null || tDicTypeList.isEmpty()) {
            return new PageInfo<>(new ArrayList<>());
        }
        List<String> typeCodeList = tDicTypeList.stream()
                .map(TDicType::getTypeCode)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<TDicValue> tDicValueList = tDicValueMapper.selectDicValueByPage(typeCodeList);
        Map<String, TDicType> typeMap = tDicTypeList.stream()
                .collect(Collectors.toMap(TDicType::getTypeCode, Function.identity()));
        List<TDicBo> tDicBoList = tDicValueList.stream()
                .map(tDicValue -> {
                    TDicBo tDicBo = new TDicBo();
                    tDicBo.setValueId(tDicValue.getId());
                    tDicBo.setTypeCode(tDicValue.getTypeCode());
                    tDicBo.setTypeValue(tDicValue.getTypeValue());
                    TDicType tDicType = typeMap.get(tDicValue.getTypeCode());
                    if (tDicType != null) {
                        tDicBo.setTypeId(tDicType.getId());
                        tDicBo.setTypeName(tDicType.getTypeName());
                    }
                    return tDicBo;
                })
                .collect(Collectors.toList());
        return new PageInfo<>(tDicBoList);
    }

    @Override
    public Integer saveRole(DicQuery dicQuery) {
        TDicType tDicType = new TDicType();
        TDicValue tDicValue = new TDicValue();

        BeanUtils.copyProperties(dicQuery, tDicType);
        BeanUtils.copyProperties(dicQuery, tDicValue);
        tDicType.setRemark(dicQuery.getTypeRemark());
        tDicValue.setOrder(dicQuery.getValueOrder());
        tDicValue.setRemark(dicQuery.getValueRemark());
        int type = tDicTypeMapper.insertSelective(tDicType);
        if (type >= 1) {
            return tDicValueMapper.insertSelective(tDicValue);
        }

        return -1;
    }

    @Override
    public TDicBo queryDic(Integer typeId, Integer valueId) {
        TDicType tDicType = tDicTypeMapper.selectByPrimaryKey(typeId);
        TDicValue tDicValue = tDicValueMapper.selectByPrimaryKey(valueId);
        TDicBo tDicBo = new TDicBo();
        BeanUtils.copyProperties(tDicType, tDicBo);
        BeanUtils.copyProperties(tDicValue, tDicBo);
        tDicBo.setTypeId(tDicType.getId());
        tDicBo.setValueId(tDicValue.getId());
        tDicBo.setTypeRemark(tDicType.getRemark());
        tDicBo.setValueOrder(tDicValue.getOrder());
        tDicBo.setValueRemark(tDicValue.getRemark());
        return tDicBo;
    }

    @Override
    public Integer updateDic(DicQuery dicQuery) {
        TDicValue tDicValue = new TDicValue();
        BeanUtils.copyProperties(dicQuery, tDicValue);
        tDicValue.setId(dicQuery.getValueId());
        tDicValue.setOrder(dicQuery.getValueOrder());
        tDicValue.setRemark(dicQuery.getValueRemark());
        return tDicValueMapper.updateByPrimaryKeySelective(tDicValue);
    }

    @Override
    public Integer deleteDic(Integer typeId, Integer valueId) {
        int value = tDicValueMapper.deleteByPrimaryKey(valueId);
        TDicType tDicType = tDicTypeMapper.selectByPrimaryKey(typeId);
        List<TDicValue> tDicValueList = tDicValueMapper.selectDicValueByTypeCode(tDicType.getTypeCode());
        if (tDicValueList == null || tDicValueList.isEmpty()){
            return tDicTypeMapper.deleteByPrimaryKey(typeId);
        }
        return value;
    }
}
