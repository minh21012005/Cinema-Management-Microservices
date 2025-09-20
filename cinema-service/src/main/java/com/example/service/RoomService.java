package com.example.service;

import com.example.domain.entity.Room;
import com.example.domain.entity.RoomType;
import com.example.domain.request.RoomReqCreateDTO;
import com.example.domain.request.RoomReqDTO;
import com.example.domain.response.RoomResDTO;
import com.example.util.error.IdInvalidException;

import java.util.List;

public interface RoomService extends BaseService<Room, Long, RoomReqDTO, RoomResDTO> {
    List<RoomResDTO> fetchAllRoom(Long cinemaId);
    List<RoomType> fetchAllRoomType();
    RoomResDTO createRoom(RoomReqCreateDTO dto) throws IdInvalidException;
    RoomResDTO updateRoom(Long id, RoomReqDTO dto) throws IdInvalidException;
    RoomResDTO changeStatus(Long id) throws IdInvalidException;
}
