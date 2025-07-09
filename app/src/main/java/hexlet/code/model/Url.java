package hexlet.code.model;

import java.sql.Timestamp;

public record Url (int id, String name, Timestamp createdAt) { }
