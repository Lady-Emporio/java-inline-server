package app.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.linkbuilder.ILinkBuilder;

import app.ClearSky;
import app.route.ElemPath;
import app.route.Path_oneValueEndStar;
import app.route.Router;
public class MyLinkBuilder implements ILinkBuilder{

	@Override
	public String getName() {
		ClearSky.log("getName()");
		return "wtf";
	}

	@Override
	public Integer getOrder() {
		ClearSky.log("getOrder()");
		return 0;
	}

	@Override
	public String buildLink(IExpressionContext context, String base, Map<String, Object> parameters){
		for (Path_oneValueEndStar path:Router.Instanse().getNamedPaths()) {
			if(base.equals(path.getName())) {
				StringBuilder finalString=new StringBuilder();
				for( ElemPath elem:path.getElements()) {
					if(elem.getNameValue()==null) {
						finalString.append(elem.getRawPartPath());
						finalString.append("/");
						continue;
					}
					Object value=parameters.get(elem.getNameValue());
					String strVal=value.toString();
					finalString.append(strVal);
					finalString.append("/");
				}
				return finalString.toString();
			}
			
		}
		throw new RuntimeException("Name: '"+base+"' not found in link builder.");
	}

}
