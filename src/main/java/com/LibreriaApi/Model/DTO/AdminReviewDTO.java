package com.LibreriaApi.Model.DTO;

public class AdminReviewDTO {

        private Long idReview;

        private Long bookId;

        private String bookTitle;

        private String username;

        private int rating;

        private String content;

        private Boolean status;

    public AdminReviewDTO() {
    }

    public AdminReviewDTO(Long idReview, Long bookId, String bookTitle, String username, int rating, String content, Boolean status) {
        this.idReview = idReview;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.username = username;
        this.rating = rating;
        this.content = content;
        this.status = status;
    }

    public Long getIdReview() {
        return idReview;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getContent() {
        return content;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
