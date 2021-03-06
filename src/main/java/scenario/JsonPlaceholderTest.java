package scenario;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.jayway.restassured.RestAssured.*;
import static java.lang.System.out;
import static java.util.Collections.reverseOrder;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;


public class JsonPlaceholderTest {

    @BeforeClass
    public static void setupUrl() {
        baseURI = "https://jsonplaceholder.typicode.com";
    }

    private static Response response;
    private final Comments comments = new Comments();

    @Test
    public void shouldPrintHighestUserIdValue() {
        List<Integer> userIds = getUserIdList();
        userIds.sort(reverseOrder());

        Integer highestUserIdValue = userIds.get(0);
        out.println("Highest userId value from response is: " + highestUserIdValue);
    }

    @Test
    public void shouldPrintHighestIdValueForUserId() {
        List<Integer> userIds = getUserIdList();
        Integer userId = userIds.get(0);

        List<Integer> idsForUserId = getIdListForUserId(userId);
        idsForUserId.sort(reverseOrder());

        Integer highestIdValueForUserId = idsForUserId.get(0);
        out.println("Highest id value for userId " + userId + " is: " + highestIdValueForUserId);
    }

    @Test
    public void shouldAddNewCommentForPostId() {
        Integer identifier = getUserIdList().get(0);
        String testValue = "test_comment";

        comments.setBody(testValue);

        response =
                given().body(comments)
                        .when().contentType(ContentType.JSON)
                        .put("/comments/" + identifier)
                        .then().statusCode(SC_OK)
                        .extract().response();

        assertThat(response.asString()).contains(testValue);

    }


    private List<Integer> getIdListForUserId(Integer userId) {
        return when().get("/posts?userId=" + userId)
                .then().statusCode(SC_OK)
                .extract().response()
                .jsonPath().get("id");
    }

    private List<Integer> getUserIdList() {
        return when().get("/posts")
                .then().statusCode(SC_OK)
                .extract().response()
                .jsonPath().get("userId");
    }

}
