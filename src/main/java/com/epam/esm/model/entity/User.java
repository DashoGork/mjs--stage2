package com.epam.esm.model.entity;

import com.epam.esm.model.audit.AuditListener;
import com.epam.esm.model.audit.AuditObject;
import com.epam.esm.model.audit.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "mjs2")
@EntityListeners(AuditListener.class)
public class User extends BaseEntity implements Auditable {
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
    @Transient
    private AuditObject auditObject;

    @Override
    public AuditObject getAudit() {
        return auditObject;
    }

    @Override
    public void setAudit(AuditObject audit) {
        audit.setTimestamp(new Date());
        audit.setEntityId(getId());
        audit.setTypeOfEntity("User");
    }
}
