package com.oauth2.authorization.utils;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
/**
 * <pre>
 * com.oauth2.authorization
 * GlobalUtils.java
 * </pre>
 *
 * @author : insung
 * @date : 2024. 3. 07.
 * @desc : 공통 유틸 클래스
 */
public class GlobalUtils {

    /**
     * <pre>
     * 1. 개요 : IS8601 날짜 포멧 메서드
     * 2. 처리내용 : IS8601 형식의 날짜 포멧을 반환시킨다.
     * </pre>
     * @Method Name : IS8601FormatConverting
     * @date : 2024. 3. 07.
     * @author : insung
     * @history :
     * ----------------------------------------------------------------------------------
     * 변경일 작성자 변경내역
     * -------------- -------------- ----------------------------------------------------
     * 2024. 3. 07. insung 최초작성
     * ----------------------------------------------------------------------------------
     */
    public static String IS8601FormatConverting(String stringDate) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(stringDate);
        Date date = Date.from(zonedDateTime.toInstant());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        return dateFormat.format(date);
    }

    public static String BCryptEncode(String text) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(text);
    }

    public static String dateFormat(LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public static String generateRandomPassword() {
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);

        CharacterData specialChars = new CharacterData() {
            public String getErrorCode() {
                return "ERROR_CODE";
            }

            public String getCharacters() {
                return "!@#$%^&*()_+";
            }
        };
        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);

        return gen.generatePassword(20, splCharRule, lowerCaseRule,
                upperCaseRule, digitRule);
    }



}
