package com.gucardev.springboottest.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Address extends BaseEntity {

  @Column(nullable = false)
  private String title;

  private String detail;

  @ManyToOne
  @JoinColumn(name="user_id")
  private User user;
}
