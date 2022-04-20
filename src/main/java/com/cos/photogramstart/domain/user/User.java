package com.cos.photogramstart.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA로 생성

@Builder
@AllArgsConstructor // 전체 생성사 생성 
@NoArgsConstructor // 빈 생성자 생성
@Data
@Entity // DB에 테이블 생성
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// 번호 증가 방법을 해당 DB 방식으로 채택(MySQL, MariaDB, Oracle, ...)
	private int id;
	
	@Column(unique = true)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String name;
	private String website;
	private String bio; // 자기소개
	@Column(nullable = false)
	private String email;
	private String phone;
	private String gender;
	
	private String profileImageUrl; // 프사
	private String role; // 권한
	
	// User를 select할 때 해당 User id로 등록된 image들을 다 가져와
	// 나는 연관관계의 주인이 아니므로 테이블에 추가 하지마
	// Lazy = user를 select할 때 해당 user id로 등록된 image들을 가져오지마
	  // 대신 getImages()함수의 image들이 호출될 때 가져와
	// Eager = User를 select할 때 해당 user id로 등록된 image들을 전부 Join해서 가져와
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY) // fetch == 가져오다
	@JsonIgnoreProperties({"user"}) // -> Image 객체 내의 user 필드 무시하고 JSON 파싱, getter 호출
	private List<Image> images; // 양방향 매핑
	
	private LocalDateTime createDate;
	
	@PrePersist // DB에 INSERT되기 직전에 실행되어 createDate 참조변수에 자동으로 시각 입력
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", password=" + password + ", name=" + name + ", website="
        + website + ", bio=" + bio + ", email=" + email + ", phone=" + phone + ", gender=" + gender
        + ", profileImageUrl=" + profileImageUrl + ", role=" + role + ", createDate="
        + createDate + "]";
  }
	
}
