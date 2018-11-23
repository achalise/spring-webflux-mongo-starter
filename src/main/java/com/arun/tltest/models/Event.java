package com.arun.tltest.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "events")
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    private String _id;
    private String type;
    private Long time;
    private String user;
}
