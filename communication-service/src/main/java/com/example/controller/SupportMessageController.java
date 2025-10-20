package com.example.controller;

import com.example.domain.entity.SupportMessage;
import com.example.domain.request.SupportMessageReqDTO;
import com.example.domain.response.SupportMessageResDTO;
import com.example.mapper.SupportMessageMapper;
import com.example.service.SupportMessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/support-chat-messages")
public class SupportMessageController extends
        BaseController<SupportMessage, Long, SupportMessageReqDTO, SupportMessageResDTO> {

    private final SupportMessageService supportMessageService;

    protected SupportMessageController(
            SupportMessageService supportMessageService,
            SupportMessageMapper supportMessageMapper) {
        super(supportMessageService, supportMessageMapper);
        this.supportMessageService = supportMessageService;
    }
}
