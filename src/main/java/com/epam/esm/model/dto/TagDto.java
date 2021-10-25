package com.epam.esm.model.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@Component
@EqualsAndHashCode
public class TagDto extends BaseEntityDto {
    @NotNull(message = "Name cannot be null")
    private String name;
}
