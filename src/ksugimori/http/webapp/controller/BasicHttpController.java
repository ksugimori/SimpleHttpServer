package ksugimori.http.webapp.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import ksugimori.http.message.Request;
import ksugimori.http.message.Response;
import ksugimori.http.message.Status;
import ksugimori.http.server.SimpleHttpServer;
import ksugimori.http.util.StringUtils;
import ksugimori.http.webapp.Controller;

/**
 * 通常の HTTP リクエストを処理するコントローラ
 */
public class BasicHttpController extends Controller {
  public static String protocolVersion = "HTTP/1.1";

  @Override
  public Response doGet(Request request) {
    Path target = Paths.get(SimpleHttpServer.getDocumentRoot(), request.getTarget()).normalize();

    // ドキュメントルート以下のみアクセス可能にする
    if (!target.startsWith(SimpleHttpServer.getDocumentRoot())) {
      return new Response(protocolVersion, Status.BAD_REQUEST);
    }

    if (Files.isDirectory(target)) {
      target = target.resolve("index.html");
    }

    Response response;
    try {
      response = new Response(protocolVersion, Status.OK);
      response.setBody(Files.readAllBytes(target));

      String ext = StringUtils.getFileExtension(target.getFileName().toString());
      String contentType = SimpleHttpServer.extensionToContentType(ext);

      response.addHeaderField("Content-Type", contentType);
    } catch (IOException e) {
      response = new Response(protocolVersion, Status.NOT_FOUND);
      response.setBody(SimpleHttpServer.readErrorPage(Status.NOT_FOUND));
    }

    return response;
  }

  @Override
  public Response doPost(Request request) {
    // nothing to do
    System.out.println("POST body: " + new String(request.getBody(), StandardCharsets.UTF_8));
    Response response = new Response(protocolVersion, Status.NO_CONTENT);
    return response;
  }
}
