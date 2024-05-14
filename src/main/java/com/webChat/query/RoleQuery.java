package com.webChat.query;

import lombok.Data;

/**
 * @author liuhua
 */
@Data
public class RoleQuery extends BaseQuery {
    private Integer id;

    private String role;

    private String roleName;

    private static final long serialVersionUID = 1L;
}
