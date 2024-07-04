import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HTMLValidator {

    private static final List<String> REQUIRED_TAGS_IN_ORDER = Arrays.asList(
            "!DOCTYPE html", "html lang", "head", "meta charset", "meta name", "/head", "body", "/body", "/html"
    );

    private static final Set<String> SEMANTIC_TAGS = new HashSet<>(Arrays.asList(
            "header", "nav", "main", "footer", "article", "section", "aside", "h1", "h2", "h3", "h4", "h5", "h6", "p", "div"
    ));

    public boolean isValidHtml(String html) {
        int currentTagIndex = 0;
        boolean insideBody = false;

        for (String requiredTag : REQUIRED_TAGS_IN_ORDER) {
            int tagIndex = html.indexOf(requiredTag, currentTagIndex);
            if (tagIndex == -1) {
                return false; // Tag not found
            }
            currentTagIndex = tagIndex + requiredTag.length();

            if (requiredTag.equals("body")) {
                insideBody = true;
            } else if (requiredTag.equals("/body")) {
                insideBody = false;
            }

            // Check for semantic tags outside of the body

            //PRECISA COLOCAR <> PARA CONSEGUIR CHECAR
            if (!insideBody) {
                int nextTagIndex = html.indexOf('<', currentTagIndex);
                while (nextTagIndex != -1 && nextTagIndex < html.indexOf("body", currentTagIndex)) {
                    String nextTag = html.substring(nextTagIndex + 1, html.indexOf('>', nextTagIndex)).trim().split(" ")[0];
                    if (SEMANTIC_TAGS.contains(nextTag)) {
                        return false; // Semantic tag found outside of body
                    }
                    nextTagIndex = html.indexOf('<', nextTagIndex + 1);
                }
            }
        }

        return true;
    }
}
