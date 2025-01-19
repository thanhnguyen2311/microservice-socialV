package com.example.postservice.enumm;

public enum PostStatus {
    PUBLIC("PUBLIC"),
    PRIVATE("PRIVATE"),
    FRIENDS("FRIENDS");

    private final String name;

    PostStatus(String name) {
        this.name = name;
    }

    public static PostStatus getPostStatus(String name) {
        for (PostStatus postStatus : PostStatus.values()) {
            if (postStatus.name.equals(name)) {
                return postStatus;
            }
        }
        return null;
    }
}
