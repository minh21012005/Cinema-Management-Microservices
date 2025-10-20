package com.example.mapper;

import com.example.domain.entity.SupportChatSession;
import com.example.domain.request.SupportChatSessionReqDTO;
import com.example.domain.response.SupportChatSessionResDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SupportChatSessionMapper extends BaseMapper<SupportChatSession, SupportChatSessionReqDTO, SupportChatSessionResDTO> {

    @Override
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "dd/MM/yyyy HH:mm")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "dd/MM/yyyy HH:mm")
    SupportChatSessionResDTO toDto(SupportChatSession entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SupportChatSession toEntity(SupportChatSessionReqDTO dto);
}
