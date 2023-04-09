package coopcycle.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LivraisonMapperTest {

    private LivraisonMapper livraisonMapper;

    @BeforeEach
    public void setUp() {
        livraisonMapper = new LivraisonMapperImpl();
    }
}
