package com.gucardev.springboottest.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageWrapper<T> {

  private List<T> content;
  private long totalElements;
  private int totalPages;
  private boolean last;

}

