package com.example.mapper;

import com.example.domain.entity.ChatSession;
import com.example.domain.request.ChatSessionReqDTO;
import com.example.domain.response.ChatSessionResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatSessionMapper extends BaseMapper<ChatSession, ChatSessionReqDTO, ChatSessionResDTO> {

    @Override
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "dd/MM/yyyy HH:mm")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "dd/MM/yyyy HH:mm")
    ChatSessionResDTO toDto(ChatSession entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ChatSession toEntity(ChatSessionReqDTO dto);
}
