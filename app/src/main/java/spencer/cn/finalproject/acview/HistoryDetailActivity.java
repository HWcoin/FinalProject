package spencer.cn.finalproject.acview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import spencer.cn.finalproject.R;
import spencer.cn.finalproject.adapter.HistoryRecordAdapter;
import spencer.cn.finalproject.manager.LocalDataManager;

public class HistoryDetailActivity extends AppCompatActivity {
    private ListView historyRecord;
    private HistoryRecordAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        historyRecord = (ListView) findViewById(R.id.lv_history);
        adapter = new HistoryRecordAdapter(this, LocalDataManager.caches);
        historyRecord.setAdapter(adapter);
    }
}
