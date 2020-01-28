package com.a1573595.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.a1573595.refreshlistview.RefreshListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RefreshListView.OnUpdateListener,
        RefreshListView.OnFailedListener {
    private RefreshListView refreshListView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> item = new ArrayList<>();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshListView = findViewById(R.id.refreshListView);
        refreshListView.setResetTime(3000L);
//        refreshListView.setRefreshEnable(false);
//        refreshListView.setLoadMoreEnable(false);
        refreshListView.setOnUpdateListener(this);
        refreshListView.setOnFailedListener(this);
        refreshListView.setListViewPadding(0, 0, 0, 24);
        refreshListView.setProgressBarColor(Color.CYAN);

        for(int i = 0; i < 20; i++) {
            item.add("Item" + i);
        }
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, item);
        refreshListView.setAdapter(arrayAdapter);
    }

    @Override
    public void onRefresh() {
        Log.e("MainActivity", "onRefresh");

        item.clear();
        for(int i = 0; i < 20; i++) {
            item.add("Item" + i);
        }

        arrayAdapter.notifyDataSetChanged();
        refreshListView.stopRefresh();

        refreshListView.setSelection(0);
    }

    @Override
    public void onLoadMore() {
        Log.e("MainActivity", "onLoadMore");

        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(() -> {
            if(item.size() < 50) {
                int size = item.size();

                for(int i = size; i < size + 20; i++) {
                    item.add("Item" + i);
                }

                arrayAdapter.notifyDataSetChanged();
                refreshListView.stopLoadMore();
            }
        }, 1500);
    }

    @Override
    public void onRefreshFailed() {
        Log.e("MainActivity", "onRefreshFailed");

        Toast.makeText(this, "Refresh failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadMoreFailed() {
        Log.e("MainActivity", "onLoadMoreFailed");

        Toast.makeText(this, "No more data", Toast.LENGTH_SHORT).show();
    }
}
