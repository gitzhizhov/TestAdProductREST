package org.ibs.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductPojo {
    private String name;
    private String type;
    private boolean exotic;

    public ProductPojo() {
    }

    public ProductPojo(String name, String type, boolean exotic) {
        this.name = name;
        this.type = type;
        this.exotic = exotic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isExotic() {
        return exotic;
    }

    public void setExotic(boolean exotic) {
        this.exotic = exotic;
    }


    static ObjectMapper objectMapper = new ObjectMapper();

    public String pojoToJsonString(String name, String type, boolean isExotic) throws JsonProcessingException {
        ProductPojo productPojo = new ProductPojo(name, type, isExotic);
        String productJson = objectMapper.writeValueAsString(productPojo);
        //System.out.println(productJson);
        return productJson;
    }
}
