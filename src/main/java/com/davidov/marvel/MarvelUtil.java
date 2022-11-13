package com.davidov.marvel;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MarvelUtil {

	public static final String MARVEL_URL_API = "https://gateway.marvel.com:443/v1/public";
    public static final String MARVEL_URI_COMICS = "/comics";
    public static final String MARVEL_URI_CHARACTERS = "/characters";
    public static final String MARVEL_URI_CREATORS = "/creators";
    public static final String MARVEL_URI_EVENTS = "/events";
    public static final String MARVEL_URI_SERIES = "/series";
    public static final String MARVEL_PARAM_TIMESTAMP = "ts";
    public static final String MARVEL_PARAM_APIKEY = "apikey";
    public static final String MARVEL_PARAM_HASH = "hash";
    public static final String MARVEL_PARAM_LIMIT = "limit";

	public static final int DEFAULT_PAGE_SIZE = 5;
   
	private MarvelUtil() {
		// hide public constructor
	}

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
