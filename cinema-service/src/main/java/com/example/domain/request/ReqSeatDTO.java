package com.example.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReqSeatDTO {
    private int row;
    private int col;
    private long typeId;
    private long roomId;
}
