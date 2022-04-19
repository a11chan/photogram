package com.cos.photogramstart.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.subscribe.Subscribe;
import com.cos.photogramstart.domain.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor // 전체 생성자 생성
@NoArgsConstructor // 빈 생성자 생성
@Data
@Entity // DB에 테이블 생성
@Table( // 제약조건 형식은 문서 보고 적어도 됨 
    uniqueConstraints = {
        @UniqueConstraint(
            name="likes_uk",
            columnNames = {"imageId","userId"}
            // └> 서로 중복되지 않게 함--1번 유저가 계속해서 2번 이미지 좋아요 불가
            )
    }
)
public class Likes { // N
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // 번호 증가 방법을 해당 DB 방식으로 채택(MySQL, MariaDB, Oracle, ...)
  private int id;
  
  @JoinColumn(name = "imageId") // 테이블 필드명 사용자 지정 어노테이션
  @ManyToOne // ManyToOne == 기본 fetch 전략이 EAGER, OneToMany는 LAZY
  private Image image; // 1
  
  @JsonIgnoreProperties({"images"})
  @JoinColumn(name = "userId")
  @ManyToOne 
  private User user; // 1
  
  private LocalDateTime createDate;

  // DB에 INSERT되기 직전에 실행되어 createDate 참조변수에 자동으로 시각 입력
  @PrePersist 
  public void createDate() {
    this.createDate = LocalDateTime.now();
  }
}
