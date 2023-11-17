package com.example.backend_java.Models;

public class Feedback {
    public enum Type {
        POSITIVE,
        NEGATIVE,
        IDEA,
        PRAISE
    }

    private String name;
    private String body;
    private Type feedback;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Type getFeedback() {
        return feedback;
    }

    public void setFeedback(Type feedback) {
        this.feedback = feedback;
    }

    public Feedback(String name, String body, Type feedback) {
        this.name = name;
        this.body = body;
        this.feedback = feedback;
    }
}
