package com.rdcl.transactionsApi.clientApi;


import com.rdcl.transactionsApi.dto.ClienteDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;


@Component
@Slf4j
public class UserApiClient {


    private final RestClient restClient;

    public UserApiClient(@Value("${userapi.url}") String userApiUrl) {
        Assert.hasText(userApiUrl, "userApiUrl no puede ser nulo o vacío");
        this.restClient = RestClient.create(userApiUrl);
    }


    public ClienteDTO getClienteById(Integer clienteId) {
        try {
            return restClient.get()
                    .uri("/api/cliente/{clienteId}", clienteId)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(),
                            (request, response) -> {
                                throw new RuntimeException("Error en la llamada al API: " + response.getStatusCode());
                            })
                    .body(ClienteDTO.class);
        } catch (Exception e) {
            log.error("Error al obtener cliente por ID: {}", clienteId, e);
            return null;
        }
    }

    public boolean existsByClienteId(Integer clienteId) {
        if (clienteId == null) return false;

        try {
            return Boolean.TRUE.equals(
                    restClient.get()
                            .uri("/api/cliente/{clienteId}", clienteId)
                            .retrieve()
                            .body(Boolean.class)
            );
        } catch (Exception e) {
            log.error("Error al verificar existencia de cliente: {}", clienteId, e);
            return false;
        }
    }
}