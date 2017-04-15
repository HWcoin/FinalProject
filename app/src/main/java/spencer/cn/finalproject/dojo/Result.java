package spencer.cn.finalproject.dojo;

import java.io.Serializable;
import java.util.ArrayList;


public class Result implements Serializable{
//	private int start;
	private ArrayList<News> data;
	
	
	
	public Result() {
		super();
	}
	public Result(ArrayList<News> data) {
		super();
//		this.start = start;
		this.data = data;
	}
//	public int getStart() {
//		return start;
//	}
//	public void setStart(int start) {
//		this.start = start;
//	}
	public ArrayList<News> getData() {
		return data;
	}
	public void setData(ArrayList<News> data) {
		this.data = data;
	}
}
