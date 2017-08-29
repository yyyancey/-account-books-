package com.example.yancey.dailyapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<CostBean> mCostBeanList;
    private CostListAdapter mAdapter;
    private ListView costList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Connector.getDatabase();//创建数据库
        mCostBeanList = new ArrayList<>();
        initCostDate(); //初始化要放前头！！！
        //DataSupport.deleteAll(CostBean.class);
        costList = (ListView) findViewById(R.id.listView);
        mAdapter = new CostListAdapter(this, mCostBeanList);
        costList.setAdapter(mAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View dialog_view = inflater.inflate(R.layout.new_cost, null);
                final EditText title = dialog_view.findViewById(R.id.et_cost_title);
                final EditText money = dialog_view.findViewById(R.id.et_cost_money);
                final DatePicker date = dialog_view.findViewById(R.id.datePicker);
                builder.setView(dialog_view);
                builder.setTitle("新建账单");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (is_qualified(title, money)) {
                            CostBean costBean = new CostBean();
                            costBean.costTitle = title.getText().toString();
                            costBean.costMoney = money.getText().toString();
                            costBean.year = date.getYear();
                            costBean.month = date.getMonth();
                            costBean.day = date.getDayOfMonth();
                            costBean.costDate = date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDayOfMonth();
                            costBean.save();
                            mCostBeanList.add(costBean);
                            mAdapter.notifyDataSetChanged();
                        } else showAdvice(MainActivity.this, title, money);


                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });

        //修改项目
        costList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View dialog_view = inflater.inflate(R.layout.modify_view, null);

                final CostBean costBean = mCostBeanList.get(i);
                final EditText title = dialog_view.findViewById(R.id.re_cost_title);
                final EditText money = dialog_view.findViewById(R.id.re_cost_money);
                final DatePicker date = dialog_view.findViewById(R.id.re_cost_date);
                final int year, month, day;
                final boolean[] is_delete = new boolean[1];

                Button delete = dialog_view.findViewById(R.id.button);
                //初始化
                title.setText(costBean.costTitle);
                money.setText(costBean.costMoney);
                date.init(costBean.year, costBean.month, costBean.day, null);

                builder.setView(dialog_view);
                builder.setTitle("修改账单");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // CostBean costBean=new CostBean();
                        if (!is_delete[0] && is_qualified(title, money)) {
                            costBean.costTitle = title.getText().toString();
                            costBean.costMoney = money.getText().toString();
                            costBean.year = date.getYear();
                            costBean.month = date.getMonth();
                            costBean.day = date.getDayOfMonth();
                            costBean.costDate = costBean.year + "-" + (date.getMonth() + 1) + "-" + date.getDayOfMonth();
                            costBean.save();
                            costList.setAdapter(mAdapter);
                            //   mCostBeanList.add(costBean);
                            mAdapter.notifyDataSetChanged();
                        } else if (!is_delete[0]) showAdvice(MainActivity.this, title, money);
                    }
                });
                delete.setOnClickListener(new View.OnClickListener() {                              //delete item
                    @Override
                    public void onClick(View view) {
                        costBean.delete();
                        mCostBeanList.remove(i);
                        title.setText(null);
                        money.setText(null);
                        mAdapter.notifyDataSetChanged();
                        is_delete[0] = true;
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });
    }

    private void initCostDate() {
//        for (int i=0;i<11;i++) {
//            CostBean costBean=new CostBean();
//            costBean.costDate="11-11";
//            costBean.costTitle="狗肉";
//            costBean.costMoney="11.1";
//            mCostBeanList.add(costBean);
//            costBean.save();
//        }
        mCostBeanList = DataSupport.findAll(CostBean.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_chart) {
            Intent intent = new Intent(MainActivity.this, ChartsActivity.class);
            intent.putExtra("cast_list", (Serializable) mCostBeanList);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_delete) {
            DataSupport.deleteAll(CostBean.class);
            // CostBean costBean=new CostBean();
            // mCostBeanList.add(costBean);
            //   mCostBeanList.remove(0);
            //costList.setAdapter(mAdapter);
            mCostBeanList.clear();
            costList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    //提示输入正确信息
    void showAdvice(Context context, EditText editText1, EditText editText2) {
        if (TextUtils.isEmpty(editText1.getText()))
            Toast.makeText(context, "请输入名称！", Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(editText2.getText()))
            Toast.makeText(context, "请输入价格！", Toast.LENGTH_SHORT).show();
    }

    boolean is_qualified(EditText editText1, EditText editText2) {
        if (TextUtils.isEmpty(editText1.getText()) || TextUtils.isEmpty(editText2.getText()))
            return false;
        else return true;
    }
}
