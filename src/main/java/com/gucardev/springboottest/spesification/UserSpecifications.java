package com.gucardev.springboottest.spesification;

import com.gucardev.springboottest.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

  public static Specification<User> searchByKeyword(String searchTerm, Long userId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (StringUtils.isNotBlank(searchTerm)) {
        String likeTerm = "%" + searchTerm.toLowerCase() + "%";
        predicates.add(
            criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), likeTerm),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeTerm),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("mail")), likeTerm)));
      }

      if (userId != null) {
        predicates.add(criteriaBuilder.notEqual(root.get("id"), userId));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public static Specification<User> sortByField(String sortField, Sort.Direction sortDirection) {
    return (root, query, criteriaBuilder) -> {
      if (sortField.equals("name")) {
        if (sortDirection.isAscending()) {
          query.orderBy(criteriaBuilder.asc(root.get("name")));
        } else {
          query.orderBy(criteriaBuilder.desc(root.get("name")));
        }
      } else if (sortField.equals("username")) {
        if (sortDirection.isAscending()) {
          query.orderBy(criteriaBuilder.asc(root.get("username")));
        } else {
          query.orderBy(criteriaBuilder.desc(root.get("username")));
        }
      } else if (sortField.equals("mail")) {
        if (sortDirection.isAscending()) {
          query.orderBy(criteriaBuilder.asc(root.get("mail")));
        } else {
          query.orderBy(criteriaBuilder.desc(root.get("mail")));
        }
      }

      return query.getRestriction();
    };
  }
}
