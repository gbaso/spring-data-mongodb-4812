package com.github.gbaso.springdatamongodb4812;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
record MyEntity(
    @Id String id,
    String name
) {}
