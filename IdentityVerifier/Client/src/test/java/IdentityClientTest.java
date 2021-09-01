import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.abondar.experimental.identity.client.IdentityClient;
import org.abondar.experimental.identity.data.AnalyseResponse;
import org.abondar.experimental.identity.data.UploadResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.FieldSetter;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class IdentityClientTest {

    private MockWebServer mockServer;

    private static IdentityClient client;

    private static ObjectMapper mapper;

    @BeforeAll
    public static void init() {

      mapper = new ObjectMapper();
      client = new IdentityClient("http://url/analyse");
    }

    @Test
    public void uploadTest() throws Exception {
        var file = "samples/passport.jpg";
        var uploadResp = new UploadResponse();
        var body = mapper.writeValueAsString(uploadResp);

        mockServer = new MockWebServer();
        mockServer.enqueue(new MockResponse().setBody(body));
        mockServer.start();
        mockServer.url("http://url.com/analyse");

        var resp=client.uploadImage(file);
        assertNull(resp);
        mockServer.shutdown();
    }


    @Test
    public void analyseTest() throws Exception {
        var analyseResp = AnalyseResponse.builder().build();
        var body = mapper.writeValueAsString(analyseResp);
        var id = UUID.randomUUID().toString();

        mockServer = new MockWebServer();
        mockServer.enqueue(new MockResponse().setBody(body));
        mockServer.start();
        mockServer.url("http://url.com/analyse");

        var resp=client.analyseResults(id);
        assertNotNull(resp);
        mockServer.shutdown();
    }
}
