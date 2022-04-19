package com.cos.photogramstart.web.dto.user;

import javax.validation.constraints.NotBlank;

import com.cos.photogramstart.domain.user.User;

import lombok.Data;

@Data
public class UserUpdateDto {
  @NotBlank
  private String name; // not null
  @NotBlank
  private String password; // not null
  private String website;
  private String bio;
  private String phone;
  private String gender;
  
  // 선택 항목을 그대로 받아 위험, 코드 수정 필요
  public User toEntity() {
    return User.builder()
        .name(name)
        .password(password) // DB가 not null이면 예외 발생, 유효성 검사 필요
        .website(website)
        .bio(bio)
        .phone(phone)
        .gender(gender)
        .build();
  }
}
