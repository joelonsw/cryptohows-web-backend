package xyz.cryptohows.backend.vc.domain.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import xyz.cryptohows.backend.project.domain.Category;
import xyz.cryptohows.backend.project.domain.Mainnet;
import xyz.cryptohows.backend.project.domain.Project;
import xyz.cryptohows.backend.project.domain.repository.ProjectRepository;
import xyz.cryptohows.backend.vc.domain.Partnership;
import xyz.cryptohows.backend.vc.domain.VentureCapital;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class VentureCapitalRepositoryTest {

    @Autowired
    private VentureCapitalRepository ventureCapitalRepository;

    @Autowired
    private PartnershipRepository partnershipRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TestEntityManager tem;

    private final VentureCapital hashed = VentureCapital.builder()
            .name("해시드")
            .about("한국의 VC")
            .homepage("hashed.com")
            .logo("hashed.png")
            .build();

    private final VentureCapital a16z = VentureCapital.builder()
            .name("a16z")
            .about("미국의 VC")
            .homepage("a16z.com")
            .logo("a16z.png")
            .build();

    private final Project EOS = Project.builder()
            .name("EOS")
            .about("EOS 프로젝트")
            .homepage("https://EOS.io/")
            .category(Category.INFRASTRUCTURE)
            .mainnet(Mainnet.EOS)
            .build();

    private final Project axieInfinity = Project.builder()
            .name("axieInfinity")
            .about("엑시 인피니티")
            .homepage("https://axieInfinity.xyz/")
            .category(Category.WEB3)
            .mainnet(Mainnet.ETHEREUM)
            .build();

    @BeforeEach
    void setUp() {
        projectRepository.save(EOS);
        projectRepository.save(axieInfinity);
        ventureCapitalRepository.save(hashed);
        ventureCapitalRepository.save(a16z);
        tem.flush();
        tem.clear();
    }


    @Test
    @DisplayName("VentureCapital이 없어지면, 해당 회사에서 투자한 Partnership 내역은 사라진다.")
    void deleteVentureCapital() {
        // given
        Partnership hashedEOS = new Partnership(hashed, EOS);
        Partnership hashedAxieInfinity = new Partnership(hashed, axieInfinity);
        partnershipRepository.saveAll(Arrays.asList(hashedEOS, hashedAxieInfinity));
        tem.flush();
        tem.clear();

        // when
        ventureCapitalRepository.deleteById(hashed.getId());

        // then
        List<Partnership> partnerships = partnershipRepository.findAll();
        assertThat(partnerships).isEmpty();
    }

    @Test
    @DisplayName("VentureCapital의 이름 리스트로 조회할 수 있다.")
    void findAllName() {
        // when
        List<VentureCapital> vcs = ventureCapitalRepository.findAllByNameInIgnoreCase(Arrays.asList("해시드", "a16z", "flower"));

        // then
        assertThat(vcs).hasSize(2);
        assertThat(vcs).containsExactly(hashed, a16z);
    }


    @Test
    @DisplayName("VentureCapital의 이름 리스트로 조회할 수 있으며, 대소문자는 상관이 없다.")
    void findAllNameIgnoreCase() {
        // when
        List<VentureCapital> vcs = ventureCapitalRepository.findAllByNameInIgnoreCase(Arrays.asList("해시드", "A16Z", "flower"));

        // then
        assertThat(vcs).hasSize(2);
        assertThat(vcs).containsExactly(hashed, a16z);
    }
}
