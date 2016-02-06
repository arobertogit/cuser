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
import java.util.Set;

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


    private ResponseEntity<String> getResponseEntity(String keyword, RdfSupplier<String, String> supplier) {
        try {
            String result = supplier.get(keyword);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<String> getResponseEntity(String country, String time, String type, RdfResourcesSupplier<String, String, String, String> supplier) {
        try {
            String result = supplier.get(country, time, type);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<String> getResponseEntity(String criteria, String value, RdfMenuSupplier<String, String, String> supplier) {
        try {
            String result = supplier.get(criteria, value);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<String> getResponseEntity(RdfSupplierToHTML<String> supplier) {
        try {
            String result = supplier.get();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private String getRealPath() {
        return context.getRealPath("/resources");
    }

    @RequestMapping(value = "/rdf/getMusicLinks", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getMusicLinks(@RequestParam(value = "country", defaultValue = "romania") String country, @RequestParam(value = "time", defaultValue = "morning") String time, @RequestParam(value = "type", defaultValue = "single") String type) {
        rdfLocalManager.writeMusicToFuseki(country,time,type);
        return getResponseEntity(country, time, type, rdfLocalManager::readMusicFromFuseki);
    }

    @RequestMapping(value = "/rdf/getVideoLinks", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getVideoLinks(@RequestParam(value = "country", defaultValue = "romania") String country, @RequestParam(value = "time", defaultValue = "morning") String time, @RequestParam(value = "type", defaultValue = "single") String type) {
        rdfLocalManager.writeVideosToFuseki(country,time,type);
        return getResponseEntity(country, time, type, rdfLocalManager::readVideosFromFuseki);
    }


    @RequestMapping(value = "/rdf/getTypeOfFoodToByID", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getTypeOfFoodToByID (@RequestParam(value = "id", defaultValue = "0") String id) {
        return getResponseEntity(id,rdfLocalManager::searchInMenu);
    }

    @RequestMapping(value = "/rdf/getTypeOfFoodByCriteria", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getTypeOfFoodByCriteria(@RequestParam(value = "criteria", defaultValue = "id") String criteria, @RequestParam(value = "valueOfCriteria", defaultValue = "0") String value ) {
        return getResponseEntity(criteria,value,rdfLocalManager::searchInMenu);
    }

    //TODO UNCOMMENT

    @RequestMapping(value = "/rdf/getMenuToHTML", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> getMenuToHTML() {
        rdfLocalManager.writeMenuToFuseki("1","25 min", "lunch", "vegetarian","Tagliatelle with Coriander Pesto", "687377.jpg", "Fresh tagliatelli, coated in a delicious and nutty homemade coriander (cilantro) pesto! Perfect and scrumptious side dish!", " bunch chopped fresh cilantro 6 tablespoons pine nuts 1 teaspoon lemon juice, or to taste 1/3 cup crumbled feta cheese salt and ground black pepper to taste 1/2 cup olive oil 1 (12 ounce) package dry tagliatelle or wide fettucine pasta 1 teaspoon extra-virgin olive oil", "Italy");
        rdfLocalManager.writeMenuToFuseki("2","40 min","lunch","seafood","Shrimp Scampi with Pasta", "960577.jpg", "Well-rounded seafood and pasta dish. Good with any pasta; angel hair is less filling.", "1 (16 ounce) package linguine pasta 2 tablespoons butter 2 tablespoons extra-virgin olive oil 2 shallots, finely diced 2 cloves garlic, minced 1 pinch red pepper flakes (optional) 1 pound shrimp, peeled and deveined 1 pinch kosher salt and freshly ground pepper 1/2 cup dry white wine 1 lemon, juiced 2 tablespoons butter 2 tablespoons extra-virgin olive oil 1/4 cup finely chopped fresh parsley leaves 1 teaspoon extra-virgin olive oil, or to taste", "Italy");
        rdfLocalManager.writeMenuToFuseki("3","60 min","lunch","pork meat","Italian Breaded Pork Chops", "257811.jpg", "Fresh tagliatelli, coated in a delicious and nutty homemade coriander (cilantro) pesto! Perfect and scrumptious side dish!", "3 eggs, lightly beaten 3 tablespoons milk 1 1/2 cups Italian seasoned bread crumbs 1/2 cup grated Parmesan cheese 2 tablespoons dried parsley 2 tablespoons olive oil 4 cloves garlic, peeled and chopped 4 pork chops", "Italy");
        rdfLocalManager.writeMenuToFuseki("4","75 min","dinner","chicken meat","White Cheese Chicken Lasagna", "735349.jpg", "A chicken and spinach lasagna with a creamy white cheese sauce. Great for any kind of pot luck. My kids love it.", " 9 lasagna noodles 1/2 cup butter 1 onion, chopped 1 clove garlic, minced 1/2 cup all-purpose flour 1 teaspoon salt 2 cups chicken broth 1 1/2 cups milk 4 cups shredded mozzarella cheese, divided 1 cup grated Parmesan cheese, divided 1 teaspoon dried basil 1 teaspoon dried oregano 1/2 teaspoon ground black pepper 2 cups ricotta cheese 2 cups cubed, cooked chicken meat 2 (10 ounce) packages frozen chopped spinach, thawed and drained 1 tablespoon chopped fresh parsley 1/4 cup grated Parmesan cheese for topping", "Italy");
        rdfLocalManager.writeMenuToFuseki("5","20 min","dinner","fish soup","Bouillabaisse", "920615.jpg", "A chicken and spinach lasagna with a creamy white cheese sauce. Great for any kind of pot luck. My kids love it.", " 9 lasagna noodles 1/2 cup butter 1 onion, chopped 1 clove garlic, minced 1/2 cup all-purpose flour 1 teaspoon salt 2 cups chicken broth 1 1/2 cups milk 4 cups shredded mozzarella cheese, divided 1 cup grated Parmesan cheese, divided 1 teaspoon dried basil 1 teaspoon dried oregano 1/2 teaspoon ground black pepper 2 cups ricotta cheese 2 cups cubed, cooked chicken meat 2 (10 ounce) packages frozen chopped spinach, thawed and drained 1 tablespoon chopped fresh parsley 1/4 cup grated Parmesan cheese for topping", "France");
        rdfLocalManager.writeMenuToFuseki("6","30 min","breakfast","ham with eggs","Oven Omlette", "595305.jpg", "Hearty meat and potato omelet baked in the oven.", "A chicken and spinach lasagna with a creamy white cheese sauce. Great for any kind of pot luck. My kids love it.", "France");
        rdfLocalManager.writeMenuToFuseki("7","75 min","dinner","chicken meat","White Cheese Chicken Lasagna", "735349.jpg", "This simply prepared fish stew is a classic French recipe from Marseilles. Serve with a slice of hot toast topped with a spoonful of rouille.", "3/4 cup olive oil 2 onions, thinly sliced 2 leeks, sliced 3 tomatoes - peeled, seeded and chopped 4 cloves garlic, minced 1 sprig fennel leaf 1 sprig fresh thyme 1 bay leaf 1 teaspoon orange zest 3/4 pound mussels, cleaned and debearded 9 cups boiling water salt and pepper to taste 5 pounds sea bass 1 pinch saffron threads 3/4 pound fresh shrimp, peeled and deveined", "Italy");
        rdfLocalManager.writeMenuToFuseki("8","10 min","dinner","dessert","Bee Sting Cake (Bienenstich) II", "270399.jpg", "It is bread like with a sugary-almond crunchy crust and a vanilla pudding filling", " 1 5/8 cups all-purpose flour 1 tablespoon active dry yeast 2 tablespoons white sugar 1 pinch salt 3/4 cup lukewarm milk 3 tablespoons butter 3 tablespoons butter 1 1/2 tablespoons confectioners' sugar 1 tablespoon milk 5/8 cup sliced almonds 1 tablespoon honey (optional) 1 1/2 cups milk 1/3 cup cornstarch 1 tablespoon white sugar 1 egg, beaten 1 teaspoon almond extract 1 cup heavy whipping cream 1/2 tablespoon cream of tartar", "Germany");
        return getResponseEntity(rdfLocalManager::readMenuFromFuseki);
    }

}


    // for returning menu using fuseki, converting to JSON, to html menu page
    @FunctionalInterface
    interface RdfSupplierToHTML<V> {

        V get() throws Exception;
    }

    // for query-ing the menu
    @FunctionalInterface
    interface RdfSupplier<K, V> {

        V get(K k) throws Exception;
    }

    // for dynamically query the menu, using criteria - value queries
    @FunctionalInterface
    interface RdfMenuSupplier<K1, K2, V> {

        V get(K1 k1, K2 k2) throws Exception;
    }

    // for query-ing the resources country of origin (romania default)
    //                             time of the day (breakfast, lunch, dinner)
    //                             type of occasion (date, alone, family, business)
    @FunctionalInterface
    interface RdfResourcesSupplier<T, K1, K2, K3> {

        T get(K1 k1, K2 k2, K3 k3) throws Exception;
    }