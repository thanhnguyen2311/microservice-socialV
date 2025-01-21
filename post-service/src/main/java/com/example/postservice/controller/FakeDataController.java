package com.example.postservice.controller;

import com.example.postservice.dto.BaseResponse;
import com.example.postservice.dto.CreateCommentDTO;
import com.example.postservice.dto.CreatePostDTO;
import com.example.postservice.entity.Comment;
import com.example.postservice.entity.Post;
import com.example.postservice.entity.PostLike;
import com.example.postservice.enumm.CommentType;
import com.example.postservice.service.ICommentService;
import com.example.postservice.service.IPostService;
import com.example.postservice.service.IPostLikeService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fake-data")
public class FakeDataController {

    private static List<String> photoUrlList = Arrays.asList(
            "https://cdn.pixabay.com/photo/2020/09/20/16/50/big-buddha-5587706_1280.jpg",
            "https://pixabay.com/vi/photos/image-7781184/",
            "https://pixabay.com/vi/photos/image-9304011/",
            "https://pixabay.com/vi/playlists/running-22335330/",
            "https://cdn.pixabay.com/photo/2022/10/22/13/41/paris-7539257_640.jpg",
            "https://cdn.pixabay.com/photo/2023/07/27/14/52/universe-8153526_640.jpg",
            "https://cdn.pixabay.com/photo/2024/08/15/19/19/highland-cow-8972000_640.jpg",
            "https://cdn.pixabay.com/photo/2021/10/02/21/00/ural-owl-6676441_640.jpg",
            "https://cdn.pixabay.com/photo/2024/07/01/10/50/flycatcher-8864922_640.jpg",
            "https://cdn.pixabay.com/photo/2024/06/07/14/45/winter-8814783_640.jpg",
            "https://cdn.pixabay.com/photo/2025/01/12/15/57/diver-9328628_640.jpg",
            "https://cdn.pixabay.com/photo/2024/12/23/17/14/switzerland-9287004_640.jpg",
            "https://cdn.pixabay.com/photo/2023/12/07/11/11/girl-8435339_640.png",
            "https://cdn.pixabay.com/photo/2025/01/12/18/47/mountain-9328919_640.jpg",
            "https://cdn.pixabay.com/photo/2024/10/12/03/15/women-9114238_640.jpg",
            "https://cdn.pixabay.com/photo/2025/01/07/16/54/woman-9317323_640.jpg",
            "https://cdn.pixabay.com/photo/2024/12/28/03/31/green-tree-python-9295182_640.jpg",
            "https://cdn.pixabay.com/photo/2024/12/31/01/02/costa-rica-9301364_640.jpg",
            "https://cdn.pixabay.com/photo/2024/12/28/03/39/vietnam-9295186_640.jpg",
            "https://cdn.pixabay.com/photo/2024/12/04/15/37/leaves-9244714_640.jpg",
            "https://cdn.pixabay.com/photo/2024/06/22/09/08/childs-hand-8845806_640.jpg",
            "https://cdn.pixabay.com/photo/2023/10/17/11/18/beetle-8320899_640.jpg",
            "https://cdn.pixabay.com/photo/2024/06/25/21/42/hd-wallpaper-8853669_640.png",
            "https://cdn.pixabay.com/photo/2022/05/23/10/57/leaf-7215867_640.jpg",
            "https://cdn.pixabay.com/photo/2024/09/11/06/00/character-9038820_640.jpg",
            "https://cdn.pixabay.com/photo/2023/12/30/21/14/fields-8478994_640.jpg",
            "https://cdn.pixabay.com/photo/2022/08/19/13/31/woman-7396948_640.jpg",
            "https://cdn.pixabay.com/photo/2020/05/19/13/21/star-5190776_640.jpg",
            "https://cdn.pixabay.com/photo/2024/12/13/14/45/real-estate-9265386_640.jpg",
            "https://cdn.pixabay.com/photo/2021/04/07/13/09/headphones-6159058_640.jpg",
            "https://cdn.pixabay.com/photo/2020/11/22/11/53/sky-5766341_640.jpg",
            "https://cdn.pixabay.com/photo/2024/10/03/10/25/dive-9093321_640.jpg",
            "https://cdn.pixabay.com/photo/2020/04/04/21/04/ocean-5003864_640.jpg",
            "https://cdn.pixabay.com/photo/2024/02/24/20/48/samaritaine-8594718_640.jpg",
            "https://cdn.pixabay.com/photo/2024/05/30/08/48/pattern-8798134_640.png",
            "https://cdn.pixabay.com/photo/2024/11/04/21/39/kingfisher-9174586_640.jpg",
            "https://cdn.pixabay.com/photo/2024/11/21/22/06/deer-9214838_640.jpg",
            "https://cdn.pixabay.com/photo/2024/05/26/15/27/kid-8788962_640.jpg",
            "https://cdn.pixabay.com/photo/2021/10/02/11/43/empire-state-building-6675010_640.jpg",
            "https://cdn.pixabay.com/photo/2024/07/14/00/34/great-wall-motor-8893216_640.jpg",
            "https://cdn.pixabay.com/photo/2024/11/17/12/46/flower-9203759_640.png",
            "https://cdn.pixabay.com/photo/2023/10/04/13/37/lemon-8293727_640.jpg",
            "https://cdn.pixabay.com/photo/2022/02/09/12/16/wedding-7003291_640.jpg"
    );

    @Autowired
    private Faker faker;
    @Autowired
    private IPostService postService;
    @Autowired
    private IPostLikeService postLikeService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/gen-post")
    public String generatePost() {
        BaseResponse<Object> response = new BaseResponse<>();
        List<String> statusOptions = Arrays.asList("PUBLIC", "PRIVATE", "FRIENDS");
        CreatePostDTO createPostDTO = new CreatePostDTO();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            createPostDTO.setUserId("34");
            createPostDTO.setImages(generateImages());
            createPostDTO.setStatus(statusOptions.get(random.nextInt(statusOptions.size())));
            createPostDTO.setContent(faker.lorem().sentence(10));
            postService.save(createPostDTO, response);
        }
        return "done";
    }

    @PostMapping("/gen-like")
    public String generateLike() {
        Random random = new Random();

        List<String> postIds = getAllPostIds();
        for (int i = 0; i < 5000; i++) {
            PostLike postLike = new PostLike();
            postLike.setUserId((long) faker.number().numberBetween(1, 100));
            postLike.setCreatedDate(new Date());
            postLike.setPostId(postIds.get(random.nextInt(postIds.size())));
            if (!postLikeService.existsByPostIdAndAndUserId(postLike.getPostId(), postLike.getUserId())) {
                postLikeService.save(postLike);
            }
        }
        return "done";
    }

    @PostMapping("/gen-comment")
    public String generateComment() {
        Random random = new Random();
        BaseResponse<Object> response = new BaseResponse<>();
        List<String> postIds = getAllPostIds();
        CreateCommentDTO dto = new CreateCommentDTO();
        for (int i = 0; i < 1000; i++) {
            dto.setUserId(String.valueOf(faker.number().numberBetween(1, 100)));
            dto.setPostId(postIds.get(random.nextInt(postIds.size())));
            dto.setCommentType("PARENT_CMT");
            dto.setContent(faker.lorem().sentence(10));
            commentService.save(dto, response);
        }
        return "done";
    }

    private List<String> generateImages() {
        List<String> images = new ArrayList<>();
        Random random = new Random();
        int numberOfImages = random.nextInt(5) + 1; // Số hình từ 1 -> 5
        for (int i = 0; i < numberOfImages; i++) {
            images.add(photoUrlList.get(random.nextInt(photoUrlList.size())));
        }
        return images;
    }

    public List<String> getAllPostIds() {
        // Tạo một truy vấn để lấy tất cả bài viết
        Query query = new Query();
        query.fields().include("_id");  // Chỉ lấy trường id

        List<Post> posts = mongoTemplate.find(query, Post.class);

        return posts.stream()
                .map(Post::getId) // Trả về id (String)
                .collect(Collectors.toList());
    }
}
