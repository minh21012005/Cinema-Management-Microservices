package com.example.controller;

import com.example.domain.entity.FAQ;
import com.example.domain.request.FaqReqDTO;
import com.example.domain.response.FaqResDTO;
import com.example.mapper.FaqMapper;
import com.example.service.FaqService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/faqs")
public class FaqController extends BaseController<FAQ, Long, FaqReqDTO, FaqResDTO> {

    private final FaqService faqService;

    protected FaqController(FaqService faqService, FaqMapper faqMapper) {
        super(faqService, faqMapper);
        this.faqService = faqService;
    }
}
