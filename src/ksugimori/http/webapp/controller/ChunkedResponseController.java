package ksugimori.http.webapp.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import ksugimori.http.message.Request;
import ksugimori.http.message.Response;
import ksugimori.http.webapp.Controller;
import ksugimori.http.webapp.RequestMapping;

/**
 * {@link ksugimori.http.webapp.controller.BasicHttpController} と同様の処理を行うがレスポンスボディを 20 byte 単位で分割する
 */
@RequestMapping("/chunked")
public class ChunkedResponseController extends Controller {
  public static int CHUNK_SIZE = 20;
  public static byte[] CRLF = new byte[] {0x0D, 0x0A};

  private Controller basicController;

  public ChunkedResponseController() {
    basicController = new BasicHttpController();
  }

  @Override
  public Response doGet(Request request) {
    Response response = basicController.doGet(request);
    chunk(response);
    return response;
  }

  @Override
  public Response doPost(Request request) {
    Response response = basicController.doPost(request);
    return response;
  }

  private void chunk(Response response) {
    byte[] body = response.getBody();

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      for (int offset = 0; offset < body.length; offset += CHUNK_SIZE) {
        byte[] chunk = Arrays.copyOfRange(body, offset, offset + CHUNK_SIZE);
        String lengthHex = Integer.toHexString(chunk.length);
        out.write(lengthHex.getBytes());
        out.write(CRLF);
        out.write(chunk);
        out.write(CRLF);
      }
      out.write("0".getBytes());
      out.write(CRLF);
      out.write(CRLF);
    } catch (IOException e) {
      return;
    }
    
    response.getHeaders().remove("Content-Length");
    response.addHeaderField("Transfer-Encoding", "chunked");
    response.setBody(out.toByteArray());
  }
}
