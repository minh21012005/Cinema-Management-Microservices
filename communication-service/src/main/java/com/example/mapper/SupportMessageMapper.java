package com.example.mapper;

import com.example.domain.entity.SupportMessage;
import com.example.domain.request.SupportMessageReqDTO;
import com.example.domain.response.SupportMessageResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SupportMessageMapper extends BaseMapper<SupportMessage, SupportMessageReqDTO, SupportMessageResDTO> {

    @Override
    @Mapping(source = "session.sessionId", target = "sessionId")
    @Mapping(source = "sender", target = "sender")
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "dd/MM/yyyy HH:mm")
    @Mapping(source = "readAt", target = "readAt", dateFormat = "dd/MM/yyyy HH:mm")
    SupportMessageResDTO toDto(SupportMessage entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "session", ignore = true) // gán trong service
    @Mapping(target = "sender", ignore = true)  // backend set USER hoặc AGENT
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SupportMessage toEntity(SupportMessageReqDTO dto);
}
