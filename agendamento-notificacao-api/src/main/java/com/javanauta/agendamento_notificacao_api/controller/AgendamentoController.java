package com.javanauta.agendamento_notificacao_api.controller;

import com.javanauta.agendamento_notificacao_api.Business.AgendamentoService;
import com.javanauta.agendamento_notificacao_api.controller.dto.in.AgendamentoRecord;
import com.javanauta.agendamento_notificacao_api.controller.dto.out.AgendamentoRecordOut;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoRecordOut> gravarAgendamentos(@RequestBody AgendamentoRecord agendamentoRecord){
        return ResponseEntity.ok(agendamentoService.gravarAgendamento(agendamentoRecord));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoRecordOut> buscarAgendamentoPorId(@PathVariable("id") Long id){
        return ResponseEntity.ok(agendamentoService.buscarAgendamentosPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable("id") Long id){
        agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.accepted().build();
    }

}