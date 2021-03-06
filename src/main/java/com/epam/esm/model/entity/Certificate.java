package com.epam.esm.model.entity;

import com.epam.esm.model.audit.AuditListener;
import com.epam.esm.model.audit.AuditObject;
import com.epam.esm.model.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "gift_certificate", schema = "mjs2")
@EntityListeners(AuditListener.class)
public class Certificate extends BaseEntity implements Auditable {
    private String name;
    private String description;
    private int price;
    private int duration;
    private Date createDate;
    private Date lastUpdateDate;

    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(
            name = "tag_gift_certificate", schema = "mjs2",
            joinColumns = {@JoinColumn(name = "certificate_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    @JsonIgnoreProperties("certificates")
    private Set<Tag> tags = new HashSet<>();
    @Transient
    private AuditObject auditObject;

    public Certificate(String name, String description, int price,
                       int duration, Date createDate, Date lastUpdateDate,
                       Set<Tag> tags) {
        super();
        this.setName(name);
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getCertificates().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Certificate that = (Certificate) o;
        return price == that.price && duration == that.duration && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, price, duration, createDate, lastUpdateDate);
    }

    @Override
    public AuditObject getAudit() {
        return auditObject;
    }

    @Override
    public void setAudit(AuditObject audit) {
        audit.setTimestamp(new Date());
        audit.setEntityId(getId());
        audit.setTypeOfEntity("Certificate");
    }
}