package com.epam.esm.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Component
public class OrderDto extends BaseEntityDto {
    private long price;
    @NotNull
    private long userId;
    private Date timeOfPurchase;
    private Set<CertificateDto> certificates = new HashSet<>();
}
