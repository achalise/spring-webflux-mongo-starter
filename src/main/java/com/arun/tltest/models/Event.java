package com.arun.tltest.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "events")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    private String _id;
    private String type;
    private Long time;
    private String user;
}
