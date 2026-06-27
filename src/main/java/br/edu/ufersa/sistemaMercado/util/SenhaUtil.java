package br.edu.ufersa.sistemaMercado.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Gera o hash da senha (SHA-256) para não guardar a senha em texto puro no banco.
public final class SenhaUtil {

    private SenhaUtil() {
    }

    public static String hash(String senha) {
        if (senha == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algoritmo de hash indisponível.", e);
        }
    }
}
