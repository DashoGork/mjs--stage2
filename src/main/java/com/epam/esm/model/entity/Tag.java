package com.epam.esm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "tag", schema = "mjs2")
public class Tag extends BaseEntity {
    private String name;
    @ManyToMany(mappedBy = "tags",
            cascade = {CascadeType.PERSIST, CascadeType.REFRESH,
                    CascadeType.MERGE}, fetch = FetchType.LAZY
    )
    @JsonIgnoreProperties("tags")
    private Set<Certificate> certificates = new HashSet<>();

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
        return Objects.equals(certificates, tag.certificates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), certificates);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}