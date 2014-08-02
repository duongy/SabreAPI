package com.sabre.DestinationFinder;

import java.util.concurrent.ExecutionException;

import com.sabre.SabreBase.DSCommHandler;

import org.apache.commons.codec.binary.Base64;

public class DestinationFinder {
	// TODO Auto-generated method stub
	//
	// Request authentication
	//
	private final String clientId = "V1:8wwal2rsmapuiak7:DEVCENTER:EXT";
	private final String clientSecret = "3M6EVsuo";
	private final String target = "http://api.test.sabre.com";
	private final String endpoint = "/v1/shop/flights/fares";

	// base64 encode clientId and clientSecret
	public String request() {

		String encodedClientId = new String(Base64.encodeBase64(
				clientId.getBytes()));
		String encodedClientSecret = new String(Base64.encodeBase64(
				clientSecret.getBytes()));

		// Concatenate encoded client and secret strings, separated with colon
		String encodedClientIdSecret = encodedClientId + ":"
				+ encodedClientSecret;

		// Convert the encoded concatenated string to a single base64 encoded
		// string.
		encodedClientIdSecret = new String(Base64.encodeBase64(
				encodedClientIdSecret.getBytes()));

		DSCommHandler dsC = new DSCommHandler();
		String token = dsC.getAuthToken("https://api.test.sabre.com",
				encodedClientIdSecret);
		String response = dsC.sendRequest(
				target + endpoint, token);
		// Display the response String
		return response;

		// dsC.sendRequest("https://api.test.sabre.com/v1/shop/flights/fares?origin=LAS&lengthofstay=5",

	}
	
	public String asyncRequest() {

		String encodedClientId = new String(Base64.encodeBase64(
				clientId.getBytes()));
		String encodedClientSecret = new String(Base64.encodeBase64(
				clientSecret.getBytes()));

		// Concatenate encoded client and secret strings, separated with colon
		String encodedClientIdSecret = encodedClientId + ":"
				+ encodedClientSecret;

		// Convert the encoded concatenated string to a single base64 encoded
		// string.
		encodedClientIdSecret = new String(Base64.encodeBase64(
				encodedClientIdSecret.getBytes()));

		DSCommHandler dsC = new DSCommHandler();
		try {
			return dsC.asyncRequest(
					target + endpoint, target, encodedClientIdSecret);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
		// Display the response String

		// dsC.sendRequest("https://api.test.sabre.com/v1/shop/flights/fares?origin=LAS&lengthofstay=5",

	}
}