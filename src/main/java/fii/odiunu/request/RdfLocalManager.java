package fii.odiunu.request;

import com.google.common.collect.Sets;
import fii.odiunu.util.RdfUtil;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static fii.odiunu.util.RdfUtil.getFileLocation;
import static fii.odiunu.util.RdfUtil.getJson;
import static fii.odiunu.util.RdfUtil.getXml;

@Service
public class RdfLocalManager implements RdfManager {

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

    @Override
    public String readMusicFromRDF(String keyword, String realPath) {
        try {
            Model model = ModelFactory.createDefaultModel();
            String fileName = "ms-" + keyword + ".xml";
            model.read(getFileLocation(realPath, fileName), "RDF/XML");

            return getXml(model);
        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }
    }

    @Override
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
    public String readVideosFromRDF(String keyword, String realPath) {
        try {
            Model model = ModelFactory.createDefaultModel();
            String fileName = keyword + ".xml";
            model.read(getFileLocation(realPath, fileName), "RDF/XML");

            return getXml(model);
        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }
    }

    @Override
    public String saveNewPossibleMenuItemsToRDF(String keyword, String realPath) {
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

    @Override
    public String readNewPossibleMenuFromRDF(String keyword, String realPath) {
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

    public List<String> getMenus(String realPath) {
        try {
            Stream<Path> list = Files.list(Paths.get(realPath));
            List<String> collect = list.map(x -> x.getFileName().toString()).filter((x) -> x.startsWith("menu")).map(x -> x.replace("menu-", "").replace(".xml", "")).collect(Collectors.toList());
            return collect;
        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }
    }

    public String getMenuToHTML(String keyword, String realPath){
        try{
            Model model = ModelFactory.createDefaultModel();
            String fileName = "menu-" + keyword + ".xml";
            model.read(getFileLocation(realPath, fileName), "RDF/XML");
            return getJson(model);
        } catch (Exception ex) {
            String message = RdfUtil.DEBUG ? ex.getMessage() : "";
            throw new RuntimeException(message);
        }

    }
}