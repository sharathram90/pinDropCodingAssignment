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
import static org.hamcrest.Matchers.*;
public class testsAPI_areaCode extends testsAPI {
    protected static String AreaCode = "877";
    protected static String url_areacode = "http://localhost:5000/interview/api/v1.0/resultsForArea/" + AreaCode;
    protected static String invalidAreaCode = "9999";
    protected static String invalidURL = "http://localhost:5000/interview/api/v1.0/resultsForArea/" + invalidAreaCode;
    protected static String phoneNumber = "877-662-5128";
    protected static String reports = "53";
    protected static String comment = "A scamkdxyiun2 6s,so,ltz,fz";

    @Test
    public void isValidURL() {
        given().
                when().
                get(url_areacode).
                then().
                log().ifError().
                assertThat().statusCode(200);
    }

    @Test //HTML response has [] value when invalid area_code is passed
    public void inInvalidURL() throws IOException {
        given().
                when().
                get(invalidURL).
                then().
                assertThat().body(containsString("[]"));
    }

    @Test //Verifying if all the attributes are present
    public void isAllAttributesPresent() {
        given().
                when().
                get(url_areacode).
                then().
                assertThat().body(containsString("area_code")).
                assertThat().body(containsString("phone_number")).
                assertThat().body(containsString("report_count")).
                assertThat().body(containsString("comment"));
    }

    @Test //Area code should match as given
    public void doesAreaCodeMatch() throws IOException {
        JsonObject json = returnsJSON_all(url_areacode);
        JsonElement areaCode = json.get("area_code");
        String ac = areaCode.getAsString();
        String AC = ac.toString();
        Assert.assertEquals(ac,AC);
    }

    @Test //Phone Number should match as given
    public void doesPhoneNumberMatch() throws IOException {
        JsonObject json = returnsJSON_all(url_areacode);
        JsonElement phNum = json.get("phone_number");
        String Phnum = phNum.getAsString();
        System.out.println(phNum);
        Assert.assertEquals(Phnum,phoneNumber);
    }

    @Test //Reports should match as given
    public void doesReportMatch() throws IOException {
        JsonObject json = returnsJSON_all(url_areacode);
        JsonElement rep = json.get("report_count");
        String Rep = rep.getAsString();
        Assert.assertEquals(reports,Rep);
    }

    @Test //Comments should match as given
    public void doesCommentMatch() throws IOException {
        JsonObject json = returnsJSON_all(url_areacode);
        JsonElement com = json.get("comment");
        String Com = com.getAsString();
        Assert.assertEquals(comment,Com);
    }


}

