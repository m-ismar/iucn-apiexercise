import com.google.gson.Gson;
import model.Species;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ThreadLocalRandom;

public class App {

    /**
     * Constants
     */
    private static final String BASE_URL = "http://apiv3.iucnredlist.org/api/v3";
    private static final String REGION_LIST_URL = "/region/list";
    private static final String REGION_SPECIES_URL = "/species/region";
    private static final String FIRST_PAGE_URL = "/page/0";
    private static final String MEASURES_URL = "/measures/species/name";
    private static final String TOKEN = "?token=9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"; //Temp token

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void main(String[] args) {

        //Load region list
        JSONObject regionResponse = sendGetRequest(BASE_URL + REGION_LIST_URL + TOKEN);
        if(regionResponse == null)
            return;

        //Get random region
        int numOfRegions = regionResponse.getInt("count");
        int randomNum = ThreadLocalRandom.current().nextInt(0, numOfRegions - 1);

        JSONArray regions = regionResponse.getJSONArray("results");
        JSONObject region = regions.getJSONObject(randomNum);
        String regionIdentifier = region.getString("identifier");

        //Load first page of species in that region
        JSONObject speciesResponse = sendGetRequest(BASE_URL + REGION_SPECIES_URL + "/" + regionIdentifier + FIRST_PAGE_URL + TOKEN);

        if(speciesResponse == null)
            return;

        JSONArray speciesJson = speciesResponse.getJSONArray("result");

        //Map responses to Species array
        Species[] species = new Gson().fromJson(speciesJson.toString(), Species[].class);

        System.out.println(species.length);
        //Filter list once by category = "CR"
        //Load measures for every element of filtered list
        //Print result


        //Filter list again by class = "MAMMALIA"
        //Print result
    }

    private static JSONObject sendGetRequest(String url) {
        System.out.println(url);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();

        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            if(response.statusCode() != 200){
                System.err.println("ERROR: Status: " + response.statusCode() + "; Message: " + response.body());
                return null;
            }

            System.out.println(response.body());
            return new JSONObject(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }



}
