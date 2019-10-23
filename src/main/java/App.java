import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Species;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Main class
 */
public class App {

    /**
     * Constants
     */
    private static final String BASE_URL = "http://apiv3.iucnredlist.org/api/v3";
    private static final String REGION_LIST_URL = "/region/list";
    private static final String REGION_SPECIES_URL = "/species/region";
    private static final String FIRST_PAGE_URL = "/page/0";
    private static final String MEASURES_URL = "/measures/species/id";
    private static final String TOKEN = "?token=9bb4facb6d23f48efbf424bb05c0c1ef1cf6f468393bc745d42179ac4aca5fee"; //Temp token

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void main(String[] args) {

        //Load region list
        JSONObject regionResponse = sendGetRequest(BASE_URL + REGION_LIST_URL + TOKEN);
        if (regionResponse == null)
            return;

        //Get random region
        int numOfRegions = regionResponse.getInt("count");
        int randomNum = ThreadLocalRandom.current().nextInt(0, numOfRegions);

        JSONArray regions = regionResponse.getJSONArray("results");
        JSONObject region = regions.getJSONObject(randomNum);
        String regionIdentifier = region.getString("identifier");

        //Load first page of species in that region
        JSONObject speciesResponse = sendGetRequest(BASE_URL + REGION_SPECIES_URL + "/" + regionIdentifier + FIRST_PAGE_URL + TOKEN);

        if (speciesResponse == null)
            return;

        JSONArray speciesJson = speciesResponse.getJSONArray("result");

        //Map responses to Species array
        Type speciesListType = new TypeToken<ArrayList<Species>>() {
        }.getType();
        ArrayList<Species> species = new Gson().fromJson(speciesJson.toString(), speciesListType);

        System.out.println("Number of species in " + region.getString("name") + ": " + species.size());

        //Filter list once by category = "CR"
        List<Species> criticallyEndangered = species.stream()
                .filter(s -> s.getCategory().equals("CR")).collect(Collectors.toList());

        System.out.println("Number of critically endangered species in " + region.getString("name") + ": " + criticallyEndangered.size());

        //Load measures for every element of filtered list
        for (Species s : criticallyEndangered) {
            JSONObject measuresResponse = sendGetRequest(BASE_URL + MEASURES_URL + "/" + s.getTaxonid() + TOKEN);

            if (measuresResponse != null) {
                JSONArray measures = measuresResponse.getJSONArray("result");

                if (measures != null && !measures.isEmpty()) {

                    StringBuilder measureTitles = new StringBuilder();

                    for (int i = 0; i < measures.length(); i++) {
                        measureTitles.append(measures.getJSONObject(i).getString("title")).append("; ");
                    }
                    s.setConservation_measures(measureTitles.toString());
                }
            }
            //Print result
            System.out.println(s.toString());
        }

        //Filter list again by class = "MAMMALIA"
        List<Species> mammals = species.stream()
                .filter(s -> s.getClass_name().equals("MAMMALIA")).collect(Collectors.toList());

        System.out.println("Number of mammal species in " + region.getString("name") + ": " + mammals.size());
        //Print result
        for (Species s : mammals) {
            System.out.println(s.toString());
        }
    }

    /**
     * Simple method that sends a get request to given url and maps it to a JSONObject
     *
     * @param url the URL of the get request
     * @return a JSONObject if status code is 200, else print error and return null
     */
    private static JSONObject sendGetRequest(String url) {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("ERROR: Status: " + response.statusCode() + "; Message: " + response.body());
                return null;
            }

            return new JSONObject(response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}
