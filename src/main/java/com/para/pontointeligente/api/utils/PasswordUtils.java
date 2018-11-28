package com.para.pontointeligente.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    private static final Logger log = LoggerFactory.getLogger(PasswordUtils.class);

    public PasswordUtils() {

    }

    /**
     * Gera um has utilizando o BCrypt.
     *
     * @param senha
     * @return String
     */

    public static String gerarBCrypt(String senha) {
        if (senha == null) {
            return senha;
        }

        log.info("Gerando hast com BCrypt.");
        BCryptPasswordEncoder bCryptEnconder = new BCryptPasswordEncoder();
        return bCryptEnconder.encode(senha);
    }


}
