package com.example.postservice.controller;

import com.example.postservice.component.Common;
import com.example.postservice.entity.PostLike;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interaction")
public class InteractionController {
    @Autowired
    private Common com;

    @Autowired
    private Faker faker;

//    @GetMapping
//    public String generateDataLike() {
//        PostLike postLike = new PostLike();
//        for (int i = 0; i < 1000; i++) {
//            postLike.getId()
//        }
//    }
}
