package spencer.cn.finalproject.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.util.PublicVar;

/**
 * Created by Administrator on 2017/4/24.
 */

public class CreateNews extends BaseFragment {
    private ImageView pic;
    private EditText title;
    private EditText content;
    private Button post;
    private Bitmap _targetBitmap;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_create_news, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        pic = (ImageView) view.findViewById(R.id.iv_xiaozhong_icon);
        title = (EditText) view.findViewById(R.id.edt_edit_xiaozhong_title);
        content = (EditText) view.findViewById(R.id.edt_edit_xiaozhong_content);
        post = (Button) view.findViewById(R.id.btn_post);

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                        PublicVar.GET_PIC_FOR_XIAOZHONG);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String contentText = content.getText().toString();
                if (TextUtils.isEmpty(titleText) || TextUtils.isEmpty(contentText)){
                    Toast.makeText(getActivity(), "标题或内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });
    }

    public void setArticleBitmap(Bitmap bit){
        pic.setImageBitmap(bit);
        _targetBitmap = bit;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
