package com.epam.esm.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class BaseEntity {
    private long id;
    private String name;
}