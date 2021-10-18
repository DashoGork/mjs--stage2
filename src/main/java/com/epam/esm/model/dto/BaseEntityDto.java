package com.epam.esm.model.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Component
public class BaseEntityDto {
    private long id;
    @NotNull
    private String name;
}
