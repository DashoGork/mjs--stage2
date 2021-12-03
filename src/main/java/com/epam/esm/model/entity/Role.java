package com.epam.esm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Entity
@Table(name = "role", schema = "mjs2")
public class Role extends BaseEntity {
    @NotEmpty
    private String name;
}
