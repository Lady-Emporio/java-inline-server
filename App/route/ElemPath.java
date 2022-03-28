package app.route;

import java.io.IOException;

import app.route.enums.ElemPathType;


public class ElemPath {
	private String rawPartPath;
	private ElemPathType type;

	private String nameValue = null;
	private String regexValue = null;

	public ElemPath(String partPath) throws IOException {
		rawPartPath = partPath;
		type = ElemPathType.common;

		if (partPath.equals("*")) {
			type = ElemPathType.star;
		}
		int begin = rawPartPath.indexOf("{");
		int end = rawPartPath.indexOf("}");

		if (begin != -1) {
			type = ElemPathType.value;

			if (begin != 0 || end != rawPartPath.length() - 1) {
				throw new IOException(
						"Part pattern not support multy values. One part - one value. Piece pattern: '"
								+ rawPartPath + "'.");
			}
			String rawPair = rawPartPath.substring(begin + 1, end);
			String[] pair = rawPair.split(":");
			if (pair.length == 1) {
				nameValue = pair[0];
				regexValue = ".+";
			} else if (pair.length == 2) {
				nameValue = pair[0];
				regexValue = pair[1];
			} else {
				throw new IOException("Not found pair value:path in pattern:" + rawPartPath + "");
			}

		}
	}
	
	public ElemPathType getType() {
		return type;
	}

	public String getRawPartPath() {
		return rawPartPath;
	}

	public String getRegex() {
		return regexValue;
	}

	public String getNameValue() {
		return nameValue;
	}
}