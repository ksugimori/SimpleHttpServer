package ksugimori.http.message;

public enum Status {
  OK(200, "OK"),
  BAD_REQUEST(400, "Bad Request"),
  NOT_FOUND(404, "Not Found");
  
  private int code;
  private String reasonPhrase;
  private Status(int code, String reasonPhrase) {
    this.code = code;
    this.reasonPhrase = reasonPhrase;
  }
  public int getCode() {
    return code;
  }
  public String getReasonPhrase() {
    return reasonPhrase;
  }
}
