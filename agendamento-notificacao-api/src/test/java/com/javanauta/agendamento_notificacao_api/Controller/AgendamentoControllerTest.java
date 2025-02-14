package com.javanauta.agendamento_notificacao_api.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.javanauta.agendamento_notificacao_api.Business.AgendamentoService;
import com.javanauta.agendamento_notificacao_api.controller.AgendamentoController;
import com.javanauta.agendamento_notificacao_api.controller.dto.in.AgendamentoRecord;
import com.javanauta.agendamento_notificacao_api.controller.dto.out.AgendamentoRecordOut;
import com.javanauta.agendamento_notificacao_api.infrastructure.enums.StatusNotificacaoEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AgendamentoControllerTest {

    @InjectMocks
    AgendamentoController agendamentoController;

    @Mock
    AgendamentoService service;

    private AgendamentoRecord agendamentoRecord;
    private AgendamentoRecordOut agendamentoRecordOut;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(agendamentoController).build();

        objectMapper.registerModule(new JavaTimeModule());

        agendamentoRecord = new AgendamentoRecord("email@email.com", "419493493",
                "Favor retornar", LocalDateTime.of(2025,01,11,01,11, 1));

        agendamentoRecordOut = new AgendamentoRecordOut(1L, "email@email.com", "55887996578",
                "Favor retornar a loja com urgência", LocalDateTime.of(2025, 1, 2, 11, 1, 1),
                StatusNotificacaoEnum.AGENDADO, null);
    }

    @Test
    void deveCriarAgendamentoComSucesso() throws Exception {
        when(service.gravarAgendamento(agendamentoRecord)).thenReturn(agendamentoRecordOut);

        mockMvc.perform(post("/agendamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(agendamentoRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.emailDestinatario").value("email@email.com"))
                .andExpect(jsonPath("$.telefoneDestinatario").value(agendamentoRecordOut.telefoneDestinatario()))
                .andExpect(jsonPath("$.mensagem").value(agendamentoRecordOut.mensagem()))
                .andExpect(jsonPath("$.dataHoraEnvio").value("02-01-2025 11:01:01"))
                .andExpect(jsonPath("$.statusNotificacao").value("AGENDADO"));

        verify(service, times(1)).gravarAgendamento(agendamentoRecord);
    }
}
