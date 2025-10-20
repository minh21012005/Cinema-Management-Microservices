package com.example.service;

import com.example.domain.entity.SupportChatSession;
import com.example.domain.request.SupportChatSessionReqDTO;
import com.example.domain.response.SupportChatSessionResDTO;

public interface SupportChatSessionService extends
        BaseService<SupportChatSession, Long, SupportChatSessionReqDTO, SupportChatSessionResDTO> {
}
