package xyz.cryptohows.backend.vc.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.cryptohows.backend.project.domain.Projects;
import xyz.cryptohows.backend.vc.domain.VentureCapital;
import xyz.cryptohows.backend.vc.domain.repository.VentureCapitalRepository;
import xyz.cryptohows.backend.vc.ui.dto.VentureCapitalResponse;
import xyz.cryptohows.backend.vc.ui.dto.VentureCapitalSimpleResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VentureCapitalService {

    private final VentureCapitalRepository ventureCapitalRepository;

    public List<VentureCapitalSimpleResponse> findAllVentureCapitals() {
        List<VentureCapital> ventureCapitals = ventureCapitalRepository.findAll();
        return VentureCapitalSimpleResponse.toList(ventureCapitals);
    }

    public VentureCapitalResponse findVentureCapitalByName(String ventureCapitalName) {
        VentureCapital ventureCapital = ventureCapitalRepository.findByName(ventureCapitalName)
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 벤처캐피탈은 없습니다."));
        Projects portfolio = new Projects(ventureCapital.getPortfolio());
        return VentureCapitalResponse.of(ventureCapital, portfolio.sortProjectsByCategory());
    }
}
