package com.web.store.app.backend.product.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@AllArgsConstructor
@Data
@Document(indexName = "product", versionType = Document.VersionType.INTERNAL)
public class Product {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Keyword)
    private String brand;
    @Field(type = FieldType.Keyword)
    private String name;
    @Field(type = FieldType.Float)
    private Float price;
    @Field(type = FieldType.Keyword)
    private String category;
    @Field(type = FieldType.Keyword)
    private String subcategory;
    @Field(type = FieldType.Keyword, name = "image_url")
    private String imageURL;
    @Field(type = FieldType.Long)
    private Long stock;
    @Field(type = FieldType.Text, analyzer = "english")
    private String description;
}