package hexlet.code.dto.urls;

import io.javalin.validation.ValidationError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BuildUrlPage {
    public String name;
    public Map<String, List<ValidationError<Object>>> errors;
}
