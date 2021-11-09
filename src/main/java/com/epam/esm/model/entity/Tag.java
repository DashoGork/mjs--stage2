package com.epam.esm.model.entity;

import com.epam.esm.model.audit.AuditListener;
import com.epam.esm.model.audit.AuditObject;
import com.epam.esm.model.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "tag", schema = "mjs2")
@EntityListeners(AuditListener.class)
public class Tag extends BaseEntity implements Auditable {
    private String name;
    @ManyToMany(mappedBy = "tags",
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                    CascadeType.MERGE}, fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties("tags")
    private Set<Certificate> certificates = new HashSet<>();

    @Transient
    private AuditObject auditObject;

    public Set<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        this.certificates = certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public AuditObject getAudit() {
        return auditObject;
    }

    @Override
    public void setAudit(AuditObject audit) {
        audit.setTimestamp(new Date());
        audit.setEntityId(getId());
        audit.setTypeOfEntity("Tag");
    }
}