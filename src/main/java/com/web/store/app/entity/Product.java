package com.web.store.app.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@AllArgsConstructor
@Data
@Document(indexName = "product", versionType = Document.VersionType.INTERNAL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Product {

    @Id
    @NotEmpty
    @Field(type = FieldType.Keyword)
    private String id;
    @NotEmpty
    @Field(type = FieldType.Keyword)
    private String brand;
    @NotEmpty
    @Field(type = FieldType.Keyword)
    private String model;
    @NotEmpty
    @Field(type = FieldType.Keyword)
    private String name;
    @NotNull
    @PositiveOrZero
    @Field(type = FieldType.Float)
    private Float price;
    @NotEmpty
    @Field(type = FieldType.Keyword)
    private String category;
    @NotNull
    @PositiveOrZero
    @Field(type = FieldType.Long)
    private Long stock;
    @NotNull
    @Field(type = FieldType.Text, analyzer = "english")
    private String description;
}