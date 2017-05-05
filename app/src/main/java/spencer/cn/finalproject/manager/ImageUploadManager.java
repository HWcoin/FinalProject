package spencer.cn.finalproject.manager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import spencer.cn.finalproject.iexport.NewsCallBack;
import spencer.cn.finalproject.util.BitmapUtil;

/**
 * Created by Administrator on 2017/4/22.
 */

public class ImageUploadManager {
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private final static OkHttpClient client = new OkHttpClient();


    private static MultipartBody.Builder getImageBuilderWithBitmap(Context mContext, File file){
        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (file!=null) {
            builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        }
        return builder;
        //添加其它信息
    }

    private static MultipartBody.Builder getMultipartBodyWithBitmap(Context mContext, File file, HashMap<String, String> params){
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        Set<Map.Entry<String, String>> set = params.entrySet();
        Iterator<Map.Entry<String, String>> it =  set.iterator();
        while (it.hasNext()){
            Map.Entry<String, String> i = it.next();
            builder.addFormDataPart(i.getKey(), i.getValue());
        }
        return builder;
    }

    public static void postArtical(final String url, final Context mContext, final Bitmap bitmap, final HashMap<String, String> params, final NewsCallBack callBack){
        new Thread(){
            @Override
            public void run() {
                File cache =  BitmapUtil.saveBitmap2file(mContext, bitmap, "cache.png");

                MultipartBody requestBody = getMultipartBodyWithBitmap(mContext, cache, params).build();
                //构建请求
                Request request = new Request.Builder()
                        .url(url)//地址
                        .post(requestBody)//添加请求体
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("imgCallBack", e.getLocalizedMessage());

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (callBack != null){
                            String result = response.body().string();
                            Log.e("imgCallBack", result);
                            callBack.onNewsReturn(result);
                        }
                    }
                });
            }
        }.start();
    }

    public static void uploadImgWithBitmap(final String url, final Context mContext, final Bitmap bitmap, final NewsCallBack callBack) {
        new Thread(){
            @Override
            public void run() {
                File cache =  BitmapUtil.saveBitmap2file(mContext, bitmap, "cache.png");

                MultipartBody requestBody = getImageBuilderWithBitmap(mContext, cache).build();
                //构建请求
                Request request = new Request.Builder()
                        .url(url)//地址
                        .post(requestBody)//添加请求体
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("imgCallBack", e.getLocalizedMessage());

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (callBack != null){
                            String result = response.body().string();
                            Log.e("imgCallBack", result);
                            callBack.onNewsReturn(result);
                        }
                    }
                });
            }
        }.start();


    }


    ///////////////////////////////////////////////////////////////////////上传一张图
    public static File getRealPathFromURI(Context mContext, Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return new File(res);
    }


    private static MultipartBody.Builder getImageBuilder(Context mContext, Uri contentUri){
        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            File f = getRealPathFromURI(mContext, contentUri);
            if (f!=null) {
                builder.addFormDataPart("file", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        return builder;
        //添加其它信息
    }

    public static void uploadImg(String url, Context mContext, Uri uri, final NewsCallBack callBack) {

        MultipartBody requestBody = getImageBuilder(mContext, uri).build();
        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    Log.e("imgCallBack", e.getLocalizedMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callBack != null){
                    String result = response.body().string();
                    Log.e("imgCallBack", result);
                    callBack.onNewsReturn(result);
                }
            }
        });

    }

    ///////////////////////////////////////////////////////////////////////////////上传多张图片
    private static MultipartBody.Builder getImagesBuilder(HashMap<String, File> params){
        // mImgUrls为存放图片的url集合
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Set<HashMap.Entry<String, File>> set =  params.entrySet();
        Iterator<HashMap.Entry<String, File>> iterator = set.iterator();
        while (iterator.hasNext()){
            HashMap.Entry<String, File> item = iterator.next();
            File f = item.getValue();
            if (f!=null) {
                builder.addFormDataPart(item.getKey(), f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }

        return builder;
        //添加其它信息
//        builder.addFormDataPart("time",takePicTime);
//        builder.addFormDataPart("mapX", SharedInfoUtils.getLongitude());
//        builder.addFormDataPart("mapY",SharedInfoUtils.getLatitude());
//        builder.addFormDataPart("name",SharedInfoUtils.getUserName());
    }


    public static void uploadImgs(String url, HashMap<String, File> params, final NewsCallBack callBack) {

        MultipartBody requestBody = getImagesBuilder(params).build();
        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .post(requestBody)//添加请求体
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("imgCallBack", e.getLocalizedMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callBack != null){
                    String result = response.body().string();
                    Log.e("imgCallBack", result);
                    callBack.onNewsReturn(result);
                }
            }
        });

    }
}
