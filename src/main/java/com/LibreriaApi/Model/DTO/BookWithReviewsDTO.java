package com.LibreriaApi.Model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookWithReviewsDTO extends BookDTO{

    private String urlImage;

    private List<ReviewDTO> reviewsDTO;
}
