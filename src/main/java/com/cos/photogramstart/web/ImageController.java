package com.cos.photogramstart.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.handler.ex.CustomValidationException;
import com.cos.photogramstart.service.ImageService;
import com.cos.photogramstart.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {

  private final ImageService imageService;
  
  @GetMapping({"/","/image/story"})
  public String story() {
    return "image/story";
  }
  
  @GetMapping({"/image/popular"})
  public String popular(Model model) {
    // ApiController는 데이터(json)를 리턴, Controller는 정적 페이지(views 폴더 내)를 리턴
    // ajax를 써야할 때 ApiController 사용, API로 구현한다면 브라우저에서 요청하는 게 아니라
    // 안드, iOS에서 요청하는 경우 필요
    List<Image> images = imageService.인기사진();
    
    model.addAttribute("images",images);
    
    return "/image/popular";
  }
  
  @GetMapping({"/image/upload"})
  public String upload() {
    return "/image/upload";
  }
  
  @PostMapping("/image")
  public String imageUpload(ImageUploadDto imageUploadDto,
      @AuthenticationPrincipal PrincipalDetails principalDetails) {
    
    if(imageUploadDto.getFile().isEmpty()) {
      throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
      // BindingResult 미사용이므로 null 처리
      // AuthController 클래스 참고, 페이지 응답 예외이므로 위의 예외 적용
    }
    
    // 서비스 호출
    imageService.사진업로드(imageUploadDto, principalDetails);
    return "redirect:/user/"+principalDetails.getUser().getId(); // 업로드 완료 시 경로
  }
  
}
