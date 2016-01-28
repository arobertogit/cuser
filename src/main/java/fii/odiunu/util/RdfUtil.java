package fii.odiunu.util;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ojrobert on 1/28/2016.
 */
public class RdfUtil {

    public static final boolean DEBUG = true;

    public static String getJson(Model model) {
        StringOutputStream out = new StringOutputStream();
        RDFDataMgr.write(out, model, Lang.JSONLD);
        return out.toString();
    }

    public static String getXml(Model model) {
        StringOutputStream out = new StringOutputStream();
        RDFDataMgr.write(out, model, Lang.RDFXML);
        return out.toString();
    }

    public static String getFileLocation(String realPath, String fileName) {
        Path path = Paths.get(realPath, fileName);
        try {
            if (!Files.exists(path.getParent()))
                Files.createDirectories(path.getParent());
        } catch (IOException e) {
        }
        return path.toString();
    }

}
