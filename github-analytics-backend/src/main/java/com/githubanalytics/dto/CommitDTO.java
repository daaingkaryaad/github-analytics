package com.githubanalytics.dto;

import lombok.Data;

@Data
public class CommitDTO {

    private String sha;
    private Commit commit;

    @Data
    public static class Commit {
        private Author author;
        private String message;
    }

    @Data
    public static class Author {
        private String name;
        private String date;
    }
}