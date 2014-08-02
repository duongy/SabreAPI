package com.sabre.SabreBase;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class DSCommHandler {
	private URL urlConn;
	private URLConnection conn;

	public String getAuthToken(String apiEndPoint, String encodedCliAndSecret) {
		// receives : apiEndPoint (https://api.test.sabre.com)
		// sabre//encodedCliAndSecret : base64Encode(
		// base64Encode(V1:[user]:[group]:[domain]) + ":" +
		// base64Encode([secret]) )
		String strRet = null;

		try {

			urlConn = new URL(apiEndPoint + "/v1/auth/token");
			conn = urlConn.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Authorization", "Basic "
					+ encodedCliAndSecret);
			conn.setRequestProperty("Accept", "application/json");
			DataOutputStream dataOut = new DataOutputStream(
					conn.getOutputStream());
			dataOut.writeBytes("grant_type=client_credentials");
			dataOut.flush();
			dataOut.close();

			// get response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String strChunk = "";
			StringBuilder sb = new StringBuilder();
			while (null != ((strChunk = rd.readLine())))
				sb.append(strChunk);

			// parse the token
			JSONObject respParser = new JSONObject(sb.toString());
			strRet = respParser.getString("access_token");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strRet;
	}

	@SuppressWarnings("deprecation")
	public String sendRequest(String payLoad, String authToken) {
		URLConnection conn = null;
		String strRet = null;
		try {
			URL urlConn = new URL(payLoad);

			conn = null;
			conn = urlConn.openConnection();

			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestProperty("Authorization", "Bearer " + authToken);
			conn.setRequestProperty("Accept", "application/json");

			DataInputStream dataIn = new DataInputStream(conn.getInputStream());
			String strChunk = "";
			StringBuilder sb = new StringBuilder("");
			while (null != ((strChunk = dataIn.readLine())))
				sb.append(strChunk);

			strRet = sb.toString();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("IOException: " + conn.getHeaderField(0));
		}
		return strRet;
	}

	public String asyncRequest(final String payLoad, String target, String encodedClientIdSecret) throws InterruptedException, ExecutionException {
		AsyncRequest t1 = new AsyncRequest(payLoad, target, encodedClientIdSecret);
		return (String) t1.execute().get();
	}

	private class AsyncRequest extends AsyncTask {
		public String strRet = null;
		private String payLoad = null;
		private String authToken = null;
		private String target=null;
		private String encodedClientIdSecret=null;

		public AsyncRequest(String payLoad, String target, String encodedClientIdSecret) {
			this.payLoad = payLoad;
			this.target=target;
			this.encodedClientIdSecret=encodedClientIdSecret;
		}

		public Object doInBackground(Object... params) {
			this.authToken = 
					getAuthToken(target,
							encodedClientIdSecret);
			URLConnection conn = null;
			try {
				URL urlConn = new URL(payLoad);

				conn = null;
				conn = urlConn.openConnection();

				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);

				conn.setRequestProperty("Authorization", "Bearer " + authToken);
				conn.setRequestProperty("Accept", "application/json");
				DataInputStream dataIn = new DataInputStream(
						conn.getInputStream());
				String strChunk = "";
				StringBuilder sb = new StringBuilder("");
				while (null != ((strChunk = dataIn.readLine())))
					sb.append(strChunk);

				strRet = sb.toString();

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("IOException: " + conn.getHeaderField(0));
			}
			if (conn!=null)
				Log.i("Connection status: " , "");
			return strRet;
		}
	}
}
