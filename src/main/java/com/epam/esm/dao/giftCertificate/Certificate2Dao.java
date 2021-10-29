package com.epam.esm.dao.giftCertificate;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import java.util.List;

@Repository
public interface Certificate2Dao extends JpaRepository<Certificate, Long> {
    @Transactional
    @Modifying()
    @Query("update " +
            "Certificate c set c.name=COALESCE(?1,?2), " +
            "c.description=COALESCE" +
            "(?1,?2), " +
            "c.price=COALESCE(?1,?2), " +
            "c.duration=COALESCE(?1,?2), c.lastUpdateDate=?1" +
            "WHERE c.id=?2")
    int patch(Certificate certificate, Certificate oldCertificate);

    List<Certificate> getCertificatesByTags(Tag tag);
//    @Query("update Certificate c set c.tags=")
//    void rewriteTags(Tag tags);


}
