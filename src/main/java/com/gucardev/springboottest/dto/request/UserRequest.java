package com.gucardev.springboottest.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

  private Long id;

  @NotBlank
  @Length(max = 30, min = 3)
  private String username;

  @Email @NotBlank private String email;
  @NotBlank private String name;
}
