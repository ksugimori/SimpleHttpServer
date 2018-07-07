package ksugimori.http.message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ksugimori.http.exception.ParseException;

public class Parser {
  public static final String PROTOCOL_VERSION = "HTTP/1.1";
  
  private static Pattern requestLinePattern =
      Pattern.compile("^(?<method>\\S+) (?<target>\\S+) (?<version>\\S+)$");
  private static Pattern headerPattern =
      Pattern.compile("^(?<name>\\S+):[ \\t]?(?<value>.+)[ \\t]?$");
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
    Request request = parseRequestLine(br);
    parseHeaderLines(br, request);
    parseBody(br, request);
    
    return request;
  }

  /**
   * parse request line
   * @param br
   * @return
   * @throws IOException
   * @throws ParseException
   */
  private static Request parseRequestLine(BufferedReader br) throws IOException, ParseException {
    String requestLine = br.readLine();

    Matcher matcher = requestLinePattern.matcher(requestLine);

    if (!matcher.matches()) {
      throw new ParseException(requestLine);
    }

    Method method = Method.valueOf(matcher.group("method"));
    String target = matcher.group("target");
    String version = matcher.group("version");

    Request request = new Request(method, target, version);
    return request;
  }

  /**
   * parse header lines.
   * @param br
   * @param request
   * @throws IOException
   * @throws ParseException
   */
  private static void parseHeaderLines(BufferedReader br, Request request) throws IOException, ParseException {
    while ( true ) {
      String headerField = br.readLine();
      if ( EMPTY.equals(headerField.trim()) ) break; // header と body の区切りまで読む

      Matcher matcher = headerPattern.matcher(headerField);

      if (matcher.matches()) {
        request.addHeaderField(matcher.group("name").toLowerCase(), matcher.group("value"));
      } else {
        throw new ParseException(headerField);
      }
    }
  }

  /**
   * parse request body.
   * @param br
   * @param request
   * @throws IOException
   */
  private static void parseBody(BufferedReader br, Request request) throws IOException {
    if (request.getHeaders().containsKey("transfer-encoding")) {
      parseChunkedBody(br, request);
    } else if (request.getHeaders().containsKey("content-length")) {
      parseSimpleBody(br, request);
    } else {
      // nothing to read
    }
  }

  private static void parseChunkedBody(BufferedReader br, Request request) throws IOException {
    String transferEncoding = request.getHeaders().get("transfer-encoding");
    if (transferEncoding.equals("chunked")) { // only accept "chunked"
      int length = 0;
      ByteArrayOutputStream body = new ByteArrayOutputStream();
      String chunkSizeHex = br.readLine().replaceFirst(" .*$", ""); // ignore chunk-ext
      int chunkSize = Integer.parseInt(chunkSizeHex, 16);
      while (chunkSize > 0) {
        char[] chunk = new char[chunkSize];
        br.read(chunk, 0, chunkSize);
        br.skip(2); // CRLF
        body.write((new String(chunk)).getBytes());
        length += chunkSize;

        chunkSizeHex = br.readLine().replaceFirst(" .*$", "");
        chunkSize = Integer.parseInt(chunkSizeHex, 16);
     }
     request.addHeaderField("content-length", Integer.toString(length));
     request.getHeaders().remove("transfer-encoding");
     request.setBody(body.toByteArray());
    }
  }

  private static void parseSimpleBody(BufferedReader br, Request request)
      throws IOException {
    Integer contentLength = Integer.valueOf(request.getHeaders().get("content-length"));

    char[] body = new char[contentLength];
    br.read(body, 0, contentLength);

    request.setBody((new String(body)).getBytes());
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
