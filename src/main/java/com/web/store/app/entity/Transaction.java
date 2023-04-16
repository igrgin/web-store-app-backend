package com.web.store.app.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Id;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
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
    private Integer userId;
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    @PastOrPresent(message = "Date must be in the past or present")
    private Date createdAt;
    private List<Long> productIds;
}
