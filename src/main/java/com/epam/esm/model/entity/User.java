package com.epam.esm.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "mjs2")
public class User extends BaseEntity {
    @NotEmpty
    private String name;
    private long purse;

    @ManyToMany(cascade = {CascadeType.REMOVE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(
            name = "order", schema = "mjs2",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "id")}
    )
    private List<Order> orders;
}
