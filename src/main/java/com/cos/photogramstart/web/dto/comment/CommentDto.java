package com.cos.photogramstart.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CommentDto {
  @NotBlank // 빈값, null, 빈 공백값 체크
  private String content;
  @NotNull // 모든 null 비허용, 기본자료형 int로 하면 에러 발생, Wrapper 클래스로 사용
  private Integer imageId;
  // @NotEmpty == 빈 값, null, 빈 공백(space) 비허용
  
  // toEntity가 필요 없다. 이유는 나중에 설명
}