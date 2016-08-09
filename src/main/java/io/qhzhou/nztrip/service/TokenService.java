package io.qhzhou.nztrip.service;

import io.qhzhou.nztrip.common.AES;
import io.qhzhou.nztrip.dto.Role;
import io.qhzhou.nztrip.dto.Token;
import io.qhzhou.nztrip.exception.EncryptionException;
import io.qhzhou.nztrip.exception.InvalidTokenException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by qianhao.zhou on 3/22/16.
 */
@Service("tokenService")
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private static final long DEFAULT_TOKEN_EXPIRE_TIME = 86400000L * 7;// 7 days

    @Value("${aes.iv}")
    private String iv;

    @Value("${aes.key}")
    private String key;

    private AES aes;

    private Base64 base64;

    @PostConstruct
    public void init() {
        aes = new AES(iv, key);
        base64 = new Base64(true);
    }

    public String encrypt(String plainText) throws EncryptionException {
        try {
            return new String(base64.encode(aes.encrypt(plainText)));
        } catch (EncryptionException e) {
            logger.error("error encrypt plainText", e);
            throw e;
        }
    }

    public String decrypt(String cipherText) throws EncryptionException {
        try {
            return new String(aes.decrypt(base64.decode(cipherText)));
        } catch (EncryptionException e) {
            logger.error("error decrypt cipherText:" + cipherText, e);
            throw e;
        }
    }

    public String generateToken(Role role, int id) {
        return generateToken(role, id, DEFAULT_TOKEN_EXPIRE_TIME);
    }

    public String generateToken(Role role, int id, long age) {
            return encrypt(role.name() + "_" + id + "_" + (System.currentTimeMillis() + age));
    }

    public Token parseToken(String token) throws InvalidTokenException {
        if (token == null) {
            throw new InvalidTokenException("token cannot be null");
        }
        String decode ;
        try {
            decode = URLDecoder.decode(token, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new InvalidTokenException(token + " cannot be decoded");
        }
        String decrypted;
        try {
            decrypted = decrypt(decode);
        } catch (EncryptionException e) {
            throw new InvalidTokenException(decode);
        }
        String[] strings = decrypted.split("_");
        if (strings.length != 3) {
            throw new InvalidTokenException(decode);
        }
        try {
            Role role = Role.valueOf(strings[0]);
            int id = Integer.parseInt(strings[1]);
            long expireTime = Long.parseLong(strings[2]);
            return new Token(role, id, expireTime);
        } catch (Exception e) {
            throw new InvalidTokenException(decode);
        }

    }

}
