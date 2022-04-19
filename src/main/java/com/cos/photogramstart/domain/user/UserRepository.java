package com.cos.photogramstart.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

// 어노테이션 없어도 JpaRepository를 상속하면 IoC 자동으로 등록
public interface UserRepository extends JpaRepository<User,Integer> {
  // JPA query method 사용
  User findByUsername(String username);
}
