package app.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public class Templates {
	private static TemplateEngine templateEngine = null;
	private static String thymeleaf_templates=null;
	static {
		try {
			thymeleaf_templates=getTemplate("component/header_nav.html");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		};
	}
	public static String getTemplate(String name) throws IOException {
		FileInputStream inputStream = new FileInputStream(Settings.TEMPLATES_DIR + "/" + name);
		String text = (thymeleaf_templates==null?"":thymeleaf_templates)+new String(inputStream.readAllBytes());
		inputStream.close();
		return text;
	}

	private static TemplateEngine getTemplateEngine() {
		if (null == templateEngine) {
			templateEngine = new TemplateEngine();
			StringTemplateResolver templateResolver = new StringTemplateResolver();
			templateResolver.setTemplateMode(TemplateMode.HTML);
			templateEngine.setTemplateResolver(templateResolver);
			templateEngine.setLinkBuilder(new MyLinkBuilder());
		}
		return templateEngine;
	}

	public static String getTemplateFromMap(String nameTemplate, Map<String, Object> dynamicAttibutesMap) throws IOException {
		templateEngine = getTemplateEngine();
		
		final Context ctx = new Context();
		for(String key:dynamicAttibutesMap.keySet()) {
			ctx.setVariable(key, dynamicAttibutesMap.get(key));
		}

		String htmlContent=getTemplate(nameTemplate);
		String template = templateEngine.process(htmlContent, ctx);
		return template;
	}
}
