package com.epam.esm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Queries {
    SELECT_ALL_TAGS("from Tag"),
    SELECT_TAG_BY_NAME("from Tag t where t.name=?1"),
    SELECT_TOP_TAG("select * from mjs2.user_tag where name in (select name from  mjs2.user_tag\n" +
            "                                where mjs2.user_tag.count_tag = (select MAX \n" +
            "            (count_tag)  from mjs2.user_tag where id = (select user_id from mjs2.order where user_id is not null group by user_id\n" +
            "            order by sum(price) desc limit 1))) and id =(select user_id from mjs2.order where user_id is not null group by user_id\n" +
            "            order by sum(price) desc limit 1)"),
    DELETE_TAG_BY_ID("delete from Tag t where t.id=?1"),
    SELECT_ALL_TAGS_OF_CERTIFICATE("select c.tags from Certificate c where c" +
            ".id=?1"),
    SELECT_ALL_CERTIFICATES("from Certificate"),
    DELETE_CERTIFICATE_BY_ID("delete from Certificate c where c.id=?1"),
    SELECT_CERTIFICATE_BY_NAME("from Certificate c where c.name=?1"),
    SELECT_ORDER_BY_USER_ID_AND_TIME_OFF_PURCHASE_AND_PRICE("from Order o " +
            "where o.userId=?1 and o.timeOfPurchase=?2 and o.price=?3"),
    SELECT_ALL_USERS("from User"),
    SELECT_USER_BY_NAME("from User u where u.name=?1"),
    ;
    private String query;
}
