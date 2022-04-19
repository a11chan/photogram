package com.cos.photogramstart.web.dto.user;

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor // 전체 생성자
@NoArgsConstructor // 빈 생성자
@Data
public class UserProfileDto {
  private boolean pageOwnerState;
  private int imageQty;
  private boolean subscribeState;
  private int subscribeCount;
  private User user; // 방문 페이지 유저
}
