package com.epam.esm.model.audit;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "history_of_changes", schema = "mjs2")
public class AuditObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "entity_id")
    private long entityId;
    @Column(name = "type_of_entity")
    private String typeOfEntity;
    private Date timestamp;
    private String operation;
}