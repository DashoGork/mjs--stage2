package com.epam.esm.service.entity;

import java.util.HashMap;
import java.util.Map;

public interface PaginationCalcService {
    default Map<String, Integer> paginate(int sizeOfList,
                                          int size, int page) {
        Map<String, Integer> indexes = new HashMap<>();
        int indexOfSeenObjects = (page - 1) * size;
        int indexOfObjectsToShow = indexOfSeenObjects + size;
        if (indexOfObjectsToShow <= sizeOfList) {
            indexes.put("offset", indexOfSeenObjects);
            indexes.put("limit", size);
        } else if (indexOfSeenObjects < sizeOfList) {
            indexes.put("offset", indexOfSeenObjects);
            indexes.put("limit", sizeOfList - indexOfSeenObjects);
        }
        return indexes;
    }
}