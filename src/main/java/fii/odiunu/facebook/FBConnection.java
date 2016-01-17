package fii.odiunu.facebook;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class FBConnection {
	public static final String FB_APP_ID = "430639803797405";
	public static final String REDIRECT_URI = "http://localhost:8080/CuSEr/Main";

	static String accessToken = "";

	public String getFBAuthUrl() {
		String fbLoginUrl = "";
		try {
			fbLoginUrl = "http://www.facebook.com/dialog/oauth?" + "client_id="
					+ FBConnection.FB_APP_ID + "&redirect_uri="
					+ URLEncoder.encode(FBConnection.REDIRECT_URI, "UTF-8")
					+ "&scope=email"
					+ "&response_type=token";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return fbLoginUrl;
	}

}