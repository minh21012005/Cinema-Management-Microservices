package com.example.service;

import com.example.domain.entity.FAQ;
import com.example.domain.request.FaqReqDTO;
import com.example.domain.response.FaqResDTO;

import java.util.List;

public interface FaqService extends BaseService<FAQ, Long, FaqReqDTO, FaqResDTO> {
    List<FaqResDTO> findAllActive();
}
