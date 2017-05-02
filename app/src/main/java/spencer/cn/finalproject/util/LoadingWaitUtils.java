package spencer.cn.finalproject.util;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingWaitUtils {
	private Context context;
	private ProgressDialog pb;
	public LoadingWaitUtils(Context context) {
		this.context = context;
		pb = new ProgressDialog(context);
		pb.setCancelable(false);
		pb.setIndeterminate(false);
		pb.setMessage("拼命加载中...");
	}
	public void show(){
		pb.show();
	}
	public void cancel(){
		pb.cancel();
	}
}
