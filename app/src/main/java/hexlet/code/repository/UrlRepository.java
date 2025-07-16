package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    private static final List<Url> ENTITIES = new ArrayList<>();

    public static void save(Url url) {
        url.setId((long) ENTITIES.size() + 1);
        url.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        ENTITIES.add(url);
    }

    public static Optional<Url> find(Long id) {
        var url = ENTITIES.stream()
            .filter(entity -> entity.getId() == id)
            .findAny();
        return url;
    }

    public static Optional<Url> find(String name) {
        var url = ENTITIES.stream()
            .filter(entity -> entity.getName().equals(name))
            .findAny();
        return url;
    }

    public static List<Url> getEntities() {
        return new ArrayList<>(ENTITIES);
    }

}
