package ksugimori.http.message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ksugimori.http.exception.ParseException;

public class Parser {
  public static final String PROTOCOL_VERSION = "HTTP/1.1";
  
  private static Pattern requestLinePattern =
      Pattern.compile("^(?<method>\\S+) (?<target>\\S+) (?<version>\\S+)$");
  private static final String EMPTY = "";
  private static final String SP = " ";
  private static final String CRLF = "\r\n";

  /**
   * parse HTTP Request
   * 
   * @param in
   * @return
   * @throws ParseException
   * @throws IOException
   */
  public static Request parseRequest(InputStream in) throws ParseException, IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String requestLine = br.readLine();

    Matcher matcher = requestLinePattern.matcher(requestLine);

    if (!matcher.matches()) {
      throw new ParseException(requestLine);
    }

    Method method = Method.valueOf(matcher.group("method"));
    String target = matcher.group("target");
    String version = matcher.group("version");

    Request request = new Request(method, target, version);
    readHeaders(br, request);
    readBody(in, request);
    
    return request;
  }

  private static void readHeaders(BufferedReader br, Request request) throws IOException {
    while ( true ) {
      String line = br.readLine();
      if ( EMPTY.equals(line.trim()) ) break; // header と body の区切りまで読む
      
      String[] tmp = line.split(": +");
      if (tmp.length != 2) continue;
      request.addHeaderField(tmp[0], tmp[1]);
    }
  }

  private static void readBody(InputStream in, Request request) throws IOException {
    Optional<String> contentLength = Optional.ofNullable(request.getHeaders().get("Content-Length"));
    int length = contentLength.map(Integer::valueOf).orElse(0);
    byte[] body = new byte[length];
    in.read(body, 0, length);
    
    request.setBody(body);
  }

  public static void writeResponse(OutputStream out, Response response) throws IOException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

    String version = response.getVersion();
    Integer statusCode = response.getStatusCode();
    String reasonPhrase = response.getReasonPhrase();

    
    writer.write(version + SP + statusCode + SP + reasonPhrase + CRLF);
    for (Map.Entry<String, String> field : response.getHeaders().entrySet()) {
      writer.write(field.getKey() + ": " + field.getValue() + CRLF);
    }
    writer.write(CRLF);
    writer.flush();

    // ボディはファイルから読み取ったバイト列をそのまま書き込む
    out.write(response.getBody());
  }
}
