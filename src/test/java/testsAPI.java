/**
 * Created by sharath.ramesh on 2/6/2018.
 */

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import io.restassured.http.ContentType;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.*;

import java.io.IOException;
import java.io.StringReader;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class testsAPI {

    protected static String urlAll = "http://localhost:5000/interview/api/v1.0/results";
    protected static String invalidURL ="http://localhost:5000/interview/api/v1.0/resultsForArea/";


    //Returns HTML response as a JSON object
    public JsonObject returnsJSON_all(String baseURL) throws IOException {

        Document doc =  Jsoup.connect(baseURL).get();
        Element element = doc.body();
        String html = element.toString();
        String s = Jsoup.parse(html).text();
        Gson g = new Gson();


        int indexOfOpenBracket = s.indexOf("[");
        int indexOfLastBracket = s.lastIndexOf("]");

        String jsonStr = s.substring(indexOfOpenBracket+1, indexOfLastBracket);
        JsonReader reader = new JsonReader(new StringReader(jsonStr));
        reader.setLenient(true);
        JsonElement e = g.fromJson(reader, JsonElement.class);
        JsonObject jsonobject = e.getAsJsonObject();


        return jsonobject;


    }




    @Test
    public void isValidURL() {
        given().
                when().
                get(urlAll).
                then().
                log().ifError().
                assertThat().statusCode(200);
    }

    @Test
    public void isInvalidURL() {
        given().
                when().
                get(invalidURL).
                then().
                log().ifError().
                assertThat().statusCode(404);
    }

    @Test
    public void isResponseJSON() throws IOException {
         given().
                when().
                get(urlAll).
                then().
                assertThat().body(containsString("area_code")).
                assertThat().body(containsString("phone_number")).
                assertThat().body(containsString("report_count")).
                assertThat().body(containsString("comment"));


          JsonObject json = returnsJSON_all(urlAll);

          Assert.assertTrue(!json.isJsonNull());




    }

   @Test
    public void validateAreaCode() throws IOException {
        JsonObject json = returnsJSON_all(urlAll);
        JsonElement area_code = json.get("area_code");
        String ac = area_code.getAsString();
        int AC = Integer.parseInt(ac);
        Assert.assertTrue(ac.length()==3);
        Assert.assertTrue(AC<=999);



    }

    @Test //Validates Proper format of Phone number. Number is of the form XXX-XXX-XXXX
    public void validatePhoneNumber() throws IOException {
        JsonObject json = returnsJSON_all(urlAll);
        JsonElement phone_number = json.get("phone_number");
        String ph = phone_number.getAsString();
        Assert.assertTrue(ph.length()==12);

        String ph1= ph.substring(0,2);
        int PH1 = Integer.parseInt(ph1);
        String ph2=ph.substring(4,6);
        int PH2=Integer.parseInt(ph2);
        String ph3=ph.substring(8,11);
        int PH3=Integer.parseInt(ph3);

        Assert.assertTrue(PH1>=0 && PH1<=999);
        Assert.assertTrue(PH2>=0 && PH2<=999);
        Assert.assertTrue(PH3>=0 && PH3<=9999);


    }



}
