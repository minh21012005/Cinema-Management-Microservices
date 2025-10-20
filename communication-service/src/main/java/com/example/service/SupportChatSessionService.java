package com.example.service;

import com.example.domain.entity.SupportChatSession;
import com.example.domain.request.SupportChatAssignReqDTO;
import com.example.domain.request.SupportChatSessionReqDTO;
import com.example.domain.response.SupportChatSessionResDTO;
import com.example.util.error.IdInvalidException;

public interface SupportChatSessionService extends
        BaseService<SupportChatSession, Long, SupportChatSessionReqDTO, SupportChatSessionResDTO> {

    SupportChatSessionResDTO assignAgent(String sessionId, SupportChatAssignReqDTO dto) throws IdInvalidException;

    SupportChatSessionResDTO closeSession(String sessionId) throws IdInvalidException;

    SupportChatSessionResDTO findBySessionId(String sessionId) throws IdInvalidException;

    SupportChatSessionResDTO createSession(SupportChatSessionReqDTO dto);
}
