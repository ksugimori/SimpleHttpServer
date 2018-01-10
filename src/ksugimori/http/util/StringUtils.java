package ksugimori.http.util;

public class StringUtils {
  private static final String EXT_SPLIT_CHAR = ".";

  /**
   * ファイル名を表す文字列から拡張子を抽出します
   * 
   * @param fileName
   * @return
   */
  public static String getFileExtension(String fileName) {
    int pos = fileName.lastIndexOf(EXT_SPLIT_CHAR);
    if (pos > 0) {
      return fileName.substring(pos + 1);
    } else {
      return "";
    }
  }


}
