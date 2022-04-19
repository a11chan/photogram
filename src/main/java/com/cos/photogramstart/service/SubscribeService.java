package com.cos.photogramstart.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {
  
  private final SubscribeRepository subscribeRepository;
  private final EntityManager em;
  // 모든 repository는 EntityManager의 구현체이므로 서비스 계층에서 직접 구현가능
  
  @Transactional(readOnly = true)
  public List<SubscribeDto> 구독리스트(int principalId, int pageUserId) {
    
    // 쿼리 준비
    StringBuffer sb = new StringBuffer();
    sb.append("SELECT u.id, u.username, .u.profileImageUrl, ");
    sb.append("if((SELECT 1 FROM subscribe where fromUserId = ? AND toUserId = u.id), 1, 0) AS subscribeState, "); // ? -> principalId
    sb.append("if((? = u.id),1,0) equalUserState "); // ? -> principalId
    sb.append("FROM user u INNER JOIN subscribe s ");
    sb.append("ON u.id = s.toUserId ");
    sb.append("WHERE s.fromUserId = ?"); // ? -> pageUserId
    
    // 쿼리 완성
    Query query = em.createNativeQuery(sb.toString())
        .setParameter(1, principalId)
        .setParameter(2, principalId)
        .setParameter(3, pageUserId);
    
    // 쿼리 실행(qlrm 라이브리러리 필요 = dto에 결과 쿼리 결과를 매핑하기 위해
    JpaResultMapper result = new JpaResultMapper();
    List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
    
    return subscribeDtos;
  }
  
  @Transactional // DB에 영향을 주는 메서드에 지정
  public void 구독하기(int fromUserId, int toUserId) {
    // subscribeRepository.save(null); Subscribe 객체를 가져오기 복잡함
      // findById 메서드를 통해 객체를 받환받아서 처리 필요
    // 네이티브 쿼리가 더 간단하므로 직접 쿼리 작성
    try {
      subscribeRepository.mSubscribe(fromUserId, toUserId);
    } catch (Exception e) {
      throw new CustomApiException("이미 구독중입니다.");
    }
  }
  
  @Transactional
  public void 구독취소하기(int fromUserId, int toUserId) {
    subscribeRepository.mUnSubscribe(fromUserId, toUserId);
  }
}
