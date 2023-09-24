package com.hepsiburada.hepsiburada.config;

import lombok.*;

@Data
public class Endpoints {
    public static final String testUrl = "hepsiburada.com/";
    public static final String prodUrl = "hepsiburada.com/";

    public static final String allCategories = "product/api/categories/get-all-categories";
    public static final String getCategoriAttrributes = "product/api/categories/@categoriID/attributes";
    public static final String getAttributeValues = "product/api/categories/@categoriID/attribute/@attributeSlug/values";

}
