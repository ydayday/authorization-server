package com.oauth2.authorization.config.common;

import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.oauth2.authorization.exception.ErrorCode;
import com.oauth2.authorization.exception.GlobalException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <pre>
 * com.oauth2.authorization
 * CustomRsaKeyPairJWKSource.java
 * </pre>
 *
 * @author : insung
 * @date : 2024. 3. 07.
 * @desc : 별도 생성한 RSA 비대칭 키를 주입하기 위한 커스터마이징 클래스(토큰 발급 시점마다 작동)
 */
@Component
@RequiredArgsConstructor
public class CustomRsaKeyPairJWKSource implements JWKSource<SecurityContext>, OAuth2TokenCustomizer<JwtEncodingContext> {

    @Value("${private-key}")
    private String privateKeyValue;

    @Override
    public List<JWK> get(JWKSelector jwkSelector, SecurityContext securityContext) throws KeySourceException {
        KeyPair keyPair = null;
        try {
            keyPair = getRsaKeyPair();
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.INTERNAL_SERVER_ERROR, e);
        }

        List<JWK> result = new ArrayList<>();
        RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();

        result.add(rsaKey);

        return result;
    }

    /**
     * <pre>
     * 1. 개요 : void
     * 2. 처리내용 : JwtEncoder의 설정을 사용자 정의할 수 있다.
     * </pre>
     * @Method Name : customize
     * @date : 2024. 3. 20.
     * @author : insung
     * @history :
     * ----------------------------------------------------------------------------------
     * 변경일 작성자 변경내역
     * -------------- -------------- ----------------------------------------------------
     * 2024. 3. 20. insung 최초작성
     * ----------------------------------------------------------------------------------
     */
    @Override
    public void customize(JwtEncodingContext context) {
        Authentication principal = context.getPrincipal();
    }

    /**
     * <pre>
     * 1. 개요 : RSA256 비대칭키 설정
     * 2. 처리내용 : 별도 생성한 비대칭키를 주입한 KeyPair 생성
     *
     * </pre>
     *
     * @Method Name : getRsaKeyPair
     * @date : 2024. 3. 07.
     * @author : insung
     * @history :
     * ----------------------------------------------------------------------------------
     * 변경일 작성자 변경내역
     * -------------- -------------- ----------------------------------------------------
     * 2024. 3. 07. insung 최초작성
     * ----------------------------------------------------------------------------------
     */
    public KeyPair getRsaKeyPair() throws Exception {
        PrivateKey privateKey = stringToprivateKey(privateKeyValue);
        RSAPrivateCrtKey crtKey = (RSAPrivateCrtKey)privateKey;
        RSAPublicKeySpec publicKeySpec = new java.security.spec.RSAPublicKeySpec(crtKey.getModulus(), crtKey.getPublicExponent());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        return new KeyPair(publicKey, privateKey);
    }


    public PrivateKey stringToprivateKey(String strPrivateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        String keyPEM = strPrivateKey
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");

        /* STEP 2 : 키값 바이트 변환 */
        byte[] decode = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(keyPEM);

        /* STEP 3 : PRIVATE, PUBLIC KEY 객체 리턴 */
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * <pre>
     * 1. 개요 : security.key 객체 반환 유틸
     * 2. 처리내용 :
     *
     * </pre>
     *
     * @Method Name : getRsaKeyPair
     * @date : 2024. 3. 07.
     * @author : insung
     * @history :
     * ----------------------------------------------------------------------------------
     * 변경일 작성자 변경내역
     * -------------- -------------- ----------------------------------------------------
     * 2024. 3. 07. insung 최초작성
     * ----------------------------------------------------------------------------------
     */
    public static Key pemToSecureKey(String fileName, String keyType) throws Exception {
        ClassPathResource resource = new ClassPathResource("/pem/" + fileName);
        String PEMStrData = readPEMFile(resource);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        String keyPEM = PEMStrData
                .replace("-----BEGIN " + keyType + " KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END " + keyType + " KEY-----", "");

        /* STEP 2 : 키값 바이트 변환 */
        byte[] bytes = org.apache.tomcat.util.codec.binary.Base64.decodeBase64(keyPEM);

        /* STEP 3 : PRIVATE, PUBLIC KEY 객체 리턴 */
        if (fileName.equals(KeyType.PRIVATE.fileName)) {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            return keyFactory.generatePrivate(keySpec);
        } else {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            return keyFactory.generatePublic(keySpec);
        }
    }

    private static String readPEMFile(ClassPathResource classPathResource) {
        String resultcontent = "";
        try {
            InputStream inputStream = classPathResource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if(line == null ) break;
                builder.append(line);
            }

            resultcontent = builder.toString();

        } catch (IOException e) {
            throw new GlobalException(ErrorCode.PEM_FILE_READ_FAIL, e);
        }
        return resultcontent;
    }

    @Getter
    @AllArgsConstructor
    public enum KeyType {

        PRIVATE("PRIVATE", "private.pem"),

        PUBLIC("PUBLIC", "public.pem");

        private final String value;

        private final String fileName;

    }


}
