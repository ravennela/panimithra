package com.example.fixmate.dtos.custom;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.*;

import org.springframework.data.jpa.domain.Specification;

import com.example.fixmate.entities.User;

public class UserSpecifications {
    public static Specification<User> filter(String name, String status, String role) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (status != null && !status.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("status")), "%" + status.toLowerCase() + "%"));
            }

            if (role != null && !role.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("role")), "%" + role.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));

        };
    }

}
