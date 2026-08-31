package com.morascore.controller;
import com.morascore.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> stats() {
        return ResponseEntity.ok(dashboardService.getStats());
    }
    @GetMapping("/ultimas-evaluaciones")
    public ResponseEntity<List<UltimaEvaluacionResponse>> ultimasEvaluaciones() {
        return ResponseEntity.ok(dashboardService.getUltimasEvaluaciones());
    }
    @GetMapping("/ultimas-decisiones")
    public ResponseEntity<List<UltimaDecisionResponse>> ultimasDecisiones() {
        return ResponseEntity.ok(dashboardService.getUltimasDecisiones());
    }
    public record DashboardStatsResponse(long clientesActivos, long totalEvaluaciones, long clientesConMora, long totalDecisiones, long aprobados) {}
    public record UltimaEvaluacionResponse(String clienteNombre, String prediccion, int probabilidadMora, String nivelRiesgo, String versionModelo, LocalDateTime fecha) {}
    public record UltimaDecisionResponse(String clienteNombre, String decision, String comentario, LocalDateTime fecha) {}
}
