package com.epam.esm.model.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
public class BaseEntityDto extends RepresentationModel<BaseEntityDto> {
    private long id;
}
