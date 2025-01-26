package com.example.postservice.service.impl;

import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreateCommentDTO;
import com.example.postservice.dto.UpdateCommentDTO;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.CommentLike;
import com.example.postservice.enumm.CommentType;
import com.example.postservice.repository.ICommentRepository;
import com.example.postservice.service.ICommentService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    @Override
    public BaseResponse<Object> delete(Long id, BaseResponse rp) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            commentRepository.delete(comment.get());
            if (comment.get().getType().equals(CommentType.PARENT_CMT)) {
                commentRepository.deleteAllByParentCommentId(id);
            }
        } else {
            rp.setCode("04");
            rp.setMessage("Comment doesn't exist");
        }
        return rp;
    }

    @Override
    public BaseResponse<Object> update(UpdateCommentDTO dto, BaseResponse rp) {
        if (!dto.isValid()) {
            rp.setCode("03");
            rp.setMessage("Thong tin dau vao khong hop le");
            return rp;
        }
        Optional<Comment> comment = commentRepository.findById(dto.getId());
        if (comment.isPresent()) {
            comment.get().setContent(dto.getContent());
            comment.get().setModifiedDate(new Date());
            commentRepository.save(comment.get());
        } else {
            rp.setCode("04");
            rp.setMessage("Comment doesn't exist");
        }
        return rp;
    }

    @Override
    public List<Long> findAllCommentId() {
        return commentRepository.findAll().stream().map(Comment::getId)
                .toList();
    }


}
