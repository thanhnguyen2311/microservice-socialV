package com.example.postservice.service.impl;

import com.example.postservice.Param;
import com.example.postservice.component.JsonFactory;
import com.example.postservice.component.RestFactory;
import com.example.postservice.dto.*;
import com.example.postservice.entity.Post;
import com.example.postservice.enumm.PostStatus;
import com.example.postservice.repository.ICommentRepository;
import com.example.postservice.repository.IPostLikeRepository;
import com.example.postservice.repository.PostRepository;
import com.example.postservice.service.IPostService;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService implements IPostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private IPostLikeRepository postLikeRepository;
    @Autowired
    private ICommentRepository commentRepository;

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
    public BaseResponse<Object> update(UpdatePostDTO dto, BaseResponse rp) {
        if (!dto.isValid()) {
            rp.setCode("03");
            rp.setMessage("Thong tin dau vao khong hop le");
            return rp;
        }
        //check exist post
        Optional<Post> post = postRepository.findById(dto.getId());
        if (post.isPresent()) {
            post.get().setContent(dto.getContent());
            post.get().setImages(dto.getImages());
            post.get().setStatus(PostStatus.getPostStatus(dto.getStatus()));
            post.get().setModifiedDate(new Date());
            postRepository.save(post.get());
        } else {
            rp.setCode("04");
            rp.setMessage("Post doesn't exist");
        }
        return rp;
    }


    @Override
    public Post findById(String id) {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public BaseResponse<Object> delete(String id, BaseResponse rp) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            postRepository.delete(post.get());
        } else {
            rp.setCode("04");
            rp.setMessage("Post doesn't exist");
        }
        return rp;
    }

    @Override
    public BaseResponse<Object> findAllByUserId(GetUserPostDTO dto, BaseResponse rp) {
        PageRequest pageRequest = PageRequest.of(dto.getPageIndex(), dto.getPageSize(), Sort.by(Sort.Order.desc("createdDate")));
        List<Post> posts = postRepository.findAllByUserIdOrderByCreatedDateDesc(Long.parseLong(dto.getUserId()), pageRequest).getContent();
        Set<String> postIds = posts.stream().map(Post::getId).collect(Collectors.toSet());
        //create 2 thread for: count like, count comment, check user like
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
            log.info(e.getMessage());
        }
        try {
            checkUserLikeDTOS = future2.get();
        } catch (Exception e) {
            log.info(e.getMessage());
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
        //create 2 thread for: count like, count comment, check user like
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
            log.info(e.getMessage());
        }
        try {
            checkUserLikeDTOS = future2.get();
        } catch (Exception e) {
            log.info(e.getMessage());
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

    @Override
    public BaseResponse<Object> findAllPostsNewFeed(GetUserPostDTO dto, BaseResponse rp) {
        //get list friend
        String res = RestFactory.getUserService(Param.baseUserUrl, Param.FUNCTION_GET_LIST_FRIEND + dto.getUserId());
        Type type = new TypeToken<BaseResponse<List<UserInfo>>>() {}.getType();
        BaseResponse<List<UserInfo>> coreRp = JsonFactory.fromJson(res, type);
        List<UserInfo> friendListDTOList = coreRp.getData();
        Map<String, UserInfo> userInfoMap = friendListDTOList.stream().collect(Collectors.toMap(UserInfo::getId, userInfo -> userInfo));
        //get list post
        Set<Long> friendIds = friendListDTOList.stream().map(userInfo -> Long.parseLong(userInfo.getId())).collect(Collectors.toSet());
        List<String> statusList = Arrays.asList("PUBLIC", "FRIENDS");
        PageRequest pageRequest = PageRequest.of(dto.getPageIndex(), dto.getPageSize(), Sort.by(Sort.Order.desc("createdDate")));
        List<Post> posts = postRepository.findAllByUserIdInAndStatusInOrderByCreatedDateDesc(friendIds, statusList, pageRequest).getContent();
        Set<String> postIds = posts.stream().map(Post::getId).collect(Collectors.toSet());
        //create 2 thread for: count like, count comment, check user like
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Callable<List<PostStatDTO>> task1 = () -> countLikeAndCommentListPost(postIds);
        Callable<List<CheckUserLikeDTO>> task2 = () -> checkUserLikeListPost(postIds, Long.parseLong(dto.getUserId()));
        Future<List<PostStatDTO>> future1 = executor.submit(task1);
        Future<List<CheckUserLikeDTO>> future2 = executor.submit(task2);
        List<PostStatDTO> postStatDTOList = null;
        List<CheckUserLikeDTO> checkUserLikeDTOS = null;
        try {
            postStatDTOList = future1.get();
            checkUserLikeDTOS = future2.get();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        Map<String, PostStatDTO> postStatDTOMap = postStatDTOList.stream().collect(Collectors.toMap(PostStatDTO::getPostId, postStatDTO -> postStatDTO));
        Map<String, CheckUserLikeDTO> checkUserLikeDTOMap = checkUserLikeDTOS.stream().collect(Collectors.toMap(CheckUserLikeDTO::getPostId, checkUserLikeDTO -> checkUserLikeDTO));
        posts.forEach(post -> {
            PostStatDTO postStatDTO = postStatDTOMap.get(post.getId());
            CheckUserLikeDTO checkUserLikeDTO = checkUserLikeDTOMap.get(post.getId());
            post.setCountLike(postStatDTO.getLikeCount());
            post.setCountComment(postStatDTO.getCommentCount());
            post.setCheck_user_like(checkUserLikeDTO.getHasLiked());
            post.setUserInfo(userInfoMap.get(String.valueOf(post.getUserId())));
        });
        rp.setData(posts);
        return rp;
    }

    @Override
    public BaseResponse<Object> getPostDetail(PostDetailRq rq, BaseResponse rp) {
        Post post = postRepository.findById(rq.getPostId()).orElse(null);
        if (post != null) {
            ExecutorService executor = Executors.newFixedThreadPool(4);
            Callable<UserInfo> task = () -> {
                String res = RestFactory.getUserService(Param.baseUserUrl, Param.FUNCTION_GET_USER_INFO + post.getUserId());
                Type type = new TypeToken<BaseResponse<UserInfo>>() {}.getType();
                BaseResponse<UserInfo> coreRp = JsonFactory.fromJson(res, type);
                return coreRp.getData();
            };
            Callable<Integer> task1 = () -> postLikeRepository.countAllByPostId(rq.getPostId());
            Callable<Integer> task2 = () -> commentRepository.countAllByPostId(rq.getPostId());
            Callable<Boolean> task3 = () -> postLikeRepository.existsByPostIdAndUserId(rq.getPostId(), Long.parseLong(rq.getUserId()));
            Future<UserInfo> future = executor.submit(task);
            Future<Integer> future1 = executor.submit(task1);
            Future<Integer> future2 = executor.submit(task2);
            Future<Boolean> future3 = executor.submit(task3);
            try {
                post.setCountLike(future1.get());
                post.setCountComment(future2.get());
                post.setCheck_user_like(future3.get() ? 1: 0);
                post.setUserInfo(future.get());
            } catch (Exception e) {
                log.info(e.getMessage());
            }
            rp.setData(post);
        }
        return rp;
    }

    private List<PostStatDTO> countLikeAndCommentListPost(Set<String> postIds) {
        return postLikeRepository.findPostStatsByIds(postIds);
    }

    private List<CheckUserLikeDTO> checkUserLikeListPost(Set<String> postIds, Long userId) {
        return postLikeRepository.findCheckUserLikesByPostIdAndUserId(postIds, userId);
    }
}
