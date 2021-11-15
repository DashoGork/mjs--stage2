package com.epam.esm.dao.giftCertificate.impl;

import com.epam.esm.dao.giftCertificate.CertificateDaoI;
import com.epam.esm.dao.giftCertificate.CertificateQueryBuilder;
import com.epam.esm.enums.Queries;
import com.epam.esm.model.entity.Certificate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class CertificateDao implements CertificateDaoI {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void create(Certificate certificate) {
        entityManager.persist(certificate);
    }

    @Override
    public List<Certificate> read(int offset, int limit) {
        Query query =
                entityManager.createQuery(Queries.SELECT_ALL_CERTIFICATES.getQuery())
                        .setFirstResult(offset).setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public List<Certificate> read() {
        Query query =
                entityManager.createQuery(Queries.SELECT_ALL_CERTIFICATES.getQuery());
        return query.getResultList();
    }

    @Override
    public Optional<Certificate> read(long id) {
        return Optional.ofNullable(entityManager.find(Certificate.class, id));
    }

    @Override
    public void delete(Certificate certificate) {
        Query query =
                entityManager.createQuery(Queries.DELETE_CERTIFICATE_BY_ID.getQuery());
        query.setParameter(1, certificate.getId());
        query.executeUpdate();
    }

    @Override
    public Optional<Certificate> findCertificateByName(String name) {
        Optional<Certificate> certificate;
        try {
            Query query =
                    entityManager.createQuery(Queries.SELECT_CERTIFICATE_BY_NAME.getQuery());
            query.setParameter(1, name);
            certificate = Optional.ofNullable((Certificate) query.getSingleResult());
        } catch (NoResultException ex) {
            certificate = Optional.empty();
        }
        return certificate;
    }

    @Override
    public void patch(Certificate certificate) {
        entityManager.persist(certificate);
    }

    @Override
    public List<Certificate> filterAndSort(String name, String description,
                                           String sortField, String sortOrder,
                                           String tagName, int offset, int limit) {
        Query query =
                entityManager.createQuery(CertificateQueryBuilder.certificateQueryBuilder(name,
                        description, sortField, sortOrder, tagName)).setFirstResult(offset).setMaxResults(limit);
        return (List<Certificate>) query.getResultList();
    }
}
