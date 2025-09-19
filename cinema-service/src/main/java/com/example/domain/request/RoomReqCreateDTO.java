package com.example.domain.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomReqCreateDTO {
    private String name;       // tên phòng
    private Long cinemaId;     // id rạp
    private Long roomTypeId;   // id loại phòng
    private List<SeatDTO> seats; // danh sách ghế

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SeatDTO {
        private Integer row;   // chỉ số hàng
        private Integer col;   // chỉ số cột
        private Long type;     // seatTypeId
        private String name;   // tên ghế (A1, B2, D1-2...)
    }
}
