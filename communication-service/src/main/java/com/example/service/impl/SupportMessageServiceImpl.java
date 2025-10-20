package com.example.service.impl;

import com.example.domain.entity.SupportMessage;
import com.example.domain.request.SupportMessageReqDTO;
import com.example.domain.response.SupportMessageResDTO;
import com.example.mapper.SupportMessageMapper;
import com.example.repository.SupportMessageRepository;
import com.example.service.SupportMessageService;
import org.springframework.stereotype.Service;

@Service
public class SupportMessageServiceImpl
        extends BaseServiceImpl<SupportMessage, Long, SupportMessageReqDTO, SupportMessageResDTO>
        implements SupportMessageService {

    private final SupportMessageRepository supportMessageRepository;
    private final SupportMessageMapper supportMessageMapper;

    protected SupportMessageServiceImpl(SupportMessageRepository supportMessageRepository,
                                        SupportMessageMapper supportMessageMapper) {
        super(supportMessageRepository);
        this.supportMessageRepository = supportMessageRepository;
        this.supportMessageMapper = supportMessageMapper;
    }
}
