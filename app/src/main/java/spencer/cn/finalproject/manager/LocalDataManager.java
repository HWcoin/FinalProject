package spencer.cn.finalproject.manager;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import spencer.cn.finalproject.dojo.BaseNewType;
import spencer.cn.finalproject.dojo.GsonNews;
import spencer.cn.finalproject.dojo.News;
import spencer.cn.finalproject.util.PublicVar;

/**
 * Created by Administrator on 2017/4/9.
 */

public class LocalDataManager {
    public static ArrayList<News> caches = new ArrayList<>();;
    ////////////////////////////////////////////从本地加载历史记录
    public static ArrayList<News> loadCaches(Context mContext) {
        File cache = new File(mContext.getFilesDir(), PublicVar.HISTORY);
        if (!cache.exists()){
            try {
                cache.createNewFile();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("newsLog", "创建历史记录文件失败");
            }
        }

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(cache));
            GsonNews gsonNews = (GsonNews) ois.readObject();
            if (gsonNews != null){
                LocalDataManager.caches = gsonNews.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return LocalDataManager.caches;
    }
    /////////////////////////////////////////////将历史记录加到本地
    public static void storeCaches(Context mContext, ArrayList<News> caches){
        GsonNews gsonNews = new GsonNews("history", caches, 200);
        File cache = new File(mContext.getFilesDir(),PublicVar.HISTORY);
        //不存在就创建,存在替换
        if (!cache.exists()){
            try {
                cache.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(cache));
            oos.writeObject(gsonNews);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("缓存写入失败");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupport Encoding Exception");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //////////////////////////////////////////////////保存一条新闻记录
    public static void keepNewsRecord(News record){
        News target = null;
        for (int i=0; i<LocalDataManager.caches.size(); i++){
            if (record.getUniquekey().equals(LocalDataManager.caches.get(i).getUniquekey())){
                target = LocalDataManager.caches.get(i);
            }
        }
        if (target != null){
            LocalDataManager.caches.remove(target);
        }
        LocalDataManager.caches.add(0, record);
    }
    /////////////////////////////////////////////////是否存在游客配置
    public static boolean isBaseConfigExists(Context mContext){
        File cache = new File(mContext.getFilesDir(), PublicVar.BASECONFIG);
        if(!cache.exists()) return false;
        if (loadBaseConfig(mContext).getData().size() > 0) return true;
        return false;
    }
    /////////////////////////////////////////////////保存游客配置
    public static BaseNewType loadBaseConfig(Context mContext){
        File cache = new File(mContext.getFilesDir(), PublicVar.BASECONFIG);
        ObjectInputStream bois = null;
        if (!cache.exists()){
            try {
                cache.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("newsLog", "创建游客记录文件失败");
            }
        }

        try {
            bois = new ObjectInputStream(new FileInputStream(cache));
            BaseNewType baseTypes = (BaseNewType) bois.readObject();
            return baseTypes;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if (bois != null){
                try {
                    bois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /////////////////////////////////////////////////读取游客配置
    public static void storeBaseConfig(Context mContext, BaseNewType baseTypes){
        File cache = new File(mContext.getFilesDir(),PublicVar.BASECONFIG);
        //不存在就创建,存在替换
        ObjectOutputStream boos = null;
        if (!cache.exists()){
            try {
                cache.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            boos = new ObjectOutputStream(new FileOutputStream(cache));
            boos.writeObject(baseTypes);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("游客配置数据写入失败");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupport Encoding Exception");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (boos != null) try {
                boos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 从缓存中加载新闻数据,加载到成员变量datas
     */
    public static GsonNews popFromCache(Context mContext, String filename){
        File cache = new File(mContext.getFilesDir(), filename);
        if (!cache.exists()){
            return null;
        }else {
            ObjectInputStream fis = null;
            try {
                fis = new ObjectInputStream(new FileInputStream(cache));
                GsonNews gsonNews = (GsonNews) fis.readObject();
                return gsonNews;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }finally {
                if (fis != null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
    /**
     * 把新的新闻数据加入缓存
     */
    public static void  pullToCache(Context mContext, GsonNews result, String filename){
        File cache = new File(mContext.getFilesDir(), filename);
        //不存在就创建,存在替换
        if (!cache.exists()){
            try {
                cache.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ObjectOutputStream fos = null;
        try {
            fos = new ObjectOutputStream(new FileOutputStream(cache));
            fos.writeObject(result);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("缓存写入失败");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupport Encoding Exception");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
