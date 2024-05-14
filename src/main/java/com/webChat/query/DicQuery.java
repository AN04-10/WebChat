package com.webChat.query;

import lombok.Data;

/**
 * @author liuhua
 */
@Data
public class DicQuery {

    /**
     * 主键，自动增长，字典类型ID
     */
    private Integer typeId;

    /**
     * 字典类型代码
     */
    private String typeCode;

    /**
     * 字典类型名称
     */
    private String typeName;

    /**
     * 字典类型备注
     */
    private String typeRemark;

    /**
     * 主键，自动增长，字典值类型ID
     */
    private Integer valueId;

    /**
     * 字典值
     */
    private String typeValue;

    /**
     * 字典值排序
     */
    private Integer valueOrder;

    /**
     * 字典值备注
     */
    private String valueRemark;


    private static final long serialVersionUID = 1L;
}
