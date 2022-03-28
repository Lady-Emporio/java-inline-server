package app.route;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.route.annotations.RequestHandle;
import app.route.enums.METHODS;
import app.server.Request;
import app.server.Response;
import app.route.Path_oneValueEndStar;

public class Router {
	private static Router single;
	private List<Class<?>> controllers = new ArrayList<Class<?>>();
	private Map<METHODS, ArrayList<UrlPattern>> urlPatterns = new HashMap<METHODS, ArrayList<UrlPattern>>();
	private ArrayList<Path_oneValueEndStar> namedPaths = new ArrayList<Path_oneValueEndStar>();
	private Router() {
	}

	public static Router Instanse() {
		if (single == null) {
			single = new Router();
		}
		return single;
	}

	public void addControlers(Class<?> view) {
		controllers.add(view);
	}

	public void call(Request req, Response resp) throws IOException {
		List<UrlPattern> list=urlPatterns.get(req.method);
		if(list==null){
			throw new IOException("Method not supported.");
		}
		Map<String, String> pathValues=new HashMap<String, String>();
		for(UrlPattern urlPattern:list) {
			if (Path_oneValueEndStar.isMatch(urlPattern.pattern, req.uri.getPath(), pathValues)) {
				try {
					req.pathValues=pathValues;
					urlPattern.function.invoke(urlPattern.obj,req,resp);
					return;
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new IOException(e.getMessage());
				}
			}
		}
		resp.error404();
	}
	
	public void init() throws IOException {
		HashMap<String, Path_oneValueEndStar> uniqueNamedPaths=new HashMap<String, Path_oneValueEndStar>();
		
		for (Class<?> controler : controllers) {
			Method[] methods = controler.getDeclaredMethods();
			for (Method method : methods) {

				RequestHandle[] annotations = method.getAnnotationsByType(RequestHandle.class);

				if (annotations.length != 0) {
					Class<?>[] parameters = method.getParameterTypes();
					if (parameters.length != 2 || parameters[0] != Request.class && parameters[1] != Response.class) {
						continue;
					}
				}

				for (RequestHandle annotation : annotations) {
					for (METHODS reqMethod : annotation.methods()) {
						ArrayList<UrlPattern> listMethods = urlPatterns.get(reqMethod);
						if (null == listMethods) {
							listMethods = new ArrayList<UrlPattern>();
							urlPatterns.put(reqMethod, listMethods);
						}
						Path_oneValueEndStar pattern = new Path_oneValueEndStar(annotation.path(),annotation.name());
						UrlPattern urlPattern = new UrlPattern(pattern, controler, method, reqMethod);
						listMethods.add(urlPattern);
						uniqueNamedPaths.put(pattern.getName(), pattern);
					}
				}
			}
		}
		getNamedPaths().clear();
		for(String lKey:uniqueNamedPaths.keySet()) {
			getNamedPaths().add(uniqueNamedPaths.get(lKey));
		}
		
		
		show();
	}

	public void show() {
		for (METHODS m : urlPatterns.keySet()) {
			System.out.println("Method: " + m + "----");
			ArrayList<UrlPattern> listMethods = urlPatterns.get(m);
			for (UrlPattern urlPattern : listMethods) {
				System.out.println(urlPattern.pattern.getRawPattern() + " : " + urlPattern.obj.getName() + "."
						+ urlPattern.function.getName());
			}
			System.out.println("----------------");
		}
	}

	public ArrayList<Path_oneValueEndStar> getNamedPaths() {
		return namedPaths;
	}

	public class UrlPattern {
		public Path_oneValueEndStar pattern;
		public Class<?> obj;
		public Method function;
		public METHODS method;

		UrlPattern(Path_oneValueEndStar pattern, Class<?> obj, Method function, METHODS method) {
			this.pattern = pattern;
			this.obj = obj;
			this.function = function;
			this.method = method;
		}
	}

}
