import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.entity.ContentType;

import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HttpClient {

 public static final String REMOTE_SERVICE_URI = ("https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats");
 public static ObjectMapper mapper = new ObjectMapper();

 public static void main(String[] args) throws IOException {
 CloseableHttpClient httpClient = HttpClientBuilder.create()
          .setUserAgent("My Test Service")
          .setDefaultRequestConfig(RequestConfig.custom()
                  .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                  .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                  .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                  .build())
          .build();
  HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
  request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
  CloseableHttpResponse response = httpClient.execute(request);
  Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
//  String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
//  System.out.println(body);
  List<Post>posts = mapper.readValue(response.getEntity().getContent(),
          new TypeReference<>(){
          }
  );
  posts = posts.stream().filter(value -> value.getUpvotes() != null)
                  .collect(Collectors.toList());
  posts.forEach(System.out::println);
  response.close();
  httpClient.close();

 }
}
