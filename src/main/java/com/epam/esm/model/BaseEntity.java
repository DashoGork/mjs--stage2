package com.epam.esm.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class BaseEntity {
    private long id;
    private String name;
}