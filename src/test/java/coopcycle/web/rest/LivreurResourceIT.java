package coopcycle.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import coopcycle.IntegrationTest;
import coopcycle.domain.Livreur;
import coopcycle.repository.EntityManager;
import coopcycle.repository.LivreurRepository;
import coopcycle.service.dto.LivreurDTO;
import coopcycle.service.mapper.LivreurMapper;
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
 * Integration tests for the {@link LivreurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class LivreurResourceIT {

    private static final Long DEFAULT_ID_LIVREUR = 1L;
    private static final Long UPDATED_ID_LIVREUR = 2L;

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_TEL = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_COOPERATIVE = "AAAAAAAAAA";
    private static final String UPDATED_COOPERATIVE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livreurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private LivreurMapper livreurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Livreur livreur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreur createEntity(EntityManager em) {
        Livreur livreur = new Livreur()
            .idLivreur(DEFAULT_ID_LIVREUR)
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .numeroTel(DEFAULT_NUMERO_TEL)
            .cooperative(DEFAULT_COOPERATIVE);
        return livreur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreur createUpdatedEntity(EntityManager em) {
        Livreur livreur = new Livreur()
            .idLivreur(UPDATED_ID_LIVREUR)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .numeroTel(UPDATED_NUMERO_TEL)
            .cooperative(UPDATED_COOPERATIVE);
        return livreur;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Livreur.class).block();
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
        livreur = createEntity(em);
    }

    @Test
    void createLivreur() throws Exception {
        int databaseSizeBeforeCreate = livreurRepository.findAll().collectList().block().size();
        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeCreate + 1);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getIdLivreur()).isEqualTo(DEFAULT_ID_LIVREUR);
        assertThat(testLivreur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testLivreur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testLivreur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLivreur.getNumeroTel()).isEqualTo(DEFAULT_NUMERO_TEL);
        assertThat(testLivreur.getCooperative()).isEqualTo(DEFAULT_COOPERATIVE);
    }

    @Test
    void createLivreurWithExistingId() throws Exception {
        // Create the Livreur with an existing ID
        livreur.setId(1L);
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        int databaseSizeBeforeCreate = livreurRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllLivreursAsStream() {
        // Initialize the database
        livreurRepository.save(livreur).block();

        List<Livreur> livreurList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(LivreurDTO.class)
            .getResponseBody()
            .map(livreurMapper::toEntity)
            .filter(livreur::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(livreurList).isNotNull();
        assertThat(livreurList).hasSize(1);
        Livreur testLivreur = livreurList.get(0);
        assertThat(testLivreur.getIdLivreur()).isEqualTo(DEFAULT_ID_LIVREUR);
        assertThat(testLivreur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testLivreur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testLivreur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLivreur.getNumeroTel()).isEqualTo(DEFAULT_NUMERO_TEL);
        assertThat(testLivreur.getCooperative()).isEqualTo(DEFAULT_COOPERATIVE);
    }

    @Test
    void getAllLivreurs() {
        // Initialize the database
        livreurRepository.save(livreur).block();

        // Get all the livreurList
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
            .value(hasItem(livreur.getId().intValue()))
            .jsonPath("$.[*].idLivreur")
            .value(hasItem(DEFAULT_ID_LIVREUR.intValue()))
            .jsonPath("$.[*].nom")
            .value(hasItem(DEFAULT_NOM))
            .jsonPath("$.[*].prenom")
            .value(hasItem(DEFAULT_PRENOM))
            .jsonPath("$.[*].email")
            .value(hasItem(DEFAULT_EMAIL))
            .jsonPath("$.[*].numeroTel")
            .value(hasItem(DEFAULT_NUMERO_TEL))
            .jsonPath("$.[*].cooperative")
            .value(hasItem(DEFAULT_COOPERATIVE));
    }

    @Test
    void getLivreur() {
        // Initialize the database
        livreurRepository.save(livreur).block();

        // Get the livreur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, livreur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(livreur.getId().intValue()))
            .jsonPath("$.idLivreur")
            .value(is(DEFAULT_ID_LIVREUR.intValue()))
            .jsonPath("$.nom")
            .value(is(DEFAULT_NOM))
            .jsonPath("$.prenom")
            .value(is(DEFAULT_PRENOM))
            .jsonPath("$.email")
            .value(is(DEFAULT_EMAIL))
            .jsonPath("$.numeroTel")
            .value(is(DEFAULT_NUMERO_TEL))
            .jsonPath("$.cooperative")
            .value(is(DEFAULT_COOPERATIVE));
    }

    @Test
    void getNonExistingLivreur() {
        // Get the livreur
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingLivreur() throws Exception {
        // Initialize the database
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();

        // Update the livreur
        Livreur updatedLivreur = livreurRepository.findById(livreur.getId()).block();
        updatedLivreur
            .idLivreur(UPDATED_ID_LIVREUR)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .numeroTel(UPDATED_NUMERO_TEL)
            .cooperative(UPDATED_COOPERATIVE);
        LivreurDTO livreurDTO = livreurMapper.toDto(updatedLivreur);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, livreurDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getIdLivreur()).isEqualTo(UPDATED_ID_LIVREUR);
        assertThat(testLivreur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testLivreur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testLivreur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLivreur.getNumeroTel()).isEqualTo(UPDATED_NUMERO_TEL);
        assertThat(testLivreur.getCooperative()).isEqualTo(UPDATED_COOPERATIVE);
    }

    @Test
    void putNonExistingLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, livreurDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateLivreurWithPatch() throws Exception {
        // Initialize the database
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();

        // Update the livreur using partial update
        Livreur partialUpdatedLivreur = new Livreur();
        partialUpdatedLivreur.setId(livreur.getId());

        partialUpdatedLivreur.idLivreur(UPDATED_ID_LIVREUR).numeroTel(UPDATED_NUMERO_TEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLivreur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getIdLivreur()).isEqualTo(UPDATED_ID_LIVREUR);
        assertThat(testLivreur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testLivreur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testLivreur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLivreur.getNumeroTel()).isEqualTo(UPDATED_NUMERO_TEL);
        assertThat(testLivreur.getCooperative()).isEqualTo(DEFAULT_COOPERATIVE);
    }

    @Test
    void fullUpdateLivreurWithPatch() throws Exception {
        // Initialize the database
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();

        // Update the livreur using partial update
        Livreur partialUpdatedLivreur = new Livreur();
        partialUpdatedLivreur.setId(livreur.getId());

        partialUpdatedLivreur
            .idLivreur(UPDATED_ID_LIVREUR)
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .numeroTel(UPDATED_NUMERO_TEL)
            .cooperative(UPDATED_COOPERATIVE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedLivreur.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreur))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getIdLivreur()).isEqualTo(UPDATED_ID_LIVREUR);
        assertThat(testLivreur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testLivreur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testLivreur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLivreur.getNumeroTel()).isEqualTo(UPDATED_NUMERO_TEL);
        assertThat(testLivreur.getCooperative()).isEqualTo(UPDATED_COOPERATIVE);
    }

    @Test
    void patchNonExistingLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, livreurDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().collectList().block().size();
        livreur.setId(count.incrementAndGet());

        // Create the Livreur
        LivreurDTO livreurDTO = livreurMapper.toDto(livreur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(livreurDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteLivreur() {
        // Initialize the database
        livreurRepository.save(livreur).block();

        int databaseSizeBeforeDelete = livreurRepository.findAll().collectList().block().size();

        // Delete the livreur
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, livreur.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Livreur> livreurList = livreurRepository.findAll().collectList().block();
        assertThat(livreurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
