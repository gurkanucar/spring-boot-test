package com.gucardev.springboottest.repository;

import com.gucardev.springboottest.model.projection.UsernameLengthProjection;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository {

  @PersistenceContext EntityManager entityManager;

  @Override
  public List<UsernameLengthProjection> getUserNamesListWithLengthGreaterThan(int length) {
    String queryString =
        "SELECT u.username AS username, u.id AS id, LENGTH(u.username) AS length, u.email AS email "
            + "FROM user_table u WHERE LENGTH(u.username) > :length";
    Query query = entityManager.createNativeQuery(queryString);
    query.setParameter("length", length);

    List<Object[]> results = query.getResultList();

    return results.stream()
        .map(
            result ->
                new UsernameLengthProjection() {
                  private final String username = (String) result[0];
                  private final Long id = ((BigInteger) result[1]).longValue();
                  private final Integer length = ((BigInteger) result[2]).intValue();
                  private final String email = (String) result[3];

                  @Override
                  public String getUsername() {
                    return username;
                  }

                  @Override
                  public Long getId() {
                    return id;
                  }

                  @Override
                  public Integer getLength() {
                    return length;
                  }

                  @Override
                  public String getEmail() {
                    return email;
                  }
                })
        .collect(Collectors.toList());
  }
}
