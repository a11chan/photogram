package com.cos.photogramstart.domain.comment;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor // 전체 생성사 생성
@NoArgsConstructor // 빈 생성자 생성
@Data
@Entity // DB에 테이블 생성
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 방법을 해당 DB 방식으로 채택(MySQL, MariaDB, Oracle, ...)
  private int id;
  
  @Column(length = 100, nullable = false)
  private String content;
  
  @JsonIgnoreProperties({"images"})
  @JoinColumn(name = "userId") // 컬럼명 사용자 지정
  @ManyToOne // ManyToOne은 (fetch = FetchType.EAGER)이 기본
  private User user;
  
  
  @JoinColumn(name = "imageId") // 컬럼명 사용자 지정
  @ManyToOne
  private Image image;
  
  private LocalDateTime createDate;

  // DB에 INSERT되기 직전에 실행되어 createDate 참조변수에 자동으로 시각 입력
  @PrePersist 
  public void createDate() {
    this.createDate = LocalDateTime.now();
  }
}
