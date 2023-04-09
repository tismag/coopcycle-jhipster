package coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import coopcycle.IntegrationTest;
import coopcycle.domain.Commercant;
import coopcycle.repository.CommercantRepository;
import coopcycle.repository.EntityManager;
import coopcycle.service.CommercantService;
import coopcycle.service.dto.CommercantDTO;
import coopcycle.service.mapper.CommercantMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link CommercantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CommercantResourceIT {

    private static final Long DEFAULT_ID_COMMERCANT = 1L;
    private static final Long UPDATED_ID_COMMERCANT = 2L;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_TEL = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_ETABLISSEMENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ETABLISSEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/commercants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CommercantRepository commercantRepository;

    @Mock
    private CommercantRepository commercantRepositoryMock;

    @Autowired
    private CommercantMapper commercantMapper;

    @Mock
    private CommercantService commercantServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Commercant commercant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commercant createEntity(EntityManager em) {
        Commercant commercant = new Commercant()
            .idCommercant(DEFAULT_ID_COMMERCANT)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .numeroTel(DEFAULT_NUMERO_TEL)
            .nomEtablissement(DEFAULT_NOM_ETABLISSEMENT)
            .adresse(DEFAULT_ADRESSE);
        return commercant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Commercant createUpdatedEntity(EntityManager em) {
        Commercant commercant = new Commercant()
            .idCommercant(UPDATED_ID_COMMERCANT)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .numeroTel(UPDATED_NUMERO_TEL)
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .adresse(UPDATED_ADRESSE);
        return commercant;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll("rel_commercant__id_commercant").block();
            em.deleteAll(Commercant.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        commercant = createEntity(em);
    }

    @Test
    void createCommercant() throws Exception {
        int databaseSizeBeforeCreate = commercantRepository.findAll().collectList().block().size();
        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commercantDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeCreate + 1);
        Commercant testCommercant = commercantList.get(commercantList.size() - 1);
        assertThat(testCommercant.getIdCommercant()).isEqualTo(DEFAULT_ID_COMMERCANT);
        assertThat(testCommercant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCommercant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testCommercant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCommercant.getNumeroTel()).isEqualTo(DEFAULT_NUMERO_TEL);
        assertThat(testCommercant.getNomEtablissement()).isEqualTo(DEFAULT_NOM_ETABLISSEMENT);
        assertThat(testCommercant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    void createCommercantWithExistingId() throws Exception {
        // Create the Commercant with an existing ID
        commercant.setId(1L);
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        int databaseSizeBeforeCreate = commercantRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commercantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCommercantsAsStream() {
        // Initialize the database
        commercantRepository.save(commercant).block();

        List<Commercant> commercantList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CommercantDTO.class)
            .getResponseBody()
            .map(commercantMapper::toEntity)
            .filter(commercant::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(commercantList).isNotNull();
        assertThat(commercantList).hasSize(1);
        Commercant testCommercant = commercantList.get(0);
        assertThat(testCommercant.getIdCommercant()).isEqualTo(DEFAULT_ID_COMMERCANT);
        assertThat(testCommercant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCommercant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testCommercant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCommercant.getNumeroTel()).isEqualTo(DEFAULT_NUMERO_TEL);
        assertThat(testCommercant.getNomEtablissement()).isEqualTo(DEFAULT_NOM_ETABLISSEMENT);
        assertThat(testCommercant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    void getAllCommercants() {
        // Initialize the database
        commercantRepository.save(commercant).block();

        // Get all the commercantList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(commercant.getId().intValue()))
            .jsonPath("$.[*].idCommercant")
            .value(hasItem(DEFAULT_ID_COMMERCANT.intValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].prenom")
            .value(hasItem(DEFAULT_PRENOM))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].numeroTel")
            .value(hasItem(DEFAULT_NUMERO_TEL))
            .jsonPath("$.[*].nomEtablissement")
            .value(hasItem(DEFAULT_NOM_ETABLISSEMENT))
            .jsonPath("$.[*].adresse")
            .value(hasItem(DEFAULT_ADRESSE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommercantsWithEagerRelationshipsIsEnabled() {
        when(commercantServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(commercantServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCommercantsWithEagerRelationshipsIsNotEnabled() {
        when(commercantServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(commercantRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getCommercant() {
        // Initialize the database
        commercantRepository.save(commercant).block();

        // Get the commercant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, commercant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(commercant.getId().intValue()))
            .jsonPath("$.idCommercant")
            .value(is(DEFAULT_ID_COMMERCANT.intValue()))
            .jsonPath("$.nom")
            .value(is(DEFAULT_NOM))
            .jsonPath("$.prenom")
            .value(is(DEFAULT_PRENOM))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.numeroTel")
            .value(is(DEFAULT_NUMERO_TEL))
            .jsonPath("$.nomEtablissement")
            .value(is(DEFAULT_NOM_ETABLISSEMENT))
            .jsonPath("$.adresse")
            .value(is(DEFAULT_ADRESSE));
    }

    @Test
    void getNonExistingCommercant() {
        // Get the commercant
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCommercant() throws Exception {
        // Initialize the database
        commercantRepository.save(commercant).block();

        int databaseSizeBeforeUpdate = commercantRepository.findAll().collectList().block().size();

        // Update the commercant
        Commercant updatedCommercant = commercantRepository.findById(commercant.getId()).block();
        updatedCommercant
            .idCommercant(UPDATED_ID_COMMERCANT)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .numeroTel(UPDATED_NUMERO_TEL)
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .adresse(UPDATED_ADRESSE);
        CommercantDTO commercantDTO = commercantMapper.toDto(updatedCommercant);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commercantDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commercantDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
        Commercant testCommercant = commercantList.get(commercantList.size() - 1);
        assertThat(testCommercant.getIdCommercant()).isEqualTo(UPDATED_ID_COMMERCANT);
        assertThat(testCommercant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCommercant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testCommercant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCommercant.getNumeroTel()).isEqualTo(UPDATED_NUMERO_TEL);
        assertThat(testCommercant.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testCommercant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    void putNonExistingCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().collectList().block().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, commercantDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commercantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().collectList().block().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commercantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().collectList().block().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(commercantDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCommercantWithPatch() throws Exception {
        // Initialize the database
        commercantRepository.save(commercant).block();

        int databaseSizeBeforeUpdate = commercantRepository.findAll().collectList().block().size();

        // Update the commercant using partial update
        Commercant partialUpdatedCommercant = new Commercant();
        partialUpdatedCommercant.setId(commercant.getId());

        partialUpdatedCommercant
            .idCommercant(UPDATED_ID_COMMERCANT)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommercant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommercant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
        Commercant testCommercant = commercantList.get(commercantList.size() - 1);
        assertThat(testCommercant.getIdCommercant()).isEqualTo(UPDATED_ID_COMMERCANT);
        assertThat(testCommercant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCommercant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testCommercant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCommercant.getNumeroTel()).isEqualTo(DEFAULT_NUMERO_TEL);
        assertThat(testCommercant.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testCommercant.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
    }

    @Test
    void fullUpdateCommercantWithPatch() throws Exception {
        // Initialize the database
        commercantRepository.save(commercant).block();

        int databaseSizeBeforeUpdate = commercantRepository.findAll().collectList().block().size();

        // Update the commercant using partial update
        Commercant partialUpdatedCommercant = new Commercant();
        partialUpdatedCommercant.setId(commercant.getId());

        partialUpdatedCommercant
            .idCommercant(UPDATED_ID_COMMERCANT)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .numeroTel(UPDATED_NUMERO_TEL)
            .nomEtablissement(UPDATED_NOM_ETABLISSEMENT)
            .adresse(UPDATED_ADRESSE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCommercant.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCommercant))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
        Commercant testCommercant = commercantList.get(commercantList.size() - 1);
        assertThat(testCommercant.getIdCommercant()).isEqualTo(UPDATED_ID_COMMERCANT);
        assertThat(testCommercant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCommercant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testCommercant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCommercant.getNumeroTel()).isEqualTo(UPDATED_NUMERO_TEL);
        assertThat(testCommercant.getNomEtablissement()).isEqualTo(UPDATED_NOM_ETABLISSEMENT);
        assertThat(testCommercant.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    void patchNonExistingCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().collectList().block().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, commercantDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commercantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().collectList().block().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commercantDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCommercant() throws Exception {
        int databaseSizeBeforeUpdate = commercantRepository.findAll().collectList().block().size();
        commercant.setId(count.incrementAndGet());

        // Create the Commercant
        CommercantDTO commercantDTO = commercantMapper.toDto(commercant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(commercantDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Commercant in the database
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCommercant() {
        // Initialize the database
        commercantRepository.save(commercant).block();

        int databaseSizeBeforeDelete = commercantRepository.findAll().collectList().block().size();

        // Delete the commercant
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, commercant.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Commercant> commercantList = commercantRepository.findAll().collectList().block();
        assertThat(commercantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
