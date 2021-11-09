package com.epam.esm.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import java.util.Set;

@Setter
@Getter
@Component
public class CertificateDto extends BaseEntityDto {
    private String description;
    private String name;
    @Min(0)
    private int price;
    @Min(0)
    private int duration;
    private Set<TagDto> tags;
}