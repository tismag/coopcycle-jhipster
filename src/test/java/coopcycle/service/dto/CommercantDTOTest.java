package coopcycle.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import coopcycle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommercantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommercantDTO.class);
        CommercantDTO commercantDTO1 = new CommercantDTO();
        commercantDTO1.setId(1L);
        CommercantDTO commercantDTO2 = new CommercantDTO();
        assertThat(commercantDTO1).isNotEqualTo(commercantDTO2);
        commercantDTO2.setId(commercantDTO1.getId());
        assertThat(commercantDTO1).isEqualTo(commercantDTO2);
        commercantDTO2.setId(2L);
        assertThat(commercantDTO1).isNotEqualTo(commercantDTO2);
        commercantDTO1.setId(null);
        assertThat(commercantDTO1).isNotEqualTo(commercantDTO2);
    }
}
