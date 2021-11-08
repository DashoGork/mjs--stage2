package com.epam.esm.service.entity;

import java.util.ArrayList;
import java.util.List;

public interface PaginationService<BaseEntity> {
    default List<BaseEntity> paginate(List<BaseEntity> objectsToPaginate,
                                      int size, int page) {
        List<BaseEntity> paginated = new ArrayList<>();
        int indexOfSeenObjects = (page - 1) * size;
        int indexOfObjectsToShow = indexOfSeenObjects + size;
        int sizeOfList = objectsToPaginate.size();
        if (indexOfObjectsToShow <= sizeOfList) {
            paginated =
                    objectsToPaginate.subList(indexOfSeenObjects,
                            indexOfSeenObjects + size);
        } else if (indexOfSeenObjects < sizeOfList) {
            paginated =
                    objectsToPaginate.subList(indexOfSeenObjects, sizeOfList);
        }
        return paginated;

    }
}
