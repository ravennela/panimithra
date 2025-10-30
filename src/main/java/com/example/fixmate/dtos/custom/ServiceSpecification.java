package com.example.fixmate.dtos.custom;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import com.example.fixmate.entities.Review;
import com.example.fixmate.entities.ServiceEntity;

public class ServiceSpecification {

    public static Specification<ServiceEntity> filter(String categoryId, String serviceName, Double minPrice,
            Double maxPrice, Double minRating, String categoryName, String subCategoryName) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Object, Object> categoryJoin = root.join("category", JoinType.LEFT);

            if (categoryId != null && !categoryId.isEmpty()) {
                predicates.add(cb.equal(categoryJoin.get("id"), categoryId));
            }

            if (categoryName != null && !categoryName.isEmpty()) {
                predicates.add(
                        cb.like(cb.lower(categoryJoin.get("categoryName")), "%" + categoryName.toLowerCase() + "%"));
            }
            Join<Object, Object> subCategoryJoin = root.join("subCategory", JoinType.LEFT);

            if (subCategoryName != null && !subCategoryName.isEmpty()) {
                predicates
                        .add(cb.like(cb.lower(subCategoryJoin.get("subCategoryName")),
                                "%" + subCategoryName.toLowerCase() + "%"));
            }
            if (serviceName != null && !serviceName.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + serviceName.toLowerCase() + "%"));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));

            }

            if (minRating != null) {
                Subquery<Double> ratingSubquery = query.subquery(Double.class);
                Root<Review> reviewRoot = ratingSubquery.from(Review.class);
                ratingSubquery.select(cb.avg(reviewRoot.get("rating")));
                ratingSubquery.where(cb.equal(reviewRoot.get("service"), root));
                predicates.add(cb.greaterThanOrEqualTo(ratingSubquery, minRating));
            }
            return cb.and(predicates.toArray(new Predicate[0]));

        };

    }
}
