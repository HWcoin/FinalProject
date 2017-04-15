package spencer.cn.finalproject.fragment;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/3/5.
 */

public class FragmentDemo extends Fragment {
    /*
    必须实现的三个回调函数
　　onCreate()
　　系统在创建Fragment的时候调用这个方法，这里应该初始化相关的组件，
    一些即便是被暂停或者被停止时依然需要保留的东西。
    onCreateView()
　　当第一次绘制Fragment的UI时系统调用这个方法，必须返回一个View，
    如果Fragment不提供UI也可以返回null。
    注意，如果继承自ListFragment，onCreateView()默认的实现会返回一个ListView，所以不用自己实现。
    onPause()
　　当用户离开Fragment时第一个调用这个方法，需要提交一些变化，
    因为用户很可能不再返回来。
    */
}
