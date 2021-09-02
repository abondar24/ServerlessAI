import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.abondar.experimental.identity.client.IdentityClient;
import org.abondar.experimental.identity.data.AnalyseResponse;
import org.abondar.experimental.identity.data.UploadResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class IdentityClientTest {

    private  static MockWebServer mockServer;

    private static ObjectMapper mapper;

    @BeforeAll
    public static void init() {
      mapper = new ObjectMapper();
      mockServer = new MockWebServer();
    }

    @Test
    public void uploadTest() throws Exception {
        var file = "samples/passport.jpg";
        var uploadResp = new UploadResponse();
        var body = mapper.writeValueAsString(uploadResp);
        var client = new IdentityClient(mockServer.url("/analyse").toString());

        mockServer.enqueue(new MockResponse().setBody(body));

        var resp=client.uploadImage(file);
        assertNull(resp);

    }


    @Test
    public void analyseTest() throws Exception {
        var analyseResp = AnalyseResponse.builder().build();
        var body = mapper.writeValueAsString(analyseResp);
        var id = UUID.randomUUID().toString();
        var client = new IdentityClient(mockServer.url("/analyse/"+id).toString());

        mockServer.enqueue(new MockResponse().setBody(body));

        var resp=client.analyseResults(id);
        assertNotNull(resp);
    }
}
