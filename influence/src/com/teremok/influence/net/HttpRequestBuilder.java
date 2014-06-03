package com.teremok.influence.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpParametersUtils;
import com.teremok.influence.util.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static com.teremok.influence.util.NotForPublicAccess.HASH_APPENDER;

/**
 * Created by Алексей on 03.06.2014
 */
public class HttpRequestBuilder {

    private static final String SERVER_URL = "http://timeforlime.ru/influence/index.php" ;

    public static Net.HttpRequest build(Map<String, String> parameters) {

        StringBuilder stringBuilder = new StringBuilder();

        for (String value : parameters.values()) {
            stringBuilder.append(value);
        }
        stringBuilder.append(HASH_APPENDER);

        String hash = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(stringBuilder.toString().getBytes());
            byte[] byteData = digest.digest();


            StringBuilder hexString = new StringBuilder();
            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            hash = hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Logger.log("string for hash: " + stringBuilder.toString());
        Logger.log("hash: " + hash);
        parameters.put("hash", hash);


        Net.HttpRequest httpGet = new Net.HttpRequest(Net.HttpMethods.GET);
        httpGet.setUrl(SERVER_URL);
        httpGet.setContent(HttpParametersUtils.convertHttpParameters(parameters));
        return httpGet;
    }



}
