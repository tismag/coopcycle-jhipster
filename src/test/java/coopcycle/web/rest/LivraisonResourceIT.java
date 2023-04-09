package coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import coopcycle.IntegrationTest;
import coopcycle.domain.Livraison;
import coopcycle.repository.EntityManager;
import coopcycle.repository.LivraisonRepository;
import coopcycle.service.dto.LivraisonDTO;
import coopcycle.service.mapper.LivraisonMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link LivraisonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LivraisonResourceIT {

    private static final Long DEFAULT_ID_LIVRAISON = 1L;
    private static final Long UPDATED_ID_LIVRAISON = 2L;

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final Long DEFAULT_QUANTITE = 1L;
    private static final Long UPDATED_QUANTITE = 2L;

    private static final String ENTITY_API_URL = "/api/livraisons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivraisonRepository livraisonRepository;

    @Autowired
    private LivraisonMapper livraisonMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Livraison livraison;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livraison createEntity(EntityManager em) {
        Livraison livraison = new Livraison().idLivraison(DEFAULT_ID_LIVRAISON).contenu(DEFAULT_CONTENU).quantite(DEFAULT_QUANTITE);
        return livraison;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livraison createUpdatedEntity(EntityManager em) {
        Livraison livraison = new Livraison().idLivraison(UPDATED_ID_LIVRAISON).contenu(UPDATED_CONTENU).quantite(UPDATED_QUANTITE);
        return livraison;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Livraison.class).block();
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
        livraison = createEntity(em);
    }

    @Test
    void createLivraison() throws Exception {
        int databaseSizeBeforeCreate = livraisonRepository.findAll().collectList().block().size();
        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeCreate + 1);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getIdLivraison()).isEqualTo(DEFAULT_ID_LIVRAISON);
        assertThat(testLivraison.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testLivraison.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
    }

    @Test
    void createLivraisonWithExistingId() throws Exception {
        // Create the Livraison with an existing ID
        livraison.setId(1L);
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        int databaseSizeBeforeCreate = livraisonRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllLivraisonsAsStream() {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        List<Livraison> livraisonList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(LivraisonDTO.class)
            .getResponseBody()
            .map(livraisonMapper::toEntity)
            .filter(livraison::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(livraisonList).isNotNull();
        assertThat(livraisonList).hasSize(1);
        Livraison testLivraison = livraisonList.get(0);
        assertThat(testLivraison.getIdLivraison()).isEqualTo(DEFAULT_ID_LIVRAISON);
        assertThat(testLivraison.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testLivraison.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
    }

    @Test
    void getAllLivraisons() {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        // Get all the livraisonList
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
            .value(hasItem(livraison.getId().intValue()))
            .jsonPath("$.[*].idLivraison")
            .value(hasItem(DEFAULT_ID_LIVRAISON.intValue()))
            .jsonPath("$.[*].contenu")
            .value(hasItem(DEFAULT_CONTENU))
            .jsonPath("$.[*].quantite")
            .value(hasItem(DEFAULT_QUANTITE.intValue()));
    }

    @Test
    void getLivraison() {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        // Get the livraison
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, livraison.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(livraison.getId().intValue()))
            .jsonPath("$.idLivraison")
            .value(is(DEFAULT_ID_LIVRAISON.intValue()))
            .jsonPath("$.contenu")
            .value(is(DEFAULT_CONTENU))
            .jsonPath("$.quantite")
            .value(is(DEFAULT_QUANTITE.intValue()));
    }

    @Test
    void getNonExistingLivraison() {
        // Get the livraison
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLivraison() throws Exception {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();

        // Update the livraison
        Livraison updatedLivraison = livraisonRepository.findById(livraison.getId()).block();
        updatedLivraison.idLivraison(UPDATED_ID_LIVRAISON).contenu(UPDATED_CONTENU).quantite(UPDATED_QUANTITE);
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(updatedLivraison);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, livraisonDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getIdLivraison()).isEqualTo(UPDATED_ID_LIVRAISON);
        assertThat(testLivraison.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testLivraison.getQuantite()).isEqualTo(UPDATED_QUANTITE);
    }

    @Test
    void putNonExistingLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, livraisonDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLivraisonWithPatch() throws Exception {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();

        // Update the livraison using partial update
        Livraison partialUpdatedLivraison = new Livraison();
        partialUpdatedLivraison.setId(livraison.getId());

        partialUpdatedLivraison.quantite(UPDATED_QUANTITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLivraison.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLivraison))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getIdLivraison()).isEqualTo(DEFAULT_ID_LIVRAISON);
        assertThat(testLivraison.getContenu()).isEqualTo(DEFAULT_CONTENU);
        assertThat(testLivraison.getQuantite()).isEqualTo(UPDATED_QUANTITE);
    }

    @Test
    void fullUpdateLivraisonWithPatch() throws Exception {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();

        // Update the livraison using partial update
        Livraison partialUpdatedLivraison = new Livraison();
        partialUpdatedLivraison.setId(livraison.getId());

        partialUpdatedLivraison.idLivraison(UPDATED_ID_LIVRAISON).contenu(UPDATED_CONTENU).quantite(UPDATED_QUANTITE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLivraison.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLivraison))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
        Livraison testLivraison = livraisonList.get(livraisonList.size() - 1);
        assertThat(testLivraison.getIdLivraison()).isEqualTo(UPDATED_ID_LIVRAISON);
        assertThat(testLivraison.getContenu()).isEqualTo(UPDATED_CONTENU);
        assertThat(testLivraison.getQuantite()).isEqualTo(UPDATED_QUANTITE);
    }

    @Test
    void patchNonExistingLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, livraisonDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLivraison() throws Exception {
        int databaseSizeBeforeUpdate = livraisonRepository.findAll().collectList().block().size();
        livraison.setId(count.incrementAndGet());

        // Create the Livraison
        LivraisonDTO livraisonDTO = livraisonMapper.toDto(livraison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livraisonDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Livraison in the database
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLivraison() {
        // Initialize the database
        livraisonRepository.save(livraison).block();

        int databaseSizeBeforeDelete = livraisonRepository.findAll().collectList().block().size();

        // Delete the livraison
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, livraison.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Livraison> livraisonList = livraisonRepository.findAll().collectList().block();
        assertThat(livraisonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
