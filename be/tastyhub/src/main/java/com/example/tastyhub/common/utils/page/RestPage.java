package com.example.tastyhub.common.utils.page;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class RestPage<T> extends PageImpl<T> {

  @JsonCreator(mode = Mode.PROPERTIES)
  public RestPage(@JsonProperty("content") java.util.List<T> content,
      @JsonProperty("number") int page,
      @JsonProperty("size") int size,
      @JsonProperty("totalSize") long totalSize) {
    super(content, PageRequest.of(page, size),totalSize);
  }

  public RestPage(List content, Pageable pageable,
      long total) {
    super(content, pageable, total);
  }

  public RestPage(List content) {
    super(content);
  }
}
