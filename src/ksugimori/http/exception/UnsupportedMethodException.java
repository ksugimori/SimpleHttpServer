package ksugimori.http.exception;

/**
 * 未対応の HTTP メソッドを受け取った場合の例外
 */
public class UnsupportedMethodException extends Exception {
  private static final long serialVersionUID = 1L;

  public UnsupportedMethodException(String method) {
    super("unknown HTTP method: " + method);
  }

}
