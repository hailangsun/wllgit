package com.wll.demo.testplus.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户实体对应表 user
 * </p>
 *
 */
@Data
@Accessors(chain = true)
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private String email;
}
