package com.sabre.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class DSCommHandler {

    public String getAuthToken(String apiEndPoint, String encodedCliAndSecret){
      //receives : apiEndPoint (https://api.test.sabre.com)
      //sabre//encodedCliAndSecret : base64Encode(  base64Encode(V1:[user]:[group]:[domain]) + ":" + base64Encode([secret]) )
      String strRet = null;

      try {

        URL urlConn = new URL(apiEndPoint + "/v1/auth/token");
        URLConnection conn=urlConn.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", "Basic " + encodedCliAndSecret);
        conn.setRequestProperty("Accept", "application/json");
        DataOutputStream dataOut = new DataOutputStream(conn.getOutputStream());
        dataOut.writeBytes("grant_type=client_credentials");
        dataOut.flush();
        dataOut.close();

        //get response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String strChunk="";
        StringBuilder sb = new StringBuilder();
        while(null != ((strChunk=rd.readLine())))
          sb.append(strChunk);

        //parse the token
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
  public String sendRequest(String payLoad,String authToken){
    URLConnection conn=null;
    String strRet=null;
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
      String strChunk="";
      StringBuilder sb = new StringBuilder("");
      while(null != ((strChunk = dataIn.readLine())))
        sb.append(strChunk);


      strRet = sb.toString();


    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.out.println("IOException: "+conn.getHeaderField(0));
    }
    return strRet;
  }
}
