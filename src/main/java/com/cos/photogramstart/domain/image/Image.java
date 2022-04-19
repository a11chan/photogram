package com.cos.photogramstart.domain.image;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.likes.Likes;
import com.cos.photogramstart.domain.subscribe.Subscribe;
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
public class Image { // N 1
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // 번호 증가 방법을 해당 DB 방식으로 채택(MySQL, MariaDB, Oracle, ...)
  private int id;
  private String caption; // 그림 내용
  // 사진을 전송받아서 서버의 특정 폴더에 저장 후 DB에 그 저장 경로를 삽입 예정
  private String postImageUrl;

  @JsonIgnoreProperties({"images"}) // 유저 정보가 들고 있는 이미지는 불필요
  @JoinColumn(name="userId") // 컬럼 이름 지정 for 외래키 사용
  @ManyToOne(fetch = FetchType.EAGER) // 이미지를 select하면 조인해서 User정보를 같이 들고 옴
  //이미지 업로드 주체를 알아야 하므로 필요, DB에 외래키로 저장됨
  private User user; // 1 1
  
  @JsonIgnoreProperties({"image"})
  // OneToMany는 기본값으로 LAZY로딩, Image를 select 시 getter로 호출할 때 로딩
  // 이미지 좋아요 - 양방향 매핑
  @OneToMany(mappedBy = "image") // "image"는 likes의 필드명, 외래키 생성 안 함
  private List<Likes> likes;
  
  // 이미지 댓글
  @OrderBy("id DESC")
  @JsonIgnoreProperties({"image"})
  @OneToMany(mappedBy = "image") // Comment 내의 image와 매핑, Comment 내 image 필드는 외래키로 설정, Lazy로딩 기본값
  private List<Comment> comments;
  
  
  private LocalDateTime createDate;

  // DB에 INSERT되기 직전에 실행되어 createDate 참조변수에 자동으로 시각 입력
  @PrePersist 
  public void createDate() {
    this.createDate = LocalDateTime.now();
  }
  
  @Transient // DB에 칼럼 생성 안 됨
  private boolean likeState;
  
  @Transient
  private int likeCount;
  
  // 오브젝트를 콘솔에 출력할 때 무한참조 문제로 User 부분을 삭제
  @Override
  public String toString() {
    return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl 
        + ", createDate=" + createDate + "]";
  }
  
}
