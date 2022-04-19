package com.cos.photogramstart.domain.subscribe;

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

import com.cos.photogramstart.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor // 전체 생성사 생성
@NoArgsConstructor // 빈 생성자 생성
@Data
@Entity // DB에 테이블 생성
@Table( // 제약조건 형식은 문서 보고 적어도 됨 
    uniqueConstraints = {
        @UniqueConstraint(
            name="subscribe_uk",
            columnNames = {"fromUserId","toUserId"}
            )
    }
)
public class Subscribe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // 번호 증가 방법을 해당 DB 방식으로 채택(MySQL, MariaDB, Oracle, ...)
  private int id;

  @JoinColumn(name="fromUserId") // 컬럼명 사용자 지정 가능 - @JoinColumn
  @ManyToOne
  // 구독신청자
  private User fromUser;

  @JoinColumn(name="toUserId")
  @ManyToOne
  // 구독제공자
  private User toUser;

  private LocalDateTime createDate;

  // DB에 INSERT되기 직전에 실행되어 createDate 참조변수에 자동으로 시각 입력
  @PrePersist 
  public void createDate() {
    this.createDate = LocalDateTime.now();
  }
}
