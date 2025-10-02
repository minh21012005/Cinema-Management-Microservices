package com.example.controller;

import com.example.domain.entity.Combo;
import com.example.domain.request.ComboReqDTO;
import com.example.domain.response.ComboResDTO;
import com.example.domain.response.ResultPaginationDTO;
import com.example.mapper.ComboMapper;
import com.example.service.ComboService;
import com.example.util.annotation.ApiMessage;
import com.example.util.error.IdInvalidException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/combos")
public class ComboController extends BaseController<Combo, Long, ComboReqDTO, ComboResDTO> {

    private final ComboService comboService;

    protected ComboController(ComboService comboService, ComboMapper comboMapper) {
        super(comboService, comboMapper);
        this.comboService = comboService;
    }

    @Override
    @PreAuthorize("hasPermission(null, 'COMBO_CREATE')")
    public ResponseEntity<ComboResDTO> create(ComboReqDTO dto) throws IdInvalidException {
        return ResponseEntity.status(HttpStatus.CREATED).body(comboService.create(dto));
    }

    @Override
    @PreAuthorize("hasPermission(null, 'COMBO_UPDATE')")
    public ResponseEntity<ComboResDTO> update(@PathVariable("id") Long id, ComboReqDTO dto) throws IdInvalidException {
        return ResponseEntity.ok(comboService.update(id, dto));
    }

    @GetMapping("/all")
    @ApiMessage("Fetched all combos")
    @PreAuthorize("hasPermission(null, 'COMBO_VIEW')")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @RequestParam(name = "name", required = false) String name,
            Pageable pageable) {
        return ResponseEntity.ok(comboService.fetchAllCombos(name, pageable));
    }

    @GetMapping("/active")
    @ApiMessage("Fetched all combos active")
    @PreAuthorize("hasPermission(null, 'COMBO_VIEW')")
    public ResponseEntity<List<ComboResDTO>> getAllCombosActive() {
        return ResponseEntity.ok(comboService.fetchAllCombosActive());
    }
}
