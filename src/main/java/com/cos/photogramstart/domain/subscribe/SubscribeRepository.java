package com.cos.photogramstart.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubscribeRepository extends JpaRepository<Subscribe, Integer>{
  
  // 메서드 명에 m- 접두어 의미를 내가 만든 이란 뜻임(my)
  
  // DB를 변경하는 네이티브 쿼리에 지정필
  @Modifying
  // JpaRepository의 save 메서드를 사용하지 않으므로 시각 입력도 직접 처리
  // :변수명 -> 메서드 매개변수를 바인딩해서 처리 like 표현식${}
  @Query(value = "INSERT INTO subscribe(fromUserId, toUserId, createDate) "
      + "VALUES(:fromUserId, :toUserId, now()) ", nativeQuery = true)
  void mSubscribe(int fromUserId, int toUserId);
  // 쿼리 실행 성공한 레코드 갯수 반환, 실패시 -1 리턴 하므로 반환형이 int임
  // 0의 경우엔 오류는 없으나 처리된 레코드가 없는 경우임
  
  @Modifying
  @Query(value = "DELETE FROM subscribe WHERE fromUserId = :fromUserId AND toUserId = :toUserId", nativeQuery = true)
  void mUnSubscribe(int fromUserId, int toUserId); // 파라미터 순서에 주의
  
  // select(조회)이기 때문에 @Modifying 추가 안 함, 쿼리문 마지막에 ; 는 안 씀
  @Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :principalId AND toUserId = :pageUserId", nativeQuery = true)
  int mSubscribeState(int principalId, int pageUserId); // 파라미터 순서에 주의
  
  @Query(value = "SELECT COUNT(*) FROM subscribe WHERE fromUserId = :pageUserId", nativeQuery = true)
  int mSubscribeCount(int pageUserId);
  
}
