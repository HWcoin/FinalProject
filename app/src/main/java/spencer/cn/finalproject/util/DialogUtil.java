package spencer.cn.finalproject.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Administrator on 2017/5/2.
 */

public class DialogUtil {

    public static void dialog(Context mContext, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(msg);
        builder.setTitle("Tips");
        builder.create().show();
    }
}
