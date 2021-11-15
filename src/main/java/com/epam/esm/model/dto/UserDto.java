package com.epam.esm.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class UserDto extends BaseEntityDto {
    @NotEmpty
    private String name;
    private long purse;
    private List<OrderDto> orders;
}
