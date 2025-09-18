package com.example.controller;

import com.example.mapper.BaseMapper;
import com.example.service.BaseService;
import com.example.util.error.IdInvalidException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class BaseController<T, ID, Req, Res> {

    protected final BaseService<T, ID, Req, Res> service;
    protected final BaseMapper<T, Req, Res> mapper;

    protected BaseController(BaseService<T, ID, Req, Res> service, BaseMapper<T, Req, Res> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        List<T> list = service.findAll();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) throws IdInvalidException {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IdInvalidException("Id không tồn tại trong hệ thống!"));
    }

    @PostMapping
    public ResponseEntity<Res> create(@Valid @RequestBody Req dto) throws IdInvalidException {
        T entity = mapper.toEntity(dto);
        Res res = mapper.toDto(service.save(entity));
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Res> update(@PathVariable ID id, @Valid @RequestBody Req dto) throws IdInvalidException {
        if (!service.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại trong hệ thống!");
        }
        T entity = mapper.toEntity(dto);
        Res res = mapper.toDto(service.save(entity));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) throws IdInvalidException {
        if (!service.existsById(id)) {
            throw new IdInvalidException("Id không tồn tại trong hệ thống!");
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

