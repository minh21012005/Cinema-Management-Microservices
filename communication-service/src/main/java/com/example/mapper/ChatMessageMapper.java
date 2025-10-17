package com.example.mapper;

import com.example.domain.entity.ChatMessage;
import com.example.domain.request.ChatMessageReqDTO;
import com.example.domain.response.ChatMessageResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper extends BaseMapper<ChatMessage, ChatMessageReqDTO, ChatMessageResDTO> {

    @Override
    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "session.sessionId", target = "sessionId")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "dd/MM/yyyy HH:mm")
    ChatMessageResDTO toDto(ChatMessage entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "session", ignore = true) // gán trong service
    @Mapping(target = "sender", ignore = true)  // backend sẽ tự set
    @Mapping(target = "type", ignore = true)    // backend sẽ tự set
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ChatMessage toEntity(ChatMessageReqDTO dto);
}
