package com.epam.esm.model.entity;

import com.epam.esm.model.audit.AuditListener;
import lombok.*;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import java.util.Objects;

@Setter
@Getter
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}