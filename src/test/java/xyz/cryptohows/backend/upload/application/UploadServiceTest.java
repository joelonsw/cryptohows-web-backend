package xyz.cryptohows.backend.upload.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import xyz.cryptohows.backend.project.domain.Project;
import xyz.cryptohows.backend.project.domain.repository.ProjectRepository;
import xyz.cryptohows.backend.round.domain.repository.RoundParticipationRepository;
import xyz.cryptohows.backend.round.domain.repository.RoundRepository;
import xyz.cryptohows.backend.vc.domain.Partnership;
import xyz.cryptohows.backend.vc.domain.VentureCapital;
import xyz.cryptohows.backend.vc.domain.repository.PartnershipRepository;
import xyz.cryptohows.backend.vc.domain.repository.VentureCapitalRepository;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UploadServiceTest {

    @Autowired
    private UploadService uploadService;

    @Autowired
    private VentureCapitalRepository ventureCapitalRepository;

    @Autowired
    private PartnershipRepository partnershipRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private RoundParticipationRepository roundParticipationRepository;

    @DisplayName("벤처 캐피탈 정보를 담은 엑셀 파일을 업로드 할 수 있다.")
    @Test
    void uploadVentureCapitals() {
        // given
        MultipartFile ventureCapitalsFile = ExcelFileFixture.getVentureCapitalsFile();

        // when
        uploadService.uploadVentureCapitals(ventureCapitalsFile);

        // then
        List<VentureCapital> ventureCapitals = ventureCapitalRepository.findAll();
        assertThat(ventureCapitals).hasSize(3);
        assertThat(ventureCapitals.get(0).getName()).isEqualTo("Crypto.com Capital");
        assertThat(ventureCapitals.get(1).getName()).isEqualTo("Spartan Group");
        assertThat(ventureCapitals.get(2).getName()).isEqualTo("GuildFi");
    }

    @DisplayName("프로젝트 정보를 담은 엑셀 파일을 업로드 할 수 있다.")
    @Test
    void uploadProjects() {
        // given
        uploadVentureCapitals();
        MultipartFile projectsFile = ExcelFileFixture.getProjects();

        // when
        uploadService.uploadProjects(projectsFile);

        // then
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(3);
        assertThat(projects.get(0).getName()).isEqualTo("Pendle Finance");
        assertThat(projects.get(1).getName()).isEqualTo("Heroes of Mavia");
        assertThat(projects.get(2).getName()).isEqualTo("Cyball");

        List<Partnership> partnerships = partnershipRepository.findAll();
        assertThat(partnerships).hasSize(5);
        assertThat(partnerships.get(0).getProject().getName()).isEqualTo("Pendle Finance");
        assertThat(partnerships.get(0).getVentureCapital().getName()).isEqualTo("Crypto.com Capital");
        assertThat(partnerships.get(1).getProject().getName()).isEqualTo("Pendle Finance");
        assertThat(partnerships.get(1).getVentureCapital().getName()).isEqualTo("Spartan Group");

        assertThat(partnerships.get(2).getProject().getName()).isEqualTo("Heroes of Mavia");
        assertThat(partnerships.get(2).getVentureCapital().getName()).isEqualTo("Crypto.com Capital");
        assertThat(partnerships.get(3).getProject().getName()).isEqualTo("Heroes of Mavia");
        assertThat(partnerships.get(3).getVentureCapital().getName()).isEqualTo("GuildFi");

        assertThat(partnerships.get(4).getProject().getName()).isEqualTo("Cyball");
        assertThat(partnerships.get(4).getVentureCapital().getName()).isEqualTo("GuildFi");
    }

    @DisplayName("라운드 정보를 담은 엑셀 파일을 업로드 할 수 있다.")
    @Test
    void uploadRounds() {
    }
}
