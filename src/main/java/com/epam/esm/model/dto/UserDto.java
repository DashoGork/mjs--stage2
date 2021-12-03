package com.epam.esm.model.dto;

import com.epam.esm.model.entity.Role;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class UserDto extends BaseEntityDto {
    @NotEmpty
    private String name;
    private String password;
    private long purse;
    private List<OrderDto> orders;
    private Role role;
}
