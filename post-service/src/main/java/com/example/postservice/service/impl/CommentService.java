package com.example.postservice.service.impl;

import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreateCommentDTO;
import com.example.postservice.entity.Comment;
import com.example.postservice.enumm.CommentType;
import com.example.postservice.repository.ICommentRepository;
import com.example.postservice.service.ICommentService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private ICommentRepository commentRepository;

    @Override
    public BaseResponse<Object> save(CreateCommentDTO dto, BaseResponse<Object> rp) {
        if (dto.isValid()) {
            Comment comment = new Comment();
            comment.setContent(dto.getContent());
            comment.setUserId(Long.parseLong(dto.getUserId()));
            comment.setPostId(dto.getPostId());
            comment.setCreatedDate(new Date());
            comment.setType(CommentType.getCommentType(dto.getCommentType()));
            if (Strings.isNotBlank(dto.getParentCommentId())) {
                comment.setParentCommentId(Long.parseLong(dto.getParentCommentId()));
            }
            commentRepository.save(comment);
        } else {
            rp.setCode("03");
            rp.setMessage("Thong tin dau vao khong hop le");
        }
        return rp;
    }
}
