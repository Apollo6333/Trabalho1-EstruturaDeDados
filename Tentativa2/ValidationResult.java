package Tentativa2;

import java.util.Map;

public class ValidationResult {
    private final boolean valid;
    private final String message;
    private final Map<String, Integer> semanticTagCount;

    public ValidationResult(boolean valid, String message, Map<String, Integer> semanticTagCount) {
        this.valid = valid;
        this.message = message;
        this.semanticTagCount = semanticTagCount;
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Integer> getSemanticTagCount() {
        return semanticTagCount;
    }
}
