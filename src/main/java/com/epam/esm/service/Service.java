package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

public interface Service<BaseEntity> {
    void create(BaseEntity entity);

}
