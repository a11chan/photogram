package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final SubscribeRepository subscribeRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Value("${file.path}")
  private String uploadFolder;
  
  @Transactional // -> 더티체킹으로 업데이트됨
  public User 회원프로필사진변경(int principalId, MultipartFile profileImageFile) {
    UUID uuid = UUID.randomUUID();
    String imageFileName = uuid+"_"+profileImageFile.getOriginalFilename();
    System.out.println("이미지 파일 이름: "+imageFileName);
    
    // 서버에 저장할 위치 지정
    Path imageFilePath = Paths.get(uploadFolder+imageFileName);
    
    // 통신&하드 입출력(I/O) 장애시 예외 발생 가능 - 런타임 에러, 컴파일 시엔 확인 불가
    try {
      Files.write(imageFilePath, profileImageFile.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    User userEntity = userRepository.findById(principalId).orElseThrow(()->{
      throw new CustomApiException("유저를 찾을 수 없습니다.");
    });
    
    userEntity.setProfileImageUrl(imageFileName);
    
    return userEntity;
  }
  
  @Transactional(readOnly = true) // readOnly = true -> 영속성 컨텍스트가 내용변경 감지 안 함
  public UserProfileDto 회원프로필(int pageUserId, int principalId) {
    UserProfileDto userProfileDto = new UserProfileDto();
    
    // == select * from image where userId = userId;
    User userEntity = userRepository.findById(pageUserId).orElseThrow(()->{
      throw new CustomException("해당 페이지는 존재하지 않습니다.");
    }); // 해당 ID를 못 찾을 수도 있기 때문에 Optional 처리 -> orElseThrow 사용
    
    userProfileDto.setUser(userEntity);
    userProfileDto.setPageOwnerState(pageUserId == principalId); // 1 == 페이지 주인, -1 은 아님
    userProfileDto.setImageQty(userEntity.getImages().size());
    
    int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId); // 파라미터 순서 주의
    int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
    
    userProfileDto.setSubscribeState(subscribeState == 1);
    userProfileDto.setSubscribeCount(subscribeCount);
    
    userEntity.getImages().forEach((image)->{
      image.setLikeCount(image.getLikes().size());
    });
    
    return userProfileDto;
  } 
  
  
  @Transactional
  public User 회원수정(int id, User user) {
    // 1. 영속화
    User userEntity = userRepository.findById(id).orElseThrow(() -> {
      return new CustomValidationApiException("찾을 수 없는 ID입니다.");
    });

    // 2. 영속화된 객체를 수정
    userEntity.setName(user.getName());
    
    String rawPassword = user.getPassword();
    String encPassword = bCryptPasswordEncoder.encode(rawPassword);
    
    userEntity.setPassword(encPassword);
    userEntity.setBio(user.getBio());
    userEntity.setWebsite(user.getWebsite());
    userEntity.setPhone(user.getPhone());
    userEntity.setGender(user.getGender());
    
    return userEntity;
    // 3. 더티체킹이 되고 업데이트 완료
  }
}
