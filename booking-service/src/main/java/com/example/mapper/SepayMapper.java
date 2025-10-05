package com.example.mapper;

import com.example.domain.entity.SePayTransaction;
import com.example.domain.request.SepayWebhookReqDTO;
import com.example.domain.response.SepayWebhookResDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface SepayMapper extends BaseMapper<SePayTransaction, SepayWebhookReqDTO, SepayWebhookResDTO> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Mapping(target = "sepayId", source = "id")
    @Mapping(target = "transactionDate", source = "transactionDate", qualifiedByName = "toLocalDateTime")
    @Mapping(target = "transferAmount", source = "transferAmount", qualifiedByName = "mapLongToDouble")
    @Mapping(target = "accumulated", source = "accumulated", qualifiedByName = "mapLongToDouble")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "rawData", expression = "java(toRawJson(dto))")
    SePayTransaction toEntity(SepayWebhookReqDTO dto);

    default String toRawJson(SepayWebhookReqDTO dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            return "{}"; // fallback nếu lỗi
        }
    }

    @Named("toLocalDateTime")
    default LocalDateTime toLocalDateTime(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        // format theo SePay gửi: "yyyy-MM-dd HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateStr, formatter);
    }

    @Named("mapLongToDouble")
    default Double mapLongToDouble(Long val) {
        return val != null ? val.doubleValue() : null;
    }
}
