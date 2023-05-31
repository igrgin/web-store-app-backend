package com.web.store.app.backend.transaction.document;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.web.store.app.backend.transaction.dto.CartProduct;
import com.web.store.app.backend.transaction.model.TransactionStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@Document(indexName = "transaction", versionType = Document.VersionType.INTERNAL)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Transaction {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    @Field(type = FieldType.Long, name = "user_id")
    private Long userId;
    @Field(type = FieldType.Date, format = DateFormat.date_time, name = "created_at")
    @PastOrPresent(message = "Date must be in the past or present")
    private ZonedDateTime createdAt;
    @Field(type = FieldType.Nested, name = "cart")
    private List<CartProduct> cart;
    @Enumerated(EnumType.STRING)
    @Field(type = FieldType.Keyword, name = "status")
    private TransactionStatus status;
    @Field(type = FieldType.Float, name = "price")
    private Float price;
}
