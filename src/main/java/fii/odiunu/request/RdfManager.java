package fii.odiunu.request;

import java.util.List;
import java.util.Set;

/**
 * Created by ojrobert on 1/28/2016.
 */
public interface RdfManager {
    String writeMusicToRDF(String keyword, String realPath);

    String readMusicFromRDF(String keyword, String realPath);

    String writeVideosToRDF(String keyword, String realPath);

    String readVideosFromRDF(String keyword, String realPath);

    String saveNewPossibleMenuItemsToRDF(String keyword, String realPath);

    String readNewPossibleMenuFromRDF(String keyword, String realPath);

    String readMusicFromFuseki(java.lang.String country, java.lang.String time, java.lang.String type);

    void writeMusicToFuseki(String country, String time, String type);

    void writeVideosToRDFFuseki(String country, String time, String type);

    Set<String> readVideosFromFuseki(String country, String time, String type);

    List<String> getMenus(String realPath);

    String getMenuToHTML(String keyword, String realPath);

}
