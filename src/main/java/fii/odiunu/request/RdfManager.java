package fii.odiunu.request;

/**
 * Created by ojrobert on 1/28/2016.
 */
public interface RdfManager {
    String writeMusicToRDF(String keyword, String realPath);

    String readMusicFromRDF(String keyword, String realPath);

    String writeVideosToRDF(String keyword, String realPath);

    String readVideosFromRDF(String keyword, String realPath);

    String saveNewMenuToRDF(String keyword, String realPath);

    String readNewMenuFromRDF(String keyword, String realPath);

    void writeMusicToFuseki(String country, String time, String type);

    String readMusicFromFuseki(java.lang.String country, java.lang.String time, java.lang.String type);

    void writeVideosToFuseki(String country, String time, String type);

    String readVideosFromFuseki(String country, String time, String type);

    void writeMenuToFuseki(String id, String arrivalTime,
                           String servingTime,
                           String menuType,
                           String title,
                           String picture,
                           String description,
                           String ingredientList,
                           String country);

    String searchInMenu(String id);

    String searchInMenu(String criteria, String toSearch);

    String readMenuFromFuseki();

}
