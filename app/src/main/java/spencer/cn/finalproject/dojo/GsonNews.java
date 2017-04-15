package spencer.cn.finalproject.dojo;

import java.io.Serializable;
import java.util.ArrayList;

public class GsonNews implements Serializable{
	private int code;
	private ArrayList<News> data;
	private String message;
	public GsonNews(){

	}
	public GsonNews(String message, ArrayList<News> data, int code) {
		super();
		this.message = message;
		this.data = data;
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<News> getData() {
		return data;
	}

	public void setData(ArrayList<News> data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

//	private String reason;
//	private Result result;
//	private int error_code;
	
//	public GsonNews() {
//		super();
//	}
//
//	public GsonNews(String reason, Result result, int error_code) {
//		super();
//		this.reason = reason;
//		this.result = result;
//		this.error_code = error_code;
//	}
//
//	public String getReason() {
//		return reason;
//	}
//
//	public void setReason(String reason) {
//		this.reason = reason;
//	}
//
//	public Result getResult() {
//		return result;
//	}
//
//	public void setResult(Result result) {
//		this.result = result;
//	}
//
//	public int getError_code() {
//		return error_code;
//	}
//
//	public void setError_code(int error_code) {
//		this.error_code = error_code;
//	}
	
}
