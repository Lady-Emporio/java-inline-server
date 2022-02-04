package app;

import java.util.ArrayList;
import java.util.List;



public class Router {
	private List<String> paths;
	private List<View> handlies;
	private final static Router hide=new Router();
	private View view_404;
	Router(){
		paths=new ArrayList<String>();
		handlies=new ArrayList<View>();
	}
	
	public void add(String path,View handle) {
		paths.add(path);
		handlies.add(handle);
	}
	public static Router getInstanse() {
		return hide;
	}
}
