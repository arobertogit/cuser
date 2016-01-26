package fii.odiunu.request;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.json.JSONArray;
import org.json.JSONObject;

public class Request {

/*    public String makeHTTPRequestJokeNorris() {
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
        String yesOrNo, line = "", linkToSong = "";
        try {
            URL url = new URL("http://api.soundcloud.com/tracks?client_id=ffcaccc2a3bf0998c26d5a980a8b8607");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = br.readLine()) != null) {

                String objects[] = line.split("\\},");

                for (int i = 0; i < objects.length; i++) {

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
    }*/

    public void readMenuFromRDF(String keyword) {
        Model model = ModelFactory.createDefaultModel();

        //de verificat calea
        model.read("/web/resources/" + keyword + ".nt", "NTRIPLES");

        for (int i = 0; i < 10; i++) {
            Resource video = model.getResource("product" + i);
            Property p1 = model.createProperty("name");
            Property p2 = model.createProperty("ingredients");
            Property p3 = model.createProperty("price");
            Property p4 = model.createProperty("tags");

            // TODO
            System.out.println(video.getProperty(p1).getString());
            System.out.println(video.getProperty(p2).getString());
            System.out.println(video.getProperty(p3).getString());
            System.out.println(video.getProperty(p4).getString());
            /* pass variables to client*/
        }
    }

    public void writeVideosToRDF(String keyword) {

        String api_key = "AIzaSyCZO2nHBNMSGgRg4VHMZ9P8dWT0H23J-Fc";
        String yt_url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="
                + keyword + "&type=video&videoCaption=closedCaption&key=" + api_key + "&format=5&maxResults=10&v=2";
        String line, stringArray;
        StringBuilder stringArrayBuilder = new StringBuilder();

        String titleOfVideo;
        String description;
        String thumbnailURL;
        String videoId;

        Model model = ModelFactory.createDefaultModel();

        try {
            URL url = new URL(yt_url);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = br.readLine()) != null) {
                stringArrayBuilder = stringArrayBuilder.append(line);
            }
            stringArray = stringArrayBuilder.toString();

            JSONObject nodeRoot = new JSONObject(stringArray);
            JSONArray jsonArray = (JSONArray) nodeRoot.get("items");


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                JSONObject snippet = (JSONObject) obj.get("snippet");

                description = (String) snippet.get("description");
                titleOfVideo = (String) snippet.get("title");


                JSONObject thumbnails = (JSONObject) snippet.get("thumbnails");
                JSONObject thumbnail = (JSONObject) thumbnails.get("high");
                thumbnailURL = (String) thumbnail.get("url");

                JSONObject id = (JSONObject) obj.get("id");
                videoId = (String) id.get("videoId");


                Resource video = model.createResource("video" + i);
                Property p1 = model.createProperty("title");
                video.addProperty(p1, titleOfVideo);
                Property p2 = model.createProperty("description");
                video.addProperty(p2, description);
                Property p3 = model.createProperty("thumbnail");
                video.addProperty(p3, thumbnailURL);
                Property p4 = model.createProperty("id");
                video.addProperty(p4, videoId);

            }
            FileOutputStream fos = new FileOutputStream(keyword + ".nt");

            RDFDataMgr.write(fos, model, Lang.NTRIPLES);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void readVideosFromRDF(String keyword) {
        Model model = ModelFactory.createDefaultModel();
        model.read(keyword + ".nt", "NTRIPLES");

        for (int i = 0; i < 10; i++) {
            Resource video = model.getResource("video" + i);
            Property p1 = model.createProperty("title");
            Property p2 = model.createProperty("description");
            Property p3 = model.createProperty("thumbnail");
            Property p4 = model.createProperty("id");

            //use it to send resources to user. p4 (id) contains the id of the video

        }

    }

    public String writeMusicToRDF(String keyword) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj = null;
        String line = "",linkToSong = "";

        String titleOfMusic;
        String description;
        String musicId;
        String musicGenre;
        String musicURL;

        Model model = ModelFactory.createDefaultModel();

        try {
            URL url = new URL("http://api.soundcloud.com/tracks?client_id=ffcaccc2a3bf0998c26d5a980a8b8607&q=" + keyword + "&limit=50");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = br.readLine()) != null) {

                String objects[] = line.split("\\},\\{");

                for(int i=0;i<objects.length;i++){

                    objects[i] = objects[i].replace("[{", "");
                    objects[i] = "{" + objects[i];
                    objects[i] = objects[i] + "}";
                    jsonObj = (JSONObject) new JSONObject(objects[i]);

                    jsonArray.put(jsonObj);

                    try {
                        description = (String) jsonObj.get("description");
                    } catch (Exception ex) {
                        description = "";
                    }
                    titleOfMusic = (String) jsonObj.get("title");
                    musicURL = (String) jsonObj.get("permalink_url");
                    musicId = (String) jsonObj.get("id").toString();
                    try {
                        musicGenre = (String) jsonObj.get("genre");
                    } catch (Exception ex) {
                        musicGenre = "";
                    }


                    Resource music = model.createResource("music"+i);
                    Property p1 = model.createProperty("title");
                    music.addProperty(p1, titleOfMusic);
                    Property p2 = model.createProperty("description");
                    music.addProperty(p2, description);
                    Property p3 = model.createProperty("url");
                    music.addProperty(p3, musicURL);
                    Property p4 = model.createProperty("id");
                    music.addProperty(p4, musicId);
                    Property p5 = model.createProperty("genre");
                    music.addProperty(p5, musicGenre);
                }
            }

            FileOutputStream fos = new FileOutputStream("ms-"+keyword+".nt");

            RDFDataMgr.write(fos, model, Lang.NTRIPLES);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return linkToSong;
    }

    public void readMusicFromRDF(String keyword){
        Model model = ModelFactory.createDefaultModel();
        model.read("ms-"+keyword+".nt", "NTRIPLES");

        for (int i = 0; i < 10; i++) {
            Resource music = model.getResource("music" + i);
            Property p1 = model.createProperty("title");
            Property p2 = model.createProperty("description");
            Property p3 = model.createProperty("url");
            Property p4 = model.createProperty("id");
            Property p5 = model.createProperty("genre");


        }

    }
}