package com.webChat.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * t_permission
 * @author 
 */
@Data
public class TPermission implements Serializable {
    private Integer id;

    private String name;

    private String code;

    private String url;

    private String type;

    private Integer parentId;

    private Integer orderNo;

    private String icon;

    /**
     * 一对多关联
     */
    private List<TPermission> permissionPOList;

    private static final long serialVersionUID = 1L;
}