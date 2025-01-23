package com.example.postservice.service.impl;

import com.example.postservice.Param;
import com.example.postservice.component.JsonFactory;
import com.example.postservice.component.RestFactory;
import com.example.postservice.dto.*;
import com.example.postservice.entity.Post;
import com.example.postservice.enumm.PostStatus;
import com.example.postservice.repository.IPostLikeRepository;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private IPostLikeRepository postLikeRepository;

    @Override
    public BaseResponse<Object> save(CreatePostDTO dto, BaseResponse rp) {
        if (!dto.isValid()) {
            rp.setCode("03");
            rp.setMessage("Thong tin dau vao khong hop le");
            return rp;
        }
        //check exist user
        ResponseEntity<String> res = RestFactory.postUserService(Param.baseUserUrl, Param.FUNCTION_CHECK_USER_EXIST, dto);
        rp = JsonFactory.fromJson(res.getBody(), BaseResponse.class);
        if (rp.isSuccess()) {
            Post post = new Post();
            post.setUserId(Long.parseLong(dto.getUserId()));
            post.setContent(dto.getContent());
            post.setImages(dto.getImages());
            post.setCreatedDate(new Date());
            post.setStatus(PostStatus.getPostStatus(dto.getStatus()));
            postRepository.save(post);
        }
        return rp;
    }

    @Override
    public Post findById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public List<Post> findAllByUserId(GetUserPostDTO dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageIndex(), dto.getPageSize(), Sort.by(Sort.Order.desc("createdDate")));
        return postRepository.findAllByUserIdOrderByCreatedDateDesc(Long.parseLong(dto.getUserId()), pageRequest).getContent();
    }

    @Override
    public BaseResponse<Object> findAllPostsFriendWall(GetUserPostDTO dto, BaseResponse rp) {
        //check friendship
        ResponseEntity<String> res = RestFactory.postUserService(Param.baseUserUrl, Param.FUNCTION_CHECK_FRIENDSHIP, dto);
        BaseResponse<Object> coreRp = JsonFactory.fromJson(res.getBody(), BaseResponse.class);
        //get list post
        List<String> statusList = new ArrayList<>();
        statusList.add("PUBLIC");
        if (coreRp.isSuccess()) {
            statusList.add("FRIENDS");
        }
        PageRequest pageRequest = PageRequest.of(dto.getPageIndex(), dto.getPageSize(), Sort.by(Sort.Order.desc("createdDate")));
        List<Post> posts = postRepository.findAllByUserIdAndStatusInOrderByCreatedDateDesc(Long.parseLong(dto.getFriendId()), statusList, pageRequest).getContent();
        Set<String> postIds = posts.stream().map(Post::getId).collect(Collectors.toSet());
        //create 3 thread for: count like, count comment, check user like
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Callable<List<PostStatDTO>> task1 = () -> countLikeAndCommentListPost(postIds);
        Callable<List<CheckUserLikeDTO>> task2 = () -> checkUserLikeListPost(postIds, Long.parseLong(dto.getUserId()));
        Future<List<PostStatDTO>> future1 = executor.submit(task1);
        Future<List<CheckUserLikeDTO>> future2 = executor.submit(task2);
        List<PostStatDTO> postStatDTOList = null;
        List<CheckUserLikeDTO> checkUserLikeDTOS = null;
        try {
            postStatDTOList = future1.get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            checkUserLikeDTOS = future2.get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Map<String, PostStatDTO> postStatDTOMap = postStatDTOList.stream().collect(Collectors.toMap(PostStatDTO::getPostId, postStatDTO -> postStatDTO));
        Map<String, CheckUserLikeDTO> checkUserLikeDTOMap = checkUserLikeDTOS.stream().collect(Collectors.toMap(CheckUserLikeDTO::getPostId, checkUserLikeDTO -> checkUserLikeDTO));
        posts.stream().forEach(post -> {
            PostStatDTO postStatDTO = postStatDTOMap.get(post.getId());
            CheckUserLikeDTO checkUserLikeDTO = checkUserLikeDTOMap.get(post.getId());
            post.setCountLike(postStatDTO.getLikeCount());
            post.setCountComment(postStatDTO.getCommentCount());
            post.setCheck_user_like(checkUserLikeDTO.getHasLiked());
        });
        rp.setData(posts);
        return rp;
    }

    private List<PostStatDTO> countLikeAndCommentListPost(Set<String> postIds) {
        return postLikeRepository.findPostStatsByIds(postIds);
    }

    private List<CheckUserLikeDTO> checkUserLikeListPost(Set<String> postIds, Long userId) {
        return postLikeRepository.findCheckUserLikesByPostIdAndUserId(postIds, userId);
    }
}
