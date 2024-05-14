package com.webChat.service;

import com.github.pagehelper.PageInfo;
import com.webChat.bo.TDicBo;
import com.webChat.query.DicQuery;

/**
 * @author liuhua
 */
public interface DicService {
    PageInfo<TDicBo> getDicByPage(DicQuery dicQuery, Integer page);

    Integer saveRole(DicQuery dicQuery);

    TDicBo queryDic(Integer typeId, Integer valueId);

    Integer updateDic(DicQuery dicQuery);

    Integer deleteDic(Integer typeId, Integer valueId);
}
