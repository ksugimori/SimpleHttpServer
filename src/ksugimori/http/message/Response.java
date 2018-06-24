package ksugimori.http.message;

/**
 * HTTP Request
 * 
 * @author ksugimori
 *
 */
public class Response extends AbstractHttpMessage {
  String version;
  Status status;

  public Response(String version, Status status) {
    super();
    this.version = version;
    this.status = status;
  }

  public String getVersion() {
    return version;
  }

  public int getStatusCode() {
    return status.getCode();
  }

  public String getReasonPhrase() {
    return status.getReasonPhrase();
  }

  @Override
  public String getStartLine() {
    return version + " " + getStatusCode() + " " + getReasonPhrase();
  }
}
