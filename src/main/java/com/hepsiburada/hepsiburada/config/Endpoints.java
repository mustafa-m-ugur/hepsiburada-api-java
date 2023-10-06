package com.hepsiburada.hepsiburada.config;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endpoints {
    private final String testUrl = "hepsiburada.com/";
    private final String prodUrl = "hepsiburada.com/";

    private final String allCategories = "product/api/categories/get-all-categories";
    private final String getCategoriAttrributes = "product/api/categories/@categoriID/attributes";
    private final String getAttributeValues = "product/api/categories/@categoriID/attribute/@attributeSlug/values";

}
