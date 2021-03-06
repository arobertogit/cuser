package fii.odiunu.request;

import com.google.common.collect.Sets;
import fii.odiunu.util.RdfUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fii.odiunu.util.RdfUtil.getFileLocation;
import static fii.odiunu.util.RdfUtil.getJson;
import static fii.odiunu.util.RdfUtil.getXml;

@Service
public class RdfLocalManager implements RdfManager {

    @Deprecated
    @Override
    public String writeMusicToRDF(String keyword, String realPath) {

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        String line, linkToSong = "";

        String titleOfMusic;
        String description;
        String musicId;
        String musicGenre;
        String musicURL;

        Model model = ModelFactory.createDefaultModel();

        String nsPrefix = "https://schema.org/AudioObject#";

        Property p1 = model.createProperty(nsPrefix, "title");
        Property p2 = model.createProperty(nsPrefix, "description");
        Property p3 = model.createProperty(nsPrefix, "url");
        Property p4 = model.createProperty(nsPrefix, "id");
        Property p5 = model.createProperty(nsPrefix, "genre");
        model.setNsPrefix("music", nsPrefix);

        try {
            URL url = new URL("http://api.soundcloud.com/tracks?client_id=ffcaccc2a3bf0998c26d5a980a8b8607&q=" + keyword + "&limit=50");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = br.readLine()) != null) {

                String objects[] = line.split("\\},\\{");

                for (int i = 0; i < objects.length; i++) {

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

                    Resource music = model.createResource(nsPrefix + "music" + i);
                    music.addProperty(p1, titleOfMusic, XSDDatatype.XSDstring);
                    music.addProperty(p2, description, XSDDatatype.XSDstring);
                    music.addProperty(p3, musicURL, XSDDatatype.XSDanyURI);
                    music.addProperty(p4, musicId, XSDDatatype.XSDpositiveInteger);
                    music.addProperty(p5, musicGenre, XSDDatatype.XSDstring);
                }
            }

            String fileName = "ms-" + keyword + ".xml";
            FileWriter out = new FileWriter(getFileLocation(realPath, fileName));
            model.write(out, "RDF/XML");
            return getXml(model);

        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }
    }

    @Deprecated
    @Override
    public String readMusicFromRDF(String keyword, String realPath) {
        try {
            Model model = ModelFactory.createDefaultModel();
            String fileName = "resources.xml";
            model.read(getFileLocation(realPath, fileName), "RDF/XML");

            return getXml(model);
        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }
    }

    @Override
    @Deprecated
    public String writeVideosToRDF(String keyword, String realPath) {

        String api_key = "AIzaSyCZO2nHBNMSGgRg4VHMZ9P8dWT0H23J-Fc";
        String yt_url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="
                + keyword + "&type=video&videoCaption=closedCaption&key=" + api_key + "&format=5&maxResults=10&v=2";
        String line = "", stringArray;
        StringBuilder stringArrayBuilder = new StringBuilder();

        String titleOfVideo;
        String description;
        String thumbnailURL;
        String videoId;

        Model model = ModelFactory.createDefaultModel();

        String nsPrefix = "https://schema.org/VideoObject#";

        Property p1 = model.createProperty(nsPrefix, "title");
        Property p2 = model.createProperty(nsPrefix, "description");
        Property p3 = model.createProperty(nsPrefix, "thumbnail");
        Property p4 = model.createProperty(nsPrefix, "id");
        model.setNsPrefix("video", nsPrefix);

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


                Resource video = model.createResource(nsPrefix + "video" + i);
                video.addProperty(p1, titleOfVideo, XSDDatatype.XSDstring);
                video.addProperty(p2, description, XSDDatatype.XSDstring);
                video.addProperty(p3, thumbnailURL, XSDDatatype.XSDanyURI);
                video.addProperty(p4, videoId, XSDDatatype.XSDstring);

            }

            String fileName = keyword + ".xml";
            FileWriter out = new FileWriter(getFileLocation(realPath, fileName));
            model.write(out, "RDF/XML");
            return getXml(model);
        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }

    }

    @Override
    @Deprecated
    public String readVideosFromRDF(String keyword, String realPath) {
        try {
            Model model = ModelFactory.createDefaultModel();
            String fileName = "resources.xml";
            model.read(getFileLocation(realPath, fileName), "RDF/XML");

            return getXml(model);
        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }
    }

    @Deprecated
    @Override
    public String saveNewMenuToRDF(String keyword, String realPath) {
        Set<String> resources = Sets.newHashSet();
        Set<String> comments = Sets.newHashSet();

        Model model = ModelFactory.createDefaultModel();

        String nsPrefix = "http://dbpedia.org/ontology/Food#";

        Property p1 = model.createProperty(nsPrefix, "name");
        Property p2 = model.createProperty(nsPrefix, "abstract");
        model.setNsPrefix("food", nsPrefix);

        try {
            // query for new list from a given country
            final String dbpedia = "http://dbpedia.org/sparql";
            String q = "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                    "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
                    "PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
                    "PREFIX : <http://dbpedia.org/resource/>" +
                    "PREFIX dbpedia2: <http://dbpedia.org/property/>" +
                    "PREFIX dbpedia: <http://dbpedia.org/>" +
                    "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" +
                    "PREFIX dbp: <http://dbpedia.org/property/>" +
                    "SELECT DISTINCT ?subject WHERE {" +
                    "?subject dbp:country ?label;" +
                    "rdf:type ?type."
                    + "FILTER ( regex (?label,\"^" + keyword + "$\", \"i\") && regex (?type,\"Food\", \"i\"))"
                    + "}"
                    + "limit 10";


            Query query = QueryFactory.create(q);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(dbpedia, query);
            ResultSet results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution qs = results.next();
                RDFNode s = qs.get("subject");
                if (s.isResource()) {
                    String uri = s.asResource().getURI();
                    resources.add(uri);
                }
            }


            for (String i : resources) {
                // get the food description
                q = "PREFIX owl: <http://www.w3.org/2002/07/owl#>" +
                        "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
                        "PREFIX dc: <http://purl.org/dc/elements/1.1/>" +
                        "PREFIX : <http://dbpedia.org/resource/>" +
                        "PREFIX dbpedia2: <http://dbpedia.org/property/>" +
                        "PREFIX dbpedia: <http://dbpedia.org/>" +
                        "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>" +
                        "PREFIX dbp: <http://dbpedia.org/property/>" +
                        "SELECT ?hasValue " +
                        "WHERE {" +
                        "   <" + i + "> ?property ?hasValue" +
                        "  FILTER (lang(?hasValue) = 'en' && regex (?property,\"abstract$\", \"i\"))" +
                        "}";

                query = QueryFactory.create(q);
                qexec = QueryExecutionFactory.sparqlService(dbpedia, query);
                results = qexec.execSelect();

                while (results.hasNext()) {
                    QuerySolution qs = results.next();
                    RDFNode s = qs.get("hasValue");
                    if (s.isLiteral()) {
                        String comment = s.toString();
                        comments.add(comment);

                        Resource music = model.createResource(nsPrefix + i.substring(28));
                        music.addProperty(p1, i, XSDDatatype.XSDstring);
                        music.addProperty(p2, comment, XSDDatatype.XSDstring);
                    }
                }

            }

            String fileName = "menu-" + keyword + ".xml";
            FileWriter out = new FileWriter(getFileLocation(realPath, fileName));
            model.write(out, "RDF/XML");
            return getXml(model);
        } catch (Exception ex) {
            throw new RuntimeException("");
        }
    }

    @Deprecated
    @Override
    public String readNewMenuFromRDF(String keyword, String realPath) {
        try {
            Model model = ModelFactory.createDefaultModel();
            String fileName = "menu-" + keyword + ".xml";
            model.read(getFileLocation(realPath, fileName), "RDF/XML");
            return getXml(model);
        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }
    }

    @Override
    public void writeMusicToFuseki(String country, String time, String type) {

        if (!StringUtils.isAlphanumeric(country) || !StringUtils.isAlphanumeric(time) || !StringUtils.isAlphanumeric(type))
            throw new RuntimeException("only alphanumeric values are allowed");

        String genre = "";
        switch (type) {
            case "business":
                switch (time) {
                    case "breakfast":
                        genre = "&genre=classical";
                        break;
                    case "lunch":
                        genre = "&genre=opera";
                        break;
                    case "dinner":
                        genre = "&genre=choral";
                        break;
                }
                break;
            case "family":
                switch (time) {
                    case "breakfast":
                        genre = "&genres=blues";
                        break;
                    case "lunch":
                        genre = "&genres=country";
                        break;
                    case "dinner":
                        genre = "&genres=pop";
                        break;
                }
                break;
            case "date":
                switch (time) {
                    case "breakfast":
                        genre = "&genres=blues";
                        break;
                    case "lunch":
                        genre = "&genres=jazz";
                        break;
                    case "dinner":
                        genre = "&genres=tango";
                        break;
                }
                break;
            case "single":
                switch (time) {
                    case "breakfast":
                        genre = "&genres=pop";
                        break;
                    case "lunch":
                        genre = "&genres=hip hop";
                        break;
                    case "dinner":
                        genre = "&genres=dance";
                        break;
                }
                break;
        }

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObj;
        String line, linkToSong = "";

        String titleOfMusic;
        String description;
        String musicId;
        String musicGenre;
        String musicURL;
        String vcountry = country;
        String vtime = time;
        String vtype = type;

        Model model = ModelFactory.createDefaultModel();

        String nsPrefix = "https://schema.org/AudioObject#";

        Property p1 = model.createProperty(nsPrefix, "title");
        Property p2 = model.createProperty(nsPrefix, "description");
        Property p3 = model.createProperty(nsPrefix, "url");
        Property p4 = model.createProperty(nsPrefix, "id");
        Property p5 = model.createProperty(nsPrefix, "genre");

        Property p6 = model.createProperty(nsPrefix, "country");
        Property p7 = model.createProperty(nsPrefix, "time");
        Property p8 = model.createProperty(nsPrefix, "type");
        model.setNsPrefix("music", nsPrefix);

        try {
            URL url = new URL("http://api.soundcloud.com/tracks?client_id=ffcaccc2a3bf0998c26d5a980a8b8607&limit=50" + genre);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = br.readLine()) != null) {

                String objects[] = line.split("\\},\\{");

                for (int i = 0; i < objects.length; i++) {

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

                    Resource music = model.createResource(nsPrefix + musicURL.substring(23));
                    music.addProperty(p1, titleOfMusic, XSDDatatype.XSDstring);
                    music.addProperty(p2, description, XSDDatatype.XSDstring);
                    music.addProperty(p3, musicURL, XSDDatatype.XSDanyURI);
                    music.addProperty(p4, musicId, XSDDatatype.XSDpositiveInteger);
                    music.addProperty(p5, musicGenre, XSDDatatype.XSDstring);
                    music.addProperty(p6, vcountry, XSDDatatype.XSDstring);
                    music.addProperty(p7, vtime, XSDDatatype.XSDstring);
                    music.addProperty(p8, vtype, XSDDatatype.XSDstring);

                    String UPDATE_Query =
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                                    "PREFIX m: <https://schema.org/AudioObject#> "
                                    + "INSERT DATA"
                                    + "{ <" + nsPrefix + musicURL.substring(23) + ">    m:title    \"" + titleOfMusic.replaceAll("\r", "").replaceAll("\"", "").replaceAll("\n", "") + "\" ;"
                                    + "                         m:description  \"" + description.replaceAll("\r", "").replaceAll("\"", "").replaceAll("\n", "") + "\" ;"
                                    + "								m:url    \"" + musicURL + "\" ;"
                                    + "								m:id    \"" + musicId + "\" ;"
                                    + "								m:genre    \"" + musicGenre + "\" ;"
                                    + "								m:country    \"" + vcountry + "\" ;"
                                    + "								m:time    \"" + vtime + "\" ;"
                                    + "								m:type    \"" + vtype + "\" ;"
                                    + "." + "}   ";
                    System.out.println(UPDATE_Query);
                    UpdateProcessor upp = UpdateExecutionFactory.createRemote(
                            UpdateFactory.create(UPDATE_Query),
                            "http://localhost:3030/resources/update");
                    upp.execute();

                }
            }

        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }
    }

    @Override
    public String readMusicFromFuseki(String country, String time, String type) {

        if (!StringUtils.isAlphanumeric(country) || !StringUtils.isAlphanumeric(time) || !StringUtils.isAlphanumeric(type))
            throw new RuntimeException("only alphanumeric values are allowed");

        final String resourceLink = "http://localhost:3030/resources/query";
        String q = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                "PREFIX : <https://schema.org/AudioObject#> " +
                "SELECT ?id WHERE {" +
                " { ?id :type \"" + type + "\" }" +
                " { ?id :time \"" + time + "\" }" +
                " { ?id :country \"" + country + "\" }" +
                "}" +
                "LIMIT 50";

        String nsPrefix = "https://schema.org/VideoObject#";

        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(resourceLink, query);
        ResultSet results = qexec.execSelect();

        Model model = ModelFactory.createDefaultModel();

        while (results.hasNext()) {
            QuerySolution qs = results.next();
            RDFNode s = qs.get("id");
            if (s.isResource()) {

                String idValue = s.asResource().getURI().substring(31);
                Resource resource = model.createResource();
                Property p1 = model.createProperty(nsPrefix, "id");
                resource.addProperty(p1, idValue);
            }
        }
        model.setNsPrefix("music", nsPrefix);

        return getJson(model);

    }

    @Override
    public void writeVideosToFuseki(String country, String time, String type) {

        if (!StringUtils.isAlphanumeric(country) || !StringUtils.isAlphanumeric(time) || !StringUtils.isAlphanumeric(type))
            throw new RuntimeException("only alphanumeric values are allowed");

        String location = "";
        switch (country) {
            case "Germany":
                location = "&location=37.42307%2C+-13.404953999999975&locationRadius=500km";
                break;
            case "Italy":
                location = "&location=41.8724111%2C+-12.48022489999994&locationRadius=500km";
                break;
            case "Spain":
                location = "&location=40.4167754%2C+-3.7037901999999576&locationRadius=500km";
                break;
            case "Paris":
                location = "&location=48.85661400000001%2C+2.3522219000000177&locationRadius=500km";
                break;
            case "Romania":
                location = "&location=44.4267674%2C+26.102538399999958&locationRadius=500km";
                break;
        }

        String queryType = "";
        String videoDefinition = "";
        String queryTerm = "";

        switch (type) {
            case "business": {
                queryType = "&safeSearch=strict";
                videoDefinition = "&videoDefinition=high";
                queryTerm = "office|boss|work|respect|devotion";
                break;
            }
            case "date": {
                queryType = "&safeSearch=moderate";
                videoDefinition = "&videoDefinition=standard";
                queryTerm = "love|romance|affection|tenderness";
                break;
            }
            case "family": {
                queryType = "&safeSearch=strict";
                videoDefinition = "&videoDefinition=any";
                queryTerm = "hope|faith|happiness";
                break;
            }
            default: {
                queryType = "&safeSearch=none";
                videoDefinition = "&videoDefinition=any";
                queryTerm = "curiosities|random facts|fun videos";
                break;
            }
        }

        String api_key = "AIzaSyCZO2nHBNMSGgRg4VHMZ9P8dWT0H23J-Fc";
        String yt_url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q="
                + "+" + queryTerm + queryType + location + "&type=video&videoCaption=closedCaption&key=" + api_key + "&format=5&maxResults=10&v=2" + videoDefinition;
        String line = "", stringArray;
        StringBuilder stringArrayBuilder = new StringBuilder();

        String titleOfVideo;
        String description;
        String thumbnailURL;
        String videoId;
        String vcountry = country;
        String vtime = time;
        String vtype = type;

        Model model = ModelFactory.createDefaultModel();
        String nsPrefix = "https://schema.org/VideoObject#";

        Property p1 = model.createProperty(nsPrefix, "title");
        Property p2 = model.createProperty(nsPrefix, "description");
        Property p3 = model.createProperty(nsPrefix, "thumbnail");
        Property p4 = model.createProperty(nsPrefix, "id");

        Property p5 = model.createProperty(nsPrefix, "country");
        Property p6 = model.createProperty(nsPrefix, "time");
        Property p7 = model.createProperty(nsPrefix, "type");
        model.setNsPrefix("video", nsPrefix);

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


                Resource video = model.createResource(nsPrefix + videoId);
                video.addProperty(p1, titleOfVideo, XSDDatatype.XSDstring);
                video.addProperty(p2, description, XSDDatatype.XSDstring);
                video.addProperty(p3, thumbnailURL, XSDDatatype.XSDanyURI);
                video.addProperty(p4, videoId, XSDDatatype.XSDstring);
                video.addProperty(p5, vcountry, XSDDatatype.XSDstring);
                video.addProperty(p6, vtime, XSDDatatype.XSDstring);
                video.addProperty(p7, vtype, XSDDatatype.XSDstring);

                String UPDATE_Query =
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                                "PREFIX v: <https://schema.org/VideoObject#> "
                                + "INSERT DATA"
                                + "{ <" + nsPrefix + videoId + ">    v:title    \"" + titleOfVideo.replace("\"", "") + "\" ;"
                                + "                             v:description  \"" + description.replace("\"", "") + "\" ;"
                                + "								v:thumbnail    \"" + thumbnailURL + "\" ;"
                                + "								v:id    \"" + videoId + "\" ;"
                                + "								v:country    \"" + vcountry + "\" ;"
                                + "								v:time    \"" + vtime + "\" ;"
                                + "								v:type    \"" + vtype + "\" ;"
                                + "." + "}   ";

                UpdateProcessor upp = UpdateExecutionFactory.createRemote(
                        UpdateFactory.create(UPDATE_Query),
                        "http://localhost:3030/resources/update");
                upp.execute();

            }


        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }

    }

    @Override
    public String readVideosFromFuseki(String country, String time, String type) {

        if (!StringUtils.isAlphanumeric(country) || !StringUtils.isAlphanumeric(time) || !StringUtils.isAlphanumeric(type))
            throw new RuntimeException("only alphanumeric values are allowed");

        final String resourceLink = "http://localhost:3030/resources/query";
        String q = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                "PREFIX : <https://schema.org/VideoObject#> " +
                "SELECT ?id WHERE {" +
                " { ?id :type \"" + type + "\" }" +
                " { ?id :time \"" + time + "\" }" +
                " { ?id :country \"" + country + "\" }" +
                "}" +
                "LIMIT 50";

        String nsPrefix = "https://schema.org/VideoObject#";

        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(resourceLink, query);
        ResultSet results = qexec.execSelect();

        Model model = ModelFactory.createDefaultModel();

        model.setNsPrefix("video", nsPrefix);

        while (results.hasNext()) {
            QuerySolution qs = results.next();
            RDFNode s = qs.get("id");
            if (s.isResource()) {
                String idValue = s.asResource().getURI().substring(31).replace("video", "");
                Resource resource = model.createResource();
                Property p1 = model.createProperty(nsPrefix, "id");
                resource.addProperty(p1, idValue);
            }
        }

        return getJson(model);

    }

    @Override
    public void writeMenuToFuseki(String id, String arrivalTime, String servingTime, String menuType, String title, String picture,
                                  String description, String ingredientList, String country) {

        String nsPrefix = "https://schema.org/Recipe#";


        String UPDATE_Query =
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
                        "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" +
                        "PREFIX r: <https://schema.org/Recipe#> "
                        + "INSERT DATA"
                        + "{ <" + nsPrefix + id + ">    r:id    \"" + id + "\" ;"
                        + "                         r:arrivalTime  \"" + arrivalTime + "\" ;"
                        + "								r:servingTime    \"" + servingTime + "\" ;"
                        + "								r:menuType    \"" + menuType + "\" ;"
                        + "								r:title    \"" + title + "\" ;"
                        + "								r:picture    \"" + picture + "\" ;"
                        + "								r:description    \"" + description + "\" ;"
                        + "								r:ingredientList    \"" + ingredientList + "\" ;"
                        + "						r:country    \"" + country + "\" ;"
                        + "." + "}   ";

        UpdateProcessor upp = UpdateExecutionFactory.createRemote(
                UpdateFactory.create(UPDATE_Query),
                "http://localhost:3030/resources/update");
        upp.execute();

    }

    //TODO
    @Override
    public String readMenuFromFuseki() {

        Set<String> ids = new HashSet();

        final String resourceLink = "http://localhost:3030/resources/query";
        String q = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX : <https://schema.org/Recipe#> " +
                "SELECT ?c WHERE {" +
                " { ?a :id ?c " +
                " }" +
                "}" +
                "LIMIT 50";

        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(resourceLink, query);
        ResultSet results = qexec.execSelect();

        while (results.hasNext()) {
            QuerySolution qs = results.next();
            RDFNode s;
            s = qs.get("c");
            String foodId = "";
            if (s.isLiteral()) {
                foodId = s.asLiteral().getString();
            }
            ids.add(foodId);
        }

        Model model = ModelFactory.createDefaultModel();
        String nsPrefix = "https://schema.org/Recipe#";
        model.setNsPrefix("recipe", nsPrefix);


        Property p1 = model.createProperty(nsPrefix, "id");
        Property p2 = model.createProperty(nsPrefix, "arrivalTime");
        Property p3 = model.createProperty(nsPrefix, "servingTime");
        Property p4 = model.createProperty(nsPrefix, "menuType");
        Property p5 = model.createProperty(nsPrefix, "title");

        Property p6 = model.createProperty(nsPrefix, "picture");
        Property p7 = model.createProperty(nsPrefix, "description");
        Property p8 = model.createProperty(nsPrefix, "ingredientList");
        Property p9 = model.createProperty(nsPrefix, "country");


        for (String id : ids) {
            final String resourceLink2 = "http://localhost:3030/resources/query";
            q = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                    "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                    "PREFIX : <https://schema.org/Recipe#> " +
                    "SELECT ?b ?c WHERE {" +
                    " { <https://schema.org/Recipe#" + id + "> ?b ?c " +
                    " }" +
                    "}" +
                    "LIMIT 50";

            query = QueryFactory.create(q);
            qexec = QueryExecutionFactory.sparqlService(resourceLink2, query);
            results = qexec.execSelect();

            while (results.hasNext()) {
                QuerySolution qs = results.next();
                RDFNode s;
                s = qs.get("c");
                String menuList = "";
                if (s.isLiteral()) {
                    menuList = s.asLiteral().getString();
                }
                s = qs.get("b");
                String menuType = "";
                if (s.isResource()) {
                    menuType = s.asResource().getURI();
                }

                Resource food = model.createResource(nsPrefix + id);

                switch (menuType) {
                    case "https://schema.org/Recipe#id":
                        food.addProperty(p1, menuList, XSDDatatype.XSDstring);
                        break;
                    case "https://schema.org/Recipe#arrivalTime":
                        food.addProperty(p2, menuList, XSDDatatype.XSDstring);
                        break;
                    case "https://schema.org/Recipe#servingTime":
                        food.addProperty(p3, menuList, XSDDatatype.XSDstring);
                        break;
                    case "https://schema.org/Recipe#menuType":
                        food.addProperty(p4, menuList, XSDDatatype.XSDstring);
                        break;
                    case "https://schema.org/Recipe#title":
                        food.addProperty(p5, menuList, XSDDatatype.XSDstring);
                        break;
                    case "https://schema.org/Recipe#picture":
                        food.addProperty(p6, menuList, XSDDatatype.XSDstring);
                        break;
                    case "https://schema.org/Recipe#description":
                        food.addProperty(p7, menuList, XSDDatatype.XSDstring);
                        break;
                    case "https://schema.org/Recipe#ingredientList":
                        food.addProperty(p8, menuList, XSDDatatype.XSDstring);
                        break;
                    case "https://schema.org/Recipe#country":
                        food.addProperty(p9, menuList, XSDDatatype.XSDstring);
                        break;
                }

            }
        }

        return getJson(model);
    }

    @Override
    public String searchInMenu(String id) {

        if (!StringUtils.isAlphanumeric(id))
            throw new RuntimeException("only alphanumeric values are allowed");

        Model model = ModelFactory.createDefaultModel();

        final String resourceLink = "http://localhost:3030/resources/query";
        String q = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX : <https://schema.org/Recipe#> " +
                "SELECT ?b ?c WHERE {" +
                " { :" + id + " ?b ?c " +
                " }" +
                "}" +
                "LIMIT 50";

        String nsPrefix = "https://schema.org/Recipe#";
        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(resourceLink, query);
        ResultSet results = qexec.execSelect();

        while (results.hasNext()) {
            QuerySolution qs = results.next();
            RDFNode s;
            s = qs.get("c");
            String menuList = "";
            if (s.isLiteral()) {
                menuList = s.asLiteral().getString();
            }
            s = qs.get("b");
            String menuType = "";
            if (s.isResource()) {
                menuType = s.asResource().getURI();
            }
            String value = menuType.substring(26) + ":" + menuList;
            Resource menuItem = model.createResource();
            Property p1 = model.createProperty(nsPrefix);
            menuItem.addProperty(p1, value);

        }

        model.setNsPrefix("menu", nsPrefix);
        return getJson(model);

    }

    @Override
    public String searchInMenu(String criteria, String toSearch) {

        if (!StringUtils.isAlphanumeric(criteria) || !StringUtils.isAlphanumeric(toSearch))
            throw new RuntimeException("only alphanumeric values are allowed");

        Model model = ModelFactory.createDefaultModel();

        final String resourceLink = "http://localhost:3030/resources/query";
        String q = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "PREFIX : <https://schema.org/Recipe#> " +
                "SELECT ?a WHERE {" +
                " { ?a :" + criteria + " ?c " +
                "FILTER ( regex (?c,\"" + toSearch + "\", \"i\") )" +
                " }" +
                "}" +
                "LIMIT 50";

        Query query = QueryFactory.create(q);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(resourceLink, query);
        ResultSet results = qexec.execSelect();
        String nsPrefix = "https://schema.org/Recipe#";

        while (results.hasNext()) {
            QuerySolution qs = results.next();
            RDFNode s = qs.get("a");
            if (s.isResource()) {
                String menuId = s.asResource().getURI().substring(26);

                Resource menuItem = model.createResource();
                Property p1 = model.createProperty(nsPrefix + "prop");
                menuItem.addProperty(p1, menuId);
            }
        }

        model.setNsPrefix("menu", nsPrefix);
        return getJson(model);

    }

}