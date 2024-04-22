package org.ibs;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.ibs.pojo.ProductPojo;
import org.ibs.spec.Specification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;



import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;

public class RestTest {

    String homeUrl = "http://localhost:8080";
    String apiGet = "/api/food";
    String apiPost = "/api/food";

    String productName = "Манго";
    String productType = "FRUIT";
    boolean productExotic = true;

    ProductPojo productPojo = new ProductPojo(productName, productType, productExotic);


    @Test
    @DisplayName("Тестирование запроса Get c проверкой status code = 200")
    public void getRequestCheckStatusCode() {
        RestAssured.given()
                .spec(Specification.requestSpecification(homeUrl))
                .when()
                .get(apiGet)
                .then()
                .statusCode(200)
                ;
    }


    @Test
    @DisplayName("Тестирование запроса Get с выводом списка товоров на консоль")
    void  getRequestPrintResult(){
        String response = RestAssured.given()
                .spec(Specification.requestSpecification(homeUrl))
                .when()
                .get(apiGet)
                .getBody()
                .prettyPrint()
                ;
    }

    @Test
    @DisplayName("Тестирование запроса Post с добавлением продукта")
    void postRequestAddProduct() throws JsonProcessingException {
        RestAssured.given()
                .spec(Specification.requestSpecification(homeUrl))
                .contentType(ContentType.JSON)
                .body(productPojo.pojoToJsonString(productName,productType,productExotic))
                .when()
                .log().all()
                .post(apiPost)
                .then()
                .statusCode(200)
                .log().all()
                ;
    }

    @Test
    @DisplayName("Тестирование запроса Post с добавлением продукта")
    void postRequestAddProductGetSessionID() throws JsonProcessingException {
        Response response = RestAssured.given()
                .spec(Specification.requestSpecification(homeUrl))
                .contentType(ContentType.JSON)
                //.body("{\"name\":\"Манго\",\"type\":\"FRUIT\",\"exotic\":true}")
                .body(productPojo)
                .when()
                .log().all()
                .post(apiPost)
                ;

        String sessionId = response.sessionId();

        RestAssured.given()
                .spec(Specification.requestSpecification(homeUrl))
                .sessionId(sessionId)
                .when()
                .get(apiGet)
                .getBody()
                .prettyPrint()
                ;

    }

}
