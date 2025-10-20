package com.example.service;

import com.example.domain.entity.SupportMessage;
import com.example.domain.request.SupportMessageReqDTO;
import com.example.domain.response.SupportMessageResDTO;

public interface SupportMessageService extends
        BaseService<SupportMessage, Long, SupportMessageReqDTO, SupportMessageResDTO> {
}
