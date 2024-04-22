package org.ibs.steps;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.ru.И;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.ibs.pojo.ProductPojo;
import org.ibs.spec.Specification;
import org.junit.jupiter.api.Assertions;

import java.util.List;



public class Step {

    static String homeUrl = "http://localhost:8080";
    static String apiGet = "/api/food";
    static String apiPost = "/api/food";
    static String cookie;
    static List<ProductPojo> product;


    @И("отправляем запрос на добавление продукта {}, {}, {}")
    public void postRequestAddProduct(String nameProd, String typeProd, boolean isExotic) throws JsonProcessingException {
        ProductPojo productPojo = new ProductPojo(nameProd, typeProd, isExotic);
        cookie = RestAssured.given()
                .spec(Specification.requestSpecification(homeUrl))
                .contentType(ContentType.JSON)
                .body(productPojo.pojoToJsonString(nameProd,typeProd,isExotic))
                //.body(productPojo)
                .when()
                .log().all()
                .post(apiPost)
                .then()
                .statusCode(200)
                .log().all().extract().sessionId();
    }

    @И("запрашиваем все продукты")
    public void getRequestAllProduct() {
        product = RestAssured.given()
                .spec(Specification.requestSpecification(homeUrl))
                .sessionId(cookie)
                .contentType(ContentType.JSON)
                .when()
                .get(apiGet)
                .then()
                .log().all()
                .extract()
                .body()
                .jsonPath()
                .getList(".", ProductPojo.class)
                ;
    }

    @И("проверяем добавленый продукт {}, {}, {}")
    public void checkAddProduct(String nameProd, String typeProd, boolean isExotic) {
        int lastProd = product.size() - 1;
        Assertions.assertEquals(nameProd, product.get(lastProd).getName(), "Название товара не соответствует");
        Assertions.assertEquals(typeProd, product.get(lastProd).getType(), "Тип товара не соответствует");
        Assertions.assertEquals(isExotic, product.get(lastProd).isExotic(), "Признак 'Экзотик' не соответствует");
    }
}
