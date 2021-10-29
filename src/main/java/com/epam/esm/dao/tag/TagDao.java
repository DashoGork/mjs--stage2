package com.epam.esm.dao.tag;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagDao extends JpaRepository<Tag, Long> {
    @Query(value = "select t from Tag t where t.name=?1")
    Tag findTagByName(String name);

    List<Tag> findTagsByCertificates(Certificate certificate);

}