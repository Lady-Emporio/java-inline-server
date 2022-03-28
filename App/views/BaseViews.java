package app.views;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.route.annotations.RequestHandle;
import app.route.enums.METHODS;
import app.server.MyTestFunctional;
import app.server.Request;
import app.server.Response;
import app.server.Templates;

public class BaseViews {
	public static void page404(Request req,Response resp) throws IOException {
		resp.sendFileFromBASE_DIR("page404.html");
	}
	
	@RequestHandle(path = "/",name="menuPage")
	public static void indexGet(Request req,Response resp) throws IOException{
		//resp.sendAll("GET Main page."+new Date());
		List<MyTestFunctional.TestHref> pagesList=new ArrayList<MyTestFunctional.TestHref>();
		for(int i=0;i<10;++i) {
			pagesList.add(new MyTestFunctional.TestHref(i,"ThisIs"+i));
		}
		resp.sendAll(Templates.getTemplateFromMap("index.html",Map.of(
			      "today", new Date().toString(),
			      "utfTime", ""+new Date().getTime(),
			      "myPages",pagesList
			  ) ));
	}
	@RequestHandle(path = "/admins")
	public static void indexAdminsGet(Request req,Response resp) throws IOException{
		//resp.sendAll("GET Main page."+new Date());
		resp.sendAll(Templates.getTemplateFromMap("admins/index.html", new HashMap<String, Object>()));
	}
	
	@RequestHandle(path = "/",methods = METHODS.POST)
	public static void indexPost(Request req,Response resp) throws IOException{
		resp.sendAll("Main page POST."+new Date());
	}
	
	@RequestHandle(path = "/w",methods = METHODS.GET)
	public static void test1(Request req,Response resp) throws IOException{
		resp.sendAll("We are "+new Date());
	}
	
	@RequestHandle(path = "/w/{TestVal}",methods = {METHODS.GET,METHODS.POST},name="OpenByIdTest")
	public static void test2(Request req,Response resp) throws IOException{
		resp.sendAll("Test :'"+req.pathValues.get("TestVal")+"' "+new Date());
	}
	
	@RequestHandle(path = "/*",methods = {METHODS.GET,METHODS.POST})
	public static void test3(Request req,Response resp) throws IOException{
		resp.sendAll("wtf??? "+new Date());
	}
}



