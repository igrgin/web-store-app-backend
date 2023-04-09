package com.web.store.app.entity;

import jakarta.persistence.Id;
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
public class Transaction {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;
    private String userId;
    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Date createdAt;
    private List<Long> productIds;
}
