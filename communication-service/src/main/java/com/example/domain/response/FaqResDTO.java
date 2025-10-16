package com.example.domain.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqResDTO {
    private Long id;
    private String question;
    private String answer;
    private String intent;
    private String tags;
}
