package com.gucardev.springboottest.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "`user`")
public class User extends BaseEntity {

  @Column(unique = true)
  private String username;

  private String email;

  private String name;

  @OneToMany(mappedBy = "user")
  private List<Address> addresses;
}
