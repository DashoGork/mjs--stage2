package com.epam.esm.model.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Getter
@Entity
@Table(name = "order", schema = "mjs2")
public class Order extends BaseEntity {

    @Column(name = "user_id")
    private long userId;

    @Column(name = "time_of_purchase")
    private Date timeOfPurchase;
    @ManyToMany(cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "orders", schema = "mjs2",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "certificate_id")}
    )
    private Set<Certificate> certificates = new HashSet<>();
    @Column(name = "price")
    private long price;

    public void setPrice(long price) {
        if (price > 0) {
            this.price = price;
        } else {
            this.price = certificates.stream().map((Certificate::getPrice))
                    .reduce(0, (acc, cur) -> acc + cur, Integer::sum);
        }
    }

    public void setPrice() {
        this.price = certificates.stream().map((Certificate::getPrice))
                .reduce(0, (acc, cur) -> acc + cur, Integer::sum);
    }

    public void setTimeOfPurchase() {
        this.timeOfPurchase = new Date();
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setCertificates(Set<Certificate> certificates) {
        this.certificates = certificates;
    }
}
