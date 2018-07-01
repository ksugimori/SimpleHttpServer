package ksugimori.http.webapp;


import ksugimori.http.exception.UnsupportedMethodException;
import ksugimori.http.message.Method;
import ksugimori.http.message.Request;
import ksugimori.http.message.Response;

/**
 * コントローラの抽象クラス。
 * このクラスの実装クラスは POST リクエストの処理 
 */
public abstract class Controller {
  /**
   * POST リクエストを受け取った場合に実行されるメソッド
   * @param request HTTP Request
   * @return HTTP Response
   */
  public abstract Response doPost(Request request);

  /**
   * POST リクエストを受け取った場合に実行されるメソッド
   * @param request HTTP Request
   * @return HTTP Response
   */
  public abstract Response doGet(Request request);
  
  /**
   * request の HTTP メソッドによってメソッドの振り分けを行い、レスポンスを返す。
   * HTTP メソッドは GET, POST にのみ対応。
   * @param request HTTP Request
   * @return HTTP Response
   * @throws UnsupportedMethodException GET, POST 以外のメソッドのリクエストが渡された場合
   */
  public Response handle(Request request) throws UnsupportedMethodException {
    Method method = request.getMethod();
    Response response;
    switch (method) {
      case GET:
        response = doGet(request);
        break;
      case POST:
        response = doPost(request);
        break;
      default:
        throw new UnsupportedMethodException(method.toString());
    }
    
    return response;
  }
}
