package com.example.postservice.enumm;

public enum CommentType {
    PARENT_CMT("PARENT_CMT"),
    CHILD_CMT("CHILD_CMT");
    private final String name;

    CommentType(String name) {
        this.name = name;
    }

    public static CommentType getCommentType(String name) {
        for (CommentType type : CommentType.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
