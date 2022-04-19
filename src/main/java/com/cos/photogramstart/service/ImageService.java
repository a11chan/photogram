package com.cos.photogramstart.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
  
  // DB에 저장을 위해 필요
  private final ImageRepository imageRepository;
  
  @Transactional(readOnly = true)
  public List<Image> 인기사진() {
    return imageRepository.mPopular();
  }
  
  @Transactional(readOnly = true) // readOnly true -> 더티체킹 -> flush(반영) X -> 성능향상
  public Page<Image> 이미지스토리(int principalId, Pageable pageable) {
    Page<Image> images = imageRepository.mStory(principalId, pageable);
    
    //images에 좋아요 상태 담기
    images.forEach((image)->{
      
      image.setLikeCount(image.getLikes().size());
      
      image.getLikes().forEach((like)->{
        // 이미지에 좋아요 누른 사람 중에 로그인 사용자가 있는지 비교
        if(like.getUser().getId() == principalId) {
          image.setLikeState(true);
        }
      });
    });
    
    return images;
  }
  
  
  @Value("${file.path}")
  private String uploadFolder;
  
  @Transactional
  public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
    UUID uuid = UUID.randomUUID();
    String imageFileName = uuid+"_"+imageUploadDto.getFile().getOriginalFilename();
    System.out.println("이미지 파일 이름: "+imageFileName);
    
    // 서버에 저장할 위치 지정
    Path imageFilePath = Paths.get(uploadFolder+imageFileName);
    
    // 통신&하드 입출력(I/O) 장애시 예외 발생 가능 - 런타임 에러, 컴파일 시엔 확인 불가
    try {
      Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // image 테이블에 저장
    Image image = imageUploadDto.toEntity(imageFileName,principalDetails.getUser()); // UUID+파일명
    imageRepository.save(image);
    
  }
}
