package com.gucardev.springboottest.repository;

import com.gucardev.springboottest.model.projection.UsernameLengthProjection;
import java.util.List;

public interface UserCustomRepository {

  List<UsernameLengthProjection> getUserNamesListWithLengthGreaterThan(int length);
}
