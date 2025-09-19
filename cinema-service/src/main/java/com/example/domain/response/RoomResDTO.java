package com.example.domain.response;

import lombok.*;

@Data
public class RoomResDTO {
    private long id;
    private String name;
    private Type type;
    private boolean active;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Type{
        private long id;
        private String name;
    }
}
