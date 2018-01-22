package ksugimori.http.exception;

/**
 * HTTP メッセージの解析に失敗したことを表す例外
 */
public class ParseException extends Exception {
  private static final long serialVersionUID = 1L;

  private String requestLine;
  
  public ParseException(String startLine) {
    super("CANNOT PARSE REQUEST LINE: " + startLine);
    this.requestLine = startLine;
  }

  /**
   * 解析できなかった request-line を返します
   */
  public String getRequestLine() {
    return requestLine;
  }
}
