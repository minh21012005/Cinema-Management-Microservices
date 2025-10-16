package com.example.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqReqDTO {
    private String question;
    private String answer;
    private String intent;   // vd: "MOVIE_INFO", "BOOK_TICKET"
    private String tags;     // "phim, đặt vé, thời gian chiếu"
}
