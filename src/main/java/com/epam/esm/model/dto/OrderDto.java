package com.epam.esm.model.dto;

import com.epam.esm.model.entity.Certificate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Component
public class OrderDto extends BaseEntityDto {
    private long price;
    private long userId;
    private Date timeOfPurchase;
    private Set<Certificate> certificates = new HashSet<>();
}
