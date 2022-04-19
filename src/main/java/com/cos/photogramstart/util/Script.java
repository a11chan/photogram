package com.cos.photogramstart.util;

public class Script {

	public static String back(String msg) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		sb.append("alert('"+msg+"');"); // 변수 넣기 "+변수명+"
		sb.append("history.back();");
		sb.append("</script>");
		return sb.toString();
	}
}
