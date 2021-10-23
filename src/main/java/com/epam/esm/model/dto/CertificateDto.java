package com.epam.esm.model.dto;

import com.epam.esm.exceptions.ParameterValidationException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
public class CertificateDto extends BaseEntityDto {
    private String description;
    private String name;
    private int price;
    private int duration;
    private List<TagDto> tags;

    public void setPrice(int price) {
        if (price > -1) {
            this.price = price;
        } else {
            throw new ParameterValidationException("invalid price. should be " +
                    "more than -1. " + price);
        }
    }

    public void setDuration(int duration) {
        if (duration > -1) {
            this.duration = duration;
        } else {
            throw new ParameterValidationException("invalid duration. should be " +
                    "more than -1. " + duration);
        }
    }
}