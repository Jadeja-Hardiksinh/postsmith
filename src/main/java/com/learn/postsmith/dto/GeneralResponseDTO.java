package com.learn.postsmith.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class GeneralResponseDTO {
    String status;
    String message;
    String redirectTo;
    @JsonProperty(value = "details")
    Object object;

    public GeneralResponseDTO(String status, String message, String redirectTo) {
        this.status = status;
        this.message = message;
        this.redirectTo = redirectTo;
    }

    public String generateJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);

    }
}
