package com.epam.esm.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto extends BaseEntityDto {
    private String name;
    private long purse;
    private List<OrderDto> orders;
}
