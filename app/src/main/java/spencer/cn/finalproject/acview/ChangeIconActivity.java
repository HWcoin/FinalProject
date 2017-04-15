package spencer.cn.finalproject.acview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.ChangeIconAdapter;
import spencer.cn.finalproject.iexport.ISelectedItem;
import spencer.cn.finalproject.util.PublicVar;

public class ChangeIconActivity extends AppCompatActivity {
    public Button back;
    private RecyclerView cells;
    private int selectIcon = R.mipmap.ic_launcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_icon);

        this.initViews();
        this.setInteractions();
    }

    public void initViews(){
        back = (Button) findViewById(R.id.btn_back);
        cells = (RecyclerView) findViewById(R.id.rv_change_icons);
        cells.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        ChangeIconAdapter adapter = new ChangeIconAdapter(this);
        adapter.setCallback(new ISelectedItem() {
            @Override
            public void onSelected(int position) {
                selectIcon = PublicVar.ICONS[position];
            }
        });
        cells.setAdapter(adapter);

    }

    private void setInteractions() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentback = new Intent(ChangeIconActivity.this, MainSceneActivity.class);
                intentback.putExtra(PublicVar.CHANGE_ICON_NAME, selectIcon);

                setResult(RESULT_OK, intentback);
                finish();
            }
        });
    }
}
