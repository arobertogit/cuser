package fii.odiunu.request;

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
}
