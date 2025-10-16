package com.example.service.impl;

import com.example.domain.entity.FAQ;
import com.example.domain.request.FaqReqDTO;
import com.example.domain.response.FaqResDTO;
import com.example.mapper.FaqMapper;
import com.example.repository.FaqRepository;
import com.example.service.FaqService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FaqServiceImpl
        extends BaseServiceImpl<FAQ, Long, FaqReqDTO, FaqResDTO>
        implements FaqService {

    private final FaqRepository faqRepository;
    private final FaqMapper faqMapper;

    protected FaqServiceImpl(FaqRepository faqRepository, FaqMapper faqMapper) {
        super(faqRepository);
        this.faqRepository = faqRepository;
        this.faqMapper = faqMapper;
    }

    @Override
    public List<FaqResDTO> findAllActive() {
        return faqRepository.findByActiveTrue().stream()
                .map(faqMapper::toDto)
                .toList();
    }
}
