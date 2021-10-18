package com.epam.esm.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.DecimalMin;
import java.util.List;

@Setter
@Getter
@Component
public class CertificateDto extends BaseEntityDto {
    private String description;
    @DecimalMin(value = "1")
    private int price;
    @DecimalMin(value = "1")
    private int duration;
    private List<TagDto> tags;
}
