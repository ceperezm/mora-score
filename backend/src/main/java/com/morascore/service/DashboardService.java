package com.morascore.service;
import com.morascore.controller.DashboardController.DashboardStatsResponse;
import com.morascore.controller.DashboardController.UltimaDecisionResponse;
import com.morascore.controller.DashboardController.UltimaEvaluacionResponse;
import com.morascore.repository.ClienteRepository;
import com.morascore.repository.DecisionRepository;
import com.morascore.repository.EvaluacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ClienteRepository clienteRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final DecisionRepository decisionRepository;
    @Transactional(readOnly = true)
    public DashboardStatsResponse getStats() {
        return new DashboardStatsResponse(
                clienteRepository.countByActivo(true),
                evaluacionRepository.count(),
                evaluacionRepository.countClientesConMora(),
                decisionRepository.count(),
                decisionRepository.countByDecisionIgnoreCase("APROBADO")
        );
    }
    @Transactional(readOnly = true)
    public List<UltimaEvaluacionResponse> getUltimasEvaluaciones() {
        return evaluacionRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(ev -> new UltimaEvaluacionResponse(
                        ev.getCliente().getNombre() + " " + ev.getCliente().getApellido(),
                        ev.getPrediccion(),
                        ev.getProbabilidad().multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).intValue(),
                        ev.getCategoriaRiesgo(),
                        ev.getVersionModelo(),
                        ev.getCreatedAt()
                )).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<UltimaDecisionResponse> getUltimasDecisiones() {
        return decisionRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(d -> new UltimaDecisionResponse(
                        d.getEvaluacion().getCliente().getNombre() + " " + d.getEvaluacion().getCliente().getApellido(),
                        d.getDecision(),
                        d.getComentario(),
                        d.getCreatedAt()
                )).collect(Collectors.toList());
    }
}
