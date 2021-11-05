package com.epam.esm.service.entity;

import com.epam.esm.model.entity.Tag;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public interface PaginationService<BaseEntity> {
    default List<BaseEntity> paginate(List<BaseEntity> objectsToPaginate,
                                      int size, int page) {
        if (size > 0 & page > 0) {
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
        } else {
            throw new InvalidParameterException("size or page is invalid");
        }

    }
}
