import java.util.Arrays;
import java.util.List;

public class HTMLValidator {

    private static final List<String> REQUIRED_TAGS_IN_ORDER = Arrays.asList(
            "!DOCTYPE html", "html lang", "head", "meta charset", "meta name", "/head", "body", "/body", "/html"
    );

    public boolean isValidHtml(String html) {
        int currentTagIndex = 0;

        for (String requiredTag : REQUIRED_TAGS_IN_ORDER) {
            int tagIndex = html.indexOf(requiredTag);
            if (tagIndex == -1 || (currentTagIndex != 0 && tagIndex < currentTagIndex)) {
                return false; // Tag not found or out of order
            }
            currentTagIndex = tagIndex + requiredTag.length();
        }
        return true;
    }
}
