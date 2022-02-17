package com.example.maledetta_treest;

public class Post {
    private int delay, status, pversion;
    private String comment, followingAuthor, datetime, authorName, author;

    public Post(int delay, int status, String comment, String followingAuthor, String datetime, String authorName, String author, int pversion) {
        this.delay = delay;
        this.status = status;
        this.comment = comment;
        this.followingAuthor = followingAuthor;
        this.datetime = datetime;
        this.authorName = authorName;
        this.author = author;
        this.pversion = pversion;
    }

    public int getDelay() {
        return delay;
    }

    public int getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public String getFollowingAuthor() {
        return followingAuthor;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthor() {
        return author;
    }

    public int getPversion() {
        return pversion;
    }

    @Override
    public String toString() {
        return "Post{" +
                "delay=" + delay +
                ", status=" + status +
                ", pversion=" + pversion +
                ", comment='" + comment + '\'' +
                ", followingAuthor='" + followingAuthor + '\'' +
                ", datetime='" + datetime + '\'' +
                ", authorName='" + authorName + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
