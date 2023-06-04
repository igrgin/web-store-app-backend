package com.web.store.app.backend.payment.process.cart.document;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
@AllArgsConstructor
@Data
@Document(indexName = "cart_product", versionType = Document.VersionType.INTERNAL)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CartProduct {
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Keyword,name = "cart_id")
    private String cartId;
    @Field(type = FieldType.Keyword,name = "product_id")
    private String productId;
    @Field(type = FieldType.Integer)
    private Integer quantity;
}
