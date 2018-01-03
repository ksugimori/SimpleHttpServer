package ksugimori.http.message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ksugimori.http.exception.InvalidMessageException;

public class Parser {
  private static Pattern requestLine =
      Pattern.compile("^(?<method>\\S+) (?<target>\\S+) (?<version>\\S+)$");
  private static final String SP = " ";
  private static final String CRLF = "\r\n";

  /**
   * parse HTTP Request
   * 
   * @param in
   * @return
   * @throws InvalidMessageException
   * @throws IOException
   */
  public static Request parseRequest(InputStream in) throws InvalidMessageException, IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    String startLine = br.readLine();

    Matcher matcher = requestLine.matcher(startLine);

    if (!matcher.matches()) {
      throw new InvalidMessageException("CANNOT PARSE REQUEST LINE.");
    }

    Method method = Method.valueOf(matcher.group("method"));
    String target = matcher.group("target");
    String version = matcher.group("version");

    // TODO Header, body も読む？

    return new Request(method, target, version);
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
