package Tentativa2;

public class ValidationResult {
    private final boolean valid;
    private final String message;
    private final int[] semanticTagCount;

    public ValidationResult(boolean valid, String message, int[] semanticTagCount) {
        this.valid = valid;
        this.message = message;
        this.semanticTagCount = semanticTagCount.clone(); // Clona o array para evitar modificações externas
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }

    public int[] getSemanticTagCount() {
        return semanticTagCount.clone(); // Clona o array para evitar modificações externas
    }
}
