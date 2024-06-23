package com.oauth2.authorization.dto.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.oauth2.authorization.jpa.entity.ObsClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObsClientDto {

    private String clientId;

    private String clientSecret;

    public ObsClientDto(ObsClient obsClient) {
        this.clientId = obsClient.getClientId();
        this.clientSecret = obsClient.getClientSecret();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Create {

        @NotBlank(message = "clientId 필수 입력 항목입니다.")
        private String clientId;

        @NotBlank(message = "clientSecret 필수 입력 항목입니다.")
        private String clientSecret;

        private String wsId;


    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {

        private String clientSecret;

//        private String wsSeq;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String id;

    }


}
