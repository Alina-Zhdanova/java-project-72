package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Getter
@Setter
public final class Url {
    private Long id;
    private String name;
    private Timestamp createdAt;

    public Url(String name) {
        this.name = name;
    }

    public String getFormattedCreatedAt() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(createdAt);
    }
}
