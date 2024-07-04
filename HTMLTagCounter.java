import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLTagCounter {
    private static final Pattern PadraoTag = Pattern.compile("(?<=<)\\s*([^\\s>/]+)(?=[\\s>])");
    private static final Set<String> TagsSemanticas = new HashSet<>(Set.of(
        "header", "nav", "main", "footer", "article", "section", "aside", "h1", "h2", "h3", "h4", "h5", "h6", "p", "div"
    ));

    public Map<String, Integer> countTags(String html) {
        Map<String, Integer> tagFrequency = new HashMap<>();
        Matcher matcher = PadraoTag.matcher(html);

        boolean insideBody = false;

        while (matcher.find()) {
            String tag = matcher.group(1).toLowerCase();

            if (tag.equals("body")) {
                insideBody = true;
            } else if (tag.equals("/body")) {
                insideBody = false;
            }

            if (insideBody && TagsSemanticas.contains(tag)) {
                tagFrequency.put(tag, tagFrequency.getOrDefault(tag, 0) + 1);
            }
        }

        return tagFrequency;
    }
}
