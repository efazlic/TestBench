package utils;

import com.google.gson.Gson;
import data.ApiCalls;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import objects.User;
import org.testng.Assert;

public class RestApiUtils extends LoggerUtils {

    private static final String BASE_URL = PropertiesUtils.getBaseUrl();
    private static final String ADMIN_USERNAME = PropertiesUtils.getAdminUsername();
    private static final String ADMIN_PASSWORD = PropertiesUtils.getAdminPassword();

    private static Response checkIfUserExistsApiCall(String sUsername, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createCheckIfUserExistsApiCall(sUsername);
        log.info("API CALL: " + sApiCall);
        Response response = null;
        try {
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", io.restassured.http.ContentType.JSON, "Accept", ContentType.JSON)
                    .when().get(sApiCall);

        }
        catch (Exception e) {
            Assert.fail("Exception in checkIfUserExists(" + sUsername + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static boolean checkIfUserExists(String sUsername, String sAuthUser, String sAuthPass) {
        log.trace("checkIfUserExists(" + sUsername + ")");
        Response response = checkIfUserExistsApiCall(sUsername, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in checkIfUserExists(" + sUsername + "). Response Body: " + sResponseBody);
        if(!(sResponseBody.equalsIgnoreCase("true") || sResponseBody.equalsIgnoreCase("false"))) {
            Assert.fail("Cannot convert response " + sResponseBody + " to boolean value!");
        }
        return Boolean.parseBoolean(sResponseBody);
    }

    public static boolean checkIfUserExists(String sUsername) {
        return checkIfUserExists(sUsername, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    public static Response getUserApiCall(String sUsername, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createGetUserApiCall(sUsername);
        Response response = null;
        try {
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", io.restassured.http.ContentType.JSON, "Accept", ContentType.JSON)
                    .when().get(sApiCall);

        }
        catch (Exception e) {
            Assert.fail("Exception in getUserApiCall(" + sUsername + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static String getUserJsonFormat(String sUsername, String sAuthUser, String sAuthPass) {
        log.trace("getUserJsonFormat(" + sUsername + ")");
        Assert.assertTrue(checkIfUserExists(sUsername, sAuthUser, sAuthPass));
        Response response = getUserApiCall(sUsername, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in checkIfUserExists(" + sUsername + "). Response Body: " + sResponseBody);
        return sResponseBody;
    }

    public static String getUserJsonFormat(String sUsername) {
        return getUserJsonFormat(sUsername, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    public static User getUser(String sUsername, String sAuthUser, String sAuthPass) {
        String json = getUserJsonFormat(sUsername, sAuthUser, sAuthPass);
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }

    public static User getUser(String sUsername) {
        return getUser(sUsername, ADMIN_USERNAME, ADMIN_PASSWORD);
    }

    public static Response deleteUserApiCall(String sUsername, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createDeleteUserApiCall(sUsername);
        Response response = null;
        try {
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", io.restassured.http.ContentType.JSON, "Accept", ContentType.JSON)
                    .when().redirects().follow(false)
                    .delete(sApiCall);
        }
        catch (Exception e) {
            Assert.fail("Exception in deleteUserApiCall(" + sUsername + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static void deleteUser(String sUsername, String sAuthUser, String sAuthPass) {
        Assert.assertTrue(checkIfUserExists(sUsername, sAuthUser, sAuthPass));
        Response response = deleteUserApiCall(sUsername, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in checkIfUserExists(" + sUsername + "). Response Body: " + sResponseBody);
    }

    public static void deleteUser(String sUsername) {

    }

    private static Response postUserApiCall(User user, String sAuthUser, String sAuthPass) {
        String sApiCall = BASE_URL + ApiCalls.createPostUserApiCall();
        Response response = null;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(user, User.class);
            response = RestAssured.given().auth().basic(sAuthUser, sAuthPass)
                    .headers("Content-Type", ContentType.JSON, "Accept", ContentType.JSON)
                    .body(json)
                    .when().redirects().follow(false)
                    .post(sApiCall);
        }
        catch (Exception e) {
            Assert.fail("Exception in postUserApiCall(" + user.getUsername() + ") Api Call: " + e.getMessage());
        }
        return response;
    }

    public static void postUser(User user, String sAuthUser, String sAuthPass) {
        Assert.assertFalse(checkIfUserExists(user.getUsername(), sAuthUser, sAuthPass));
        Response response = postUserApiCall(user, sAuthUser, sAuthPass);
        int status = response.getStatusCode();
        String sResponseBody = response.getBody().asString();
        Assert.assertEquals(status, 200, "Wrong Response Status Code in checkIfUserExists(" + user.getUsername() + "). Response Body: " + sResponseBody);
        log.debug("User created: " + checkIfUserExists(user.getUsername(), sAuthUser, sAuthPass));
    }

    public static void postUser(User user) {
        postUser(user, ADMIN_USERNAME, ADMIN_PASSWORD);
    }
}
