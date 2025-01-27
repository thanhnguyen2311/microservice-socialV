package com.example.postservice.service.impl;

import com.example.postservice.Param;
import com.example.postservice.component.JsonFactory;
import com.example.postservice.component.RestFactory;
import com.example.postservice.dto.*;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.CommentLike;
import com.example.postservice.enumm.CommentType;
import com.example.postservice.repository.ICommentLikeRepository;
import com.example.postservice.repository.ICommentRepository;
import com.example.postservice.service.ICommentService;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService implements ICommentService {
    @Autowired
    private ICommentRepository commentRepository;
    @Autowired
    private ICommentLikeRepository commentLikeRepository;

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

    @Override
    public BaseResponse<Object> getListCommentPostDetail(PostDetailRq rq, BaseResponse rp) {
        List<Comment> comments = commentRepository.findAllByPostId(rq.getPostId());
        Set<Long> commentIds = comments.stream().map(Comment::getId).collect(Collectors.toSet());
        Set<Long> userIds = comments.stream().map(Comment::getUserId).collect(Collectors.toSet());
        //create 2 thread for: count like comment, check like, info user comment
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Callable<List<UserInfo>> task = () -> {
            ResponseEntity<String> res = RestFactory.postUserService(Param.baseUserUrl, Param.FUNCTION_GET_LIST_USER_INFO, new InfoListUserRq(userIds));
            Type type = new TypeToken<BaseResponse<List<UserInfo>>>() {}.getType();
            BaseResponse<List<UserInfo>> coreRp = JsonFactory.fromJson(res.getBody(), type);
            return coreRp.getData();
        };
        Callable<List<CommentStatDTO>> task1 = () -> commentLikeRepository.countLikeListComment(commentIds);
        Callable<List<CheckUserLikeCommentDTO>> task2 = () -> commentLikeRepository.checkLikeListComment(commentIds, Long.parseLong(rq.getUserId()));
        Future<List<UserInfo>> future = executor.submit(task);
        Future<List<CommentStatDTO>> future1 = executor.submit(task1);
        Future<List<CheckUserLikeCommentDTO>> future2 = executor.submit(task2);
        List<CommentStatDTO> commentStatDTOS = null;
        List<CheckUserLikeCommentDTO> checkUserLikeCommentDTOS = null;
        List<UserInfo> userInfoList = null;
        try {
            commentStatDTOS = future1.get();
            checkUserLikeCommentDTOS = future2.get();
            userInfoList = future.get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Map<Long, Long> commentStatDTOMap = commentStatDTOS.stream().collect(Collectors.toMap(CommentStatDTO::getCommentId, CommentStatDTO::getCountLike));
        Map<Long, Integer> checkUserLikeCommentDTOMap = checkUserLikeCommentDTOS.stream().collect(Collectors.toMap(CheckUserLikeCommentDTO::getCommentId, CheckUserLikeCommentDTO::getHasLiked));
        Map<String, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getId, dto -> dto));
        comments.forEach(c -> {
            c.setCountLike(commentStatDTOMap.get(c.getId()));
            c.setCheck_user_like(checkUserLikeCommentDTOMap.get(c.getId()));
            c.setUserInfo(userInfoMap.get(String.valueOf(c.getUserId())));
        });
        rp.setData(comments);
        return rp;
    }
}
