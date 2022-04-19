package com.cos.photogramstart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.domain.comment.CommentRepository;
import com.cos.photogramstart.domain.image.Image;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  
  @Transactional
  public Comment 댓글쓰기(String content, int imageId, int userId) {
    
    // DB에서 id에 맞는 객체 불러와야 하나 가상 객체를 만들어 우회
    // 대신 return 시에 image객체와 user객체는 id값만 가지고 있는 빈 객체를 리턴
    Image image = new Image();
    image.setId(imageId);
    
    // DB에서 불러온 값이므로 user -> userEntity가 적합
    User userEntity = userRepository.findById(userId).orElseThrow(()->{
      throw new CustomApiException("유저 아이디를 찾을 수 없습니다.");
    });
    
    Comment comment = new Comment();
    comment.setContent(content);
    comment.setImage(image);
    comment.setUser(userEntity);
    
    return commentRepository.save(comment);
  }
  
  @Transactional
  public void 댓글삭제(int id) {
    try {
      commentRepository.deleteById(id);
    } catch (Exception e) {
      throw new CustomApiException(e.getMessage());
    }
  }
}