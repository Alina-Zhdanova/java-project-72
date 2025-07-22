package hexlet.code.repository;

import hexlet.code.model.UrlCheck;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {

    private static List<UrlCheck> checks = new ArrayList<>();

    public static void save(UrlCheck check) {
        check.setId((long) checks.size() + 1);
        check.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        checks.add(check);
    }

    public static List<UrlCheck> find(Long urlId) {
        var idChecks = new ArrayList<>(checks.stream()
            .filter(check -> check.getUrlId() == urlId)
            .toList());
        Collections.reverse(idChecks);
        return idChecks;
    }

    public static void removeAll() {
        checks = new ArrayList<UrlCheck>();
    }

}
