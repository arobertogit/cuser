package fii.odiunu.request;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

public class Request {

	public String makeHTTPRequestJokeNorris() {
		JSONObject jsonObj = null;
		String joke;
		try {
			URL url = new URL("http://api.icndb.com/jokes/random/");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {
				jsonObj = (JSONObject) new JSONParser().parse(strTemp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		jsonObj = (JSONObject) jsonObj.get("value");
		joke = (String) jsonObj.get("joke");
		return joke;
	}

	public String makeHTTPRequestJokeMomma() {
		JSONObject jsonObj = null;
		String joke;
		try {
			URL url = new URL("http://api.yomomma.info/");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {
				jsonObj = (JSONObject) new JSONParser().parse(strTemp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		joke = (String) jsonObj.get("joke");
		return joke;
	}

	public String makeHTTPRequestYesOrNo() {
		JSONObject jsonObj = null;
		String yesOrNo;
		try {
			URL url = new URL("http://yesno.wtf/api/");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String strTemp = "";
			while (null != (strTemp = br.readLine())) {
				jsonObj = (JSONObject) new JSONParser().parse(strTemp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		yesOrNo = (String) jsonObj.get("image");
		return yesOrNo;
	}

	public String makeHTTPRequestMusic() {

		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = null;
		String yesOrNo, line = "",linkToSong = "";
		try {
			URL url = new URL("http://api.soundcloud.com/tracks?client_id=ffcaccc2a3bf0998c26d5a980a8b8607");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((line = br.readLine()) != null) {

				String objects[] = line.split("\\},");
				
				for(int i=0;i<objects.length;i++){
					
					objects[i] = objects[i].replace("[", "");
					objects[i] = objects[i] + "}";
					System.out.println(objects[i] + "\n");
					jsonObj = (JSONObject) new JSONParser().parse(objects[i]);
					
					jsonArray.add(jsonObj);
				}
			}
			Random rn = new Random();
			int randomNum = rn.nextInt((jsonArray.size() - 0) + 1) + 0;
			
			jsonObj = (JSONObject) jsonArray.get(randomNum);
			linkToSong = (String) jsonObj.get("permalink_url");
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return linkToSong;
	}

}