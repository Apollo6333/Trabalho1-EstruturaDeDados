package Tentativa2;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlValidator {
    private static final Pattern TAG_PATTERN = Pattern.compile("<\\s*([^\\s>/!]+)([^>]*)>|<\\s*/\\s*([^\\s>/!]+)\\s*>");
    private static final Pattern DOCTYPE_PATTERN = Pattern.compile("<!DOCTYPE\\s+html\\s*>", Pattern.CASE_INSENSITIVE);
    private static final Set<String> SEMANTIC_TAGS = new HashSet<>(Arrays.asList(
        "header", "form", "nav", "main", "footer", "article", "section", "aside", "h1", "h2", "h3", "h4", "h5", "h6", "p", "div"
    ));

    public ValidationResult validate(String html) {
        Pilha<String> tagStack = new PilhaArrayList<>();
        Map<String, Integer> semanticTagCount = new HashMap<>();
        String[] lines = html.split("\n");
        int lineNumber = 0;
        boolean insideBody = false;

        for (String line : lines) {
            lineNumber++;
            line = line.trim();
            if (line.isEmpty()) continue;

            // Skip DOCTYPE declaration
            if (DOCTYPE_PATTERN.matcher(line).find()) {
                continue;
            }

            Matcher matcher = TAG_PATTERN.matcher(line);
            while (matcher.find()) {
                String tag;
                if (matcher.group(1) != null) {
                    // Opening tag
                    tag = matcher.group(1).toLowerCase();
                    if (tag.equals("body")) {
                        insideBody = true;
                    }
                    if (!isSelfClosingTag(tag)) {
                        tagStack.push(tag);
                    }
                    if (insideBody && SEMANTIC_TAGS.contains(tag)) {
                        semanticTagCount.put(tag, semanticTagCount.getOrDefault(tag, 0) + 1);
                    }
                } else if (matcher.group(3) != null) {
                    // Closing tag
                    tag = matcher.group(3).toLowerCase();
                    if (tag.equals("body")) {
                        insideBody = false;
                    }
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
}
