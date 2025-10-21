package com.example.service;

import com.example.domain.entity.SupportMessage;
import com.example.domain.request.SupportMessageReadReqDTO;
import com.example.domain.request.SupportMessageReqDTO;
import com.example.domain.response.SupportMessageResDTO;
import com.example.util.error.IdInvalidException;

import java.util.List;

public interface SupportMessageService extends
        BaseService<SupportMessage, Long, SupportMessageReqDTO, SupportMessageResDTO> {

    SupportMessageResDTO sendUserMessage(SupportMessageReqDTO dto) throws IdInvalidException;

    SupportMessageResDTO sendAgentMessage(SupportMessageReqDTO dto) throws IdInvalidException;

    void markAsRead(SupportMessageReadReqDTO dto) throws IdInvalidException;

    List<SupportMessageResDTO> getMessagesBySession(String sessionId) throws IdInvalidException;

    List<SupportMessageResDTO> getMessageHistory();
}
