package com.web.store.app.backend.transaction.dto;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public record CartProduct(@Field(type = FieldType.Keyword) String id, @Field(type = FieldType.Integer) Integer quantity,
                          @Field(type = FieldType.Keyword) String name, @Field(type = FieldType.Float) Float price) {
}
