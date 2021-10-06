CREATE SCHEMA mjs2;
CREATE TABLE mjs2.tag
(
    ID long PRIMARY KEY AUTO_INCREMENT,
    name varchar(255),
);
CREATE TABLE mjs2.gift_certificate
(
    ID long PRIMARY KEY AUTO_INCREMENT,
    name varchar(255),
    description varchar(255),
    price integer,
    duration integer,
    create_date timestamp,
    last_update_date timestamp,
);
CREATE TABLE mjs2.tag_gift_certificate
(
    ID long PRIMARY KEY AUTO_INCREMENT,
    tag_id long not null,
    certificate_id long not null,
    FOREIGN KEY (tag_id) REFERENCES tag(id),
    FOREIGN KEY (certificate_id) REFERENCES gift_certificate(id),

);