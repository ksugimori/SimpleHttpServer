import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import ksugimori.http.message.Parser;

public class ParserTest {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = Parser.class.getDeclaredMethod("parseRequestLine", String.class);
		method.setAccessible(true);
		
		System.out.println(method.invoke(null, "GETT /index.html HTTP/1.1"));
	}
}
