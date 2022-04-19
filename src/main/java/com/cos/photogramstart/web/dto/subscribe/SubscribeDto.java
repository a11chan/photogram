package com.cos.photogramstart.web.dto.subscribe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDto {
  // DB 쿼리문에 맞게 필드명 변경
  private int id;
  private String username;
  private String profileImageUrl;
  // 마리아 DB는 int를 래퍼 객체로 보내야 정상 작동
  private Integer subscribeState;
  private Integer equalUserState;
}
