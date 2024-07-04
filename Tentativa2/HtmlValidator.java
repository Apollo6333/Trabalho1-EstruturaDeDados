package Tentativa2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlValidator {
    private static final Pattern TAG_PATTERN = Pattern.compile("<\\s*([^\\s>/!]+)([^>]*)>|<\\s*/\\s*([^\\s>/!]+)\\s*>");
    protected static final String[] SEMANTIC_TAGS = {
            "!DOCTYPE", "html", "header", "form", "nav", "main", "footer", "article", "section", "aside",
            "h1", "h2", "h3", "h4", "h5", "h6", "p", "div"
    };

    public ValidationResult validate(String html) {
        Pilha<String> tagStack = new PilhaArrayList<>();
        int[] semanticTagCount = new int[SEMANTIC_TAGS.length];
        String[] lines = html.split("\n");
        int lineNumber = 0;

        for (String line : lines) {
            lineNumber++;
            line = line.trim();
            if (line.isEmpty()) continue;

            Matcher matcher = TAG_PATTERN.matcher(line);
            while (matcher.find()) {
                String tag;
                if (matcher.group(1) != null) {
                    // Opening tag
                    tag = matcher.group(1).toLowerCase();
                    if (!isSelfClosingTag(tag)) {
                        tagStack.push(tag);
                    }
                    incrementSemanticTagCount(tag, semanticTagCount);
                } else if (matcher.group(3) != null) {
                    // Closing tag
                    tag = matcher.group(3).toLowerCase();
                    try {
                        if (tagStack.estaVazia()) {
                            return new ValidationResult(false, "Foi encontrada uma tag final inesperada </" + tag + "> na linha " + lineNumber + " (nenhuma tag final esperada).", semanticTagCount);
                        }
                        String expectedTag = tagStack.peek();
                        if (!expectedTag.equals(tag)) {
                            return new ValidationResult(false, "Foi encontrada uma tag final inesperada </" + tag + "> na linha " + lineNumber + " (aguardava-se </" + expectedTag + ">).", semanticTagCount);
                        }
                        tagStack.pop();
                    } catch (PilhaVaziaException e) {
                        return new ValidationResult(false, e.getMessage(), semanticTagCount);
                    }
                }
            }
        }

        if (!tagStack.estaVazia()) {
            StringBuilder missingTags = new StringBuilder();
            while (!tagStack.estaVazia()) {
                try {
                    missingTags.append("</").append(tagStack.pop()).append("> ");
                } catch (PilhaVaziaException e) {
                    break;
                }
            }
            return new ValidationResult(false, "Faltam tags finais: " + missingTags.toString().trim(), semanticTagCount);
        }

        return new ValidationResult(true, "HTML bem formatado", semanticTagCount);
    }

    private boolean isSelfClosingTag(String tag) {
        return tag.equals("meta") || tag.equals("link") || tag.equals("img") || tag.equals("input") || tag.equals("br") || tag.equals("hr");
    }

    private void incrementSemanticTagCount(String tag, int[] semanticTagCount) {
        for (int i = 0; i < SEMANTIC_TAGS.length; i++) {
            if (SEMANTIC_TAGS[i].equals(tag)) {
                semanticTagCount[i]++;
                return;
            }
        }
    }
}
