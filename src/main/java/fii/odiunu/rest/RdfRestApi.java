package fii.odiunu.rest;

import fii.odiunu.request.RdfLocalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import java.util.List;

/**
 * Created by ojrobert on 1/26/2016.
 */
@RestController
public class RdfRestApi {
    @Autowired
    private RdfLocalManager rdfLocalManager;
    @Autowired
    private ServletContext context;

    @RequestMapping("/Test")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name + "!";
    }

    @RequestMapping(value = "/rdf/getMusic", produces = "application/xml")
    @ResponseBody
    public ResponseEntity<String> getAudio(@RequestParam(value = "keyword", defaultValue = "romanian") String keyword) {
        return getResponseEntity(keyword, rdfLocalManager::readMusicFromRDF);
    }

    private ResponseEntity<String> getResponseEntity(String keyword, RdfSupplier<String, String, String> supplier) {
        try {
            String result = supplier.get(keyword, getRealPath());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String getRealPath() {
        return context.getRealPath("/resources");
    }


    @RequestMapping(value = "/rdf/getVideo", produces = "application/xml")
    @ResponseBody
    public ResponseEntity<String> getVideo(@RequestParam(value = "keyword", defaultValue = "romanian") String keyword) {
        return getResponseEntity(keyword, rdfLocalManager::readVideosFromRDF);
    }

    @RequestMapping(value = "/rdf/getMenu", produces = "application/xml")
    @ResponseBody
    public ResponseEntity<String> getMenu(@RequestParam(value = "keyword", defaultValue = "romanian") String keyword) {
        return getResponseEntity(keyword, rdfLocalManager::readNewPossibleMenuFromRDF);
    }

    @RequestMapping(value = "/rdf/writeMusic", produces = "application/xml")
    @ResponseBody
    public ResponseEntity<String> writeMusic(@RequestParam(value = "keyword", defaultValue = "romanian") String keyword) {
        return getResponseEntity(keyword, rdfLocalManager::writeMusicToRDF);
    }

    @RequestMapping(value = "/rdf/writeVideo", produces = "application/xml")
    @ResponseBody
    public ResponseEntity<String> writeVideo(@RequestParam(value = "keyword", defaultValue = "romanian") String keyword) {
        return getResponseEntity(keyword, rdfLocalManager::writeVideosToRDF);
    }

    @RequestMapping(value = "/rdf/writeMenu", produces = "application/xml")
    @ResponseBody
    public ResponseEntity<String> writeMenu(@RequestParam(value = "keyword", defaultValue = "romanian") String keyword) {
        return getResponseEntity(keyword, rdfLocalManager::saveNewPossibleMenuItemsToRDF);
    }

    @RequestMapping(value = "/rdf/getMenuToHTML", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getMenuToHTML(@RequestParam(value = "keyword", defaultValue = "romanian") String keyword) {
        return getResponseEntity(keyword, rdfLocalManager::getMenuToHTML);
    }

/*  @RequestMapping(value = "/rdf/getMusicLinks", produces = "application/json")
    @ResponseBody
    public List<String> getMusicLinks(@RequestParam(value = "keyword", defaultValue = "romanian") String keyword) {
        return rdfLocalManager.getMusicLinks(keyword, getRealPath());
    }*/


    @FunctionalInterface
    interface RdfSupplier<T, K, V> {

        T get(K k, V v) throws Exception;
    }
}
