package com.gamix.utils;

import org.springframework.data.domain.Sort;

public class SortUtils {
    public static Sort sortByUpdatedAtOrCreatedAt() {
        return Sort.by(
            Sort.Order.desc("updatedAt").nullsLast(),
            Sort.Order.desc("createdAt")
        );
    }
}
