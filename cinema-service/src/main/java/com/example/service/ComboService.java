package com.example.service;

import com.example.domain.entity.Combo;
import com.example.domain.request.ComboReqDTO;
import com.example.domain.response.ComboResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;

public interface ComboService extends BaseService<Combo, Long, ComboReqDTO, ComboResDTO> {
    ComboResDTO create(ComboReqDTO dto);
    ComboResDTO update(Long id, ComboReqDTO dto) throws IdInvalidException;
    ResultPaginationDTO fetchAllCombos(String name, Pageable pageable);
}
