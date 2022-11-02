package com.test.marvel;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MarvelUtil {
   
    public static long getTimestamp() {
        log.debug(">>> getTimestamp");
        return Instant.now().toEpochMilli();
    }

	public static String getHash(String publicApiKey, String privateApiKey, long timeStamp) {
        log.debug(">>> getHash");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			String preHash = timeStamp + privateApiKey + publicApiKey;
			return new BigInteger(1, md.digest(preHash.getBytes())).toString(16);
		} catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage());
		}

		return null;
	}
}
