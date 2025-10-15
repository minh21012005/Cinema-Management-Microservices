package com.example.domain.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingReqDTO {

    @NotNull(message = "movieId không được để trống")
    private Long movieId;

    @Min(value = 1, message = "Số sao tối thiểu là 1")
    @Max(value = 5, message = "Số sao tối đa là 5")
    private int stars;

    @Size(max = 1000, message = "Bình luận không được vượt quá 1000 ký tự")
    private String comment;
}
