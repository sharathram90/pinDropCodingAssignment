

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

import java.awt.geom.Area;
import java.io.IOException;
import java.io.StringReader;

import static io.restassured.RestAssured.*;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;

public class testsAPI_limit extends testsAPI {
    protected static String lim = "2";
    protected static String baseURL = "http://localhost:5000/interview/api/v1.0/results/" + lim;
    protected static String invLim = "999";
    protected static String invalidURL ="http://localhost:5000/interview/api/v1.0/results/" + invLim;

    @Test
    public void isValidURL() {
        given().
                when().
                get(baseURL).
                then().
                log().ifError().
                assertThat().statusCode(200);

    }

    @Test //Invalid limit displays the complete JSON response
    public void invalidLimit() throws IOException {
        Assert.assertEquals(returnsJSON_all(invalidURL),returnsJSON_all("http://localhost:5000/interview/api/v1.0/results"));
    }

    @Test
    public void validLimit() throws IOException {
       Assert.assertNotEquals(returnsJSON_all(baseURL),returnsJSON_all("http://localhost:5000/interview/api/v1.0/results"));
    }

    @Test
    public void isAllAttributesPresent() {
        given().
                when().
                get(baseURL).
                then().
                assertThat().body(containsString("area_code")).
                assertThat().body(containsString("phone_number")).
                assertThat().body(containsString("report_count")).
                assertThat().body(containsString("comment"));
    }
}
