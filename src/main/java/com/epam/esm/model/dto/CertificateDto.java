package com.epam.esm.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Component
public class CertificateDto extends BaseEntityDto {
    private String description;
    @Size(min = 1)
    private String name;
    @Min(0)
    private int price;
    @Min(0)
    private int duration;
    private Date createDate;
    private Date lastUpdateDate;
    private Set<TagDto> tags;
}