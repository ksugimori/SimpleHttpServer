package ksugimori.http.exception;

/**
 * HTTP メッセージの解析に失敗したことを表す例外
 */
public class InvalidMessageException extends Exception {
  private static final long serialVersionUID = 1L;

  public InvalidMessageException(Throwable cause) {
    super(cause);
  }

  public InvalidMessageException(String message) {
    super(message);
  }

}
