package com.otc.service;

import com.otc.config.OneTimeCodeCodeProperties;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;

@Component
public class OneTimeCodeCodeGenerator {

    private final OneTimeCodeCodeProperties props;
    private final SecureRandom random = new SecureRandom();

    public OneTimeCodeCodeGenerator(OneTimeCodeCodeProperties props) {
        this.props = props;
    }

    public GeneratedCode generate() {
        String raw = buildRaw();
        String formatted = format(raw);
        return new GeneratedCode(raw, formatted);
    }

    private String buildRaw() {
        String alphabet = props.alphabet();
        List<Integer> groups = props.groups();
        int total = groups.stream().mapToInt(Integer::intValue).sum();
        StringBuilder sb = new StringBuilder(total);
        for (int i = 0; i < total; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    private String format(String raw) {
        String sep = props.separator();
        StringBuilder sb = new StringBuilder();
        sb.append(props.prefix());

        int idx = 0;
        for (Integer g : props.groups()) {
            sb.append(sep);
            sb.append(raw, idx, idx + g);
            idx += g;
        }
        return sb.toString();
    }

    public record GeneratedCode(String code, String formatted) {}}
