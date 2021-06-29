import org.apache.http.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class WorkDayTest {
    private static final String URL = "https://boeing.wd1.myworkdayjobs.com/en-US/EXTERNAL_CAREERS/login";
    private static final String errorMessage = "ERROR: Invalid Username/Password";
    private static final String errorType = "ERROR";

    @Test
    void loginTest_InvalidCredentials_Error() throws IOException {
        HttpPost request = createLoginRequest();

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = httpResponse.getEntity();
        JSONObject response = new JSONObject(EntityUtils.toString(entity));

        Assertions.assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
        Assertions.assertEquals(errorMessage, response.getString("message"));
        Assertions.assertEquals(errorType, response.getString("type"));
    }

    public static HttpPost createLoginRequest() throws IOException {
        HttpPost request = new HttpPost(URL);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("username", "mateusz@gmail.com"));
        nameValuePairs.add(new BasicNameValuePair("password", "1234"));

        UrlEncodedFormEntity requestEntity = new UrlEncodedFormEntity(nameValuePairs);
        request.setEntity(requestEntity);
        request.setHeader(getCookies());
        return request;
    }

    public static Header getCookies() throws IOException {
        HttpGet request = new HttpGet(URL);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        List<String> values = Arrays.stream(httpResponse.getHeaders("Set-Cookie"))
                .map(NameValuePair::getValue)
                .toList();
        return new BasicHeader("Cookie", String.join(";", values));
    }
}