package com.epam.esm.dao.giftCertificate;

import com.epam.esm.enums.SortOptions;
import com.epam.esm.enums.SortOrder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CertificateQueryBuilder {
    public static String certificateQueryBuilder(String name,
                                                 String description,
                                                 String sortField,
                                                 String sortOrder,
                                                 String tagName) {
        Map<String, String> settings = new HashMap<>();
        settings.put("c.name", name);
        settings.put("c.description", description);
        String startOfQuery = "from Certificate c";
        StringBuilder query = new StringBuilder();
        query.append(startOfQuery);
        int countOfNotNullSettings = 0;
        if (tagName != null) {
            countOfNotNullSettings++;
            query.append(" join fetch c.tags t where t.name like " +
                    "'%" + tagName + "%'");
        }
        for (Map.Entry<String, String> entry : settings.entrySet()
        ) {
            if (entry.getValue() != null & !entry.getValue().isEmpty()) {
                if (countOfNotNullSettings == 0) {
                    query.append(" where ");
                }
                if (countOfNotNullSettings > 0) {
                    query.append(" and ");
                }
                query.append(entry.getKey() + " like '%" + entry.getValue() +
                        "%' ");
                countOfNotNullSettings++;
            }
        }
        query.append(addSortOptions(sortField, sortOrder));
        return query.toString();
    }

    private static String addSortOptions(String sortField, String sortOrder) {
        StringBuilder partOfQuery = new StringBuilder();
        if (!sortField.isEmpty() & !sortOrder.isEmpty()) {
            String valueOfSortField;
            String valueOfSortOrder;
            if (SortOptions.DATE.name().equals(sortField.toUpperCase(Locale.ROOT))) {
                valueOfSortField = " order by c.createDate ";
            } else if (SortOptions.NAME.name().equals(sortField.toUpperCase(Locale.ROOT))) {
                valueOfSortField = " order by c.name ";
            } else {
                throw new IllegalArgumentException("Invalid sort field " + sortField);
            }
            if (SortOrder.ASC.name().equals(sortOrder.toUpperCase(Locale.ROOT))) {
                valueOfSortOrder = SortOrder.ASC.name();
            } else if (SortOrder.DESC
                    .name().equals(sortOrder.toUpperCase(Locale.ROOT))) {
                valueOfSortOrder = SortOrder.DESC.name();
            } else {
                throw new IllegalArgumentException("Invalid sort order " + sortOrder);
            }
            partOfQuery.append(valueOfSortField);
            partOfQuery.append(valueOfSortOrder);
        }
        return partOfQuery.toString();
    }
}
