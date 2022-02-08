package app;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import views.BaseViews;

public class Router {
	static public class RouterFunction {
		String method;
		String pattern;
		String nameFunction;
		Class<?>obj;

		RouterFunction(String pattern, String method,Class<?>obj, String nameFunction) {
			this.pattern = pattern;
			this.method = method;
			this.obj = obj;
			this.nameFunction = nameFunction;
			if( !method.equals(Settings.POST) && !method.equals(Settings.GET) ) {
				throw new RuntimeException("Method: '"+method+"' not support.");
			}
		}
	}

	private List<RouterFunction> functions;
	private final static Router hide = new Router();
	RouterFunction page404;
	public RouterFunction cgi=null;
	Router() {
		functions = new ArrayList<RouterFunction>();
		page404=new RouterFunction("page not found404",Settings.GET, BaseViews.class, "page404");
		functions.add(page404);
	}

	public void call(Request req,Response resp) throws Exception   {
		for (RouterFunction routeFunction : functions) {
			if(!req.method.equals(routeFunction.method)) {
				continue;
			}
			if (req.uri.getPath().equals(routeFunction.pattern)) {
				System.out.println("Router: "+ routeFunction.obj.getName()+" / "+routeFunction.nameFunction+".");
				Method method = routeFunction.obj.getMethod(routeFunction.nameFunction, Request.class, Response.class);
				method.invoke(null, req, resp);
				return;
			}
		}
		
		if(null!=cgi) {
			System.out.println("Router: cgi: "+req.uri.getPath());
			Method method = cgi.obj.getMethod(cgi.nameFunction, Request.class, Response.class);
			method.invoke(null, req, resp);
			return;
		}
		System.out.println("Router: 404.");
		Method method = BaseViews.class.getMethod(page404.nameFunction, Request.class, Response.class);
		method.invoke(null, req, resp);
	}
	
	public void add(String pattern,String method, Class<?>obj, String nameFunction) {
		RouterFunction w = new RouterFunction(pattern,method, obj, nameFunction);
		this.functions.add(w);
	}

	public static Router getInstanse() {
		return hide;
	}

	public void checkSettingsRoute() {
		for (RouterFunction routeFunction : functions) {
			Class<?> classC = routeFunction.obj;
			
			try {
				Method tm = routeFunction.obj.getMethod(routeFunction.nameFunction, Request.class, Response.class);
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
				throw new RuntimeException("In class: " + classC.getName() + " in method: " + routeFunction.nameFunction
						+ " NoSuchMethodException | SecurityException.");
			}
			
			
			
			
			Method[] methodC = classC.getMethods();
			boolean isFind = false;
			Method find = null;
			for (int i = 0; i < methodC.length; ++i) {
				find = methodC[i];
				if (routeFunction.nameFunction == find.getName()) {
					isFind = true;
					break;
				}
			}
			if (!isFind) {
				throw new RuntimeException(
						"In class: " + classC.getName() + " not method: " + routeFunction.nameFunction + ".");
			}
			Class<?>[] parCheck = find.getParameterTypes();
			if (parCheck.length != 2) {
				throw new RuntimeException("In class: " + classC.getName() + " in method: " + routeFunction.nameFunction
						+ " wrong length parameters.");
			}

			if (parCheck[0].getName() != Request.class.getName()) {
				throw new RuntimeException("In class: " + classC.getName() + " in method: " + routeFunction.nameFunction
						+ " 0 parameters not Request.");
			}
			if (parCheck[1].getName() != Response.class.getName()) {
				throw new RuntimeException("In class: " + classC.getName() + " in method: " + routeFunction.nameFunction
						+ " 1 parameters not Response.");
			}

		}
	}
}
