package com.epam.esm.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Component
public class Certificate extends BaseEntity {
    private String description;
    private int price;
    private int duration;
    private Date createDate;
    private Date lastUpdateDate;
    private List<Tag> tags;

    public Certificate(String name, String description, int price, int duration, Date createDate, Date lastUpdateDate, List<Tag> tags) {
        super();
        super.setName(name);
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }
}