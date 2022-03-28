package app.route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.route.enums.ElemPathType;


public class Path_oneValueEndStar {


	private String rawPattern;
	private List<ElemPath> elements;
	private String name="";
	
	public Path_oneValueEndStar(String pattern) throws IOException {
		this(pattern,"");
	}
	
	public Path_oneValueEndStar(String pattern,String name) throws IOException {
		if(pattern.isEmpty()) {
			throw new IOException("Path_oneValueEndStar is empty.");
		}
		rawPattern = pattern;
		this.name=name;
		if (!getRawPattern().endsWith("/")) {
			rawPattern = getRawPattern() + "/";
		}
		elements = new ArrayList<ElemPath>();

		String[] patternParts = pattern.split("/");
		if (patternParts.length==0) {
			ElemPath elem = new ElemPath(rawPattern);
			getElements().add(elem);
		}
		for (int i = 0; i <= patternParts.length - 1; ++i) {
			ElemPath elem = new ElemPath(patternParts[i]);
			if (elem.getType() == ElemPathType.star && i != patternParts.length - 1) {
				throw new IOException("Star can be only in the end path. Pattern: '" + getRawPattern() + "'.");
			}
			getElements().add(elem);
		}

		{
			ArrayList<String> checkValues = new ArrayList<String>();
			for (ElemPath elem : elements) {
				if (elem.getNameValue() != null) {
					if (checkValues.indexOf(elem.getNameValue()) == -1) {
						checkValues.add(elem.getNameValue());
					} else {
						throw new IOException("Not support multivalues.");
					}
				}
			}
		}
	}

	public static boolean isMatch(Path_oneValueEndStar pattern, String path,Map<String, String> out_values) throws IOException {
		out_values.clear();
		if (!path.endsWith("/")) {
			path += "/";
		}
		String[] pathPaths = path.split("/");
		if (pathPaths.length==0) {
			pathPaths = new String[] {"/"};
		}
		List<ElemPath> pathElements = pattern.getElements();
		boolean isMatch = true;

		String nowPartPath;
		ElemPath nowElem;
		
		int i;
		for (i = 0; i < pathPaths.length; ++i) {
			nowPartPath = pathPaths[i];

			if (i >= pathElements.size()) {
				isMatch = false;
				break;
			}
			nowElem = pathElements.get(i);
			if (nowElem.getType() == ElemPathType.star) {
				break;
			}
			if (nowElem.getType() == ElemPathType.value) {
				if (nowPartPath.matches(nowElem.getRegex())) {
					out_values.put(nowElem.getNameValue(), nowPartPath);
					continue;
				}
				isMatch = false;
				break;
			}

			if (nowElem.getType() == ElemPathType.common) {
				if (!nowPartPath.equals(nowElem.getRawPartPath())) {
					isMatch = false;
					break;
				}
				continue;
			}
			throw new IOException("Wonderful error.");
		}

		if (isMatch && i!=pathElements.size()) {
				isMatch=false;//end by end path, not pattern.
		}
		
		if (isMatch) {
			for (String key : out_values.keySet()) {
				System.out.println("Key: " + key + ": " + out_values.get(key));
			}
		}
		return isMatch;
	}
	public List<ElemPath> getElements() {
		return elements;
	}

	public String getRawPattern() {
		return rawPattern;
	}

	public String getName() {
		return name;
	}
}