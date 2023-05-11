package subway.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.InitSectionRequest;
import subway.dto.EndSectionRequest;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SectionControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("처음 노선에 구간을 등록할 수 있다.")
    @Test
    void createInitSections() {
        InitSectionRequest request = new InitSectionRequest(1L, 1L, 2L, 10);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines/1/init-sections")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에 역을 추가할 수 있다.")
    @Test
    void createSection() {
        // 3L 강변
        // 2L 잠실
        // 1L 잠실나루

        InitSectionRequest initRequest = new InitSectionRequest(1L, 3L, 2L, 10);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post("/lines/1/init-sections")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest mainRequest = new SectionRequest(
                1L,
                1L,
                3L,
                2L,
                3,
                7);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mainRequest)
                .when().post("/lines/1/section")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에 종착역을 추가할 수 있다.")
    @Test
    void createEndSection() {
        // 3L 강변
        // 2L 잠실
        // 1L 잠실나루

        InitSectionRequest initRequest = new InitSectionRequest(1L, 1L, 2L, 10);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest)
                .when().post("/lines/1/init-sections")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        EndSectionRequest mainRequest = new EndSectionRequest(
                1L,
                3L,
                1L,
                5
        );

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mainRequest)
                .when().post("/lines/1/end-section")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("노선에서 하나의 역을 제거할 수 있다.")
    @Test
    void deleteSection() {
        // 3L 강변
        // 2L 잠실
        // 1L 잠실나루

        InitSectionRequest initRequest1 = new InitSectionRequest(1L, 3L, 2L, 10);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest1)
                .when().post("/lines/1/init-sections")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest2 = new SectionRequest(
                1L,
                1L,
                3L,
                2L,
                3,
                7);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest2)
                .when().post("/lines/1/section")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/1/section?station-id=1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }


    @DisplayName("노선에서 하나의 종착역을 제거할 수 있다.")
    @Test
    void deleteEndSection() {
        // 3L 강변
        // 2L 잠실
        // 1L 잠실나루

        InitSectionRequest initRequest1 = new InitSectionRequest(1L, 3L, 2L, 10);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest1)
                .when().post("/lines/1/init-sections")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        SectionRequest initRequest2 = new SectionRequest(
                1L,
                1L,
                3L,
                2L,
                3,
                7);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest2)
                .when().post("/lines/1/section")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/1/end-section?station-id=3")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("노선에서 마지막 남은 두개의 역을 제거할 수 있다.")
    @Test
    void deleteLastSection() {
        // 3L 강변
        // 2L 잠실
        // 1L 잠실나루

        InitSectionRequest initRequest1 = new InitSectionRequest(1L, 3L, 2L, 10);

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(initRequest1)
                .when().post("/lines/1/init-sections")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/1/last-sections?upward-id=3&downward-id=2")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

}
