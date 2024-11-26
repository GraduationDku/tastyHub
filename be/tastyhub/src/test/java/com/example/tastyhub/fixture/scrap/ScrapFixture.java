package com.example.tastyhub.fixture.scrap;

import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;
import static com.example.tastyhub.fixture.user.UserFixture.USER;

import com.example.tastyhub.common.domain.scrap.dtos.PagingScrapResponse;
import com.example.tastyhub.common.domain.scrap.entity.Scrap;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ScrapFixture {

  public static final Scrap SCRAP = Scrap.builder()
      .id(1L)
      .user(USER)
      .recipe(RECIPE)
      .build();

  public static final PagingScrapResponse PAGING_SCRAP_RESPONSE = PagingScrapResponse.builder()
      .foodId(SCRAP.getId())
      .foodName(RECIPE.getFoodName())
      .foodImgUrl(RECIPE.getRecipeImgUrl())
      .build();

  // 페이지 요청 생성 (PageRequest)
  private static final PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

  // PageImpl을 사용하여 페이지 데이터 구성
  public static final Page<PagingScrapResponse> PAGING_SCRAP_RESPONSES = new PageImpl<>(
      List.of(PAGING_SCRAP_RESPONSE), // 데이터 리스트
      pageable,                            // 페이지 요청 정보
      1                                    // 전체 데이터 개수
  );
}
