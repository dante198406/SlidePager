package org.ninetripods.mq.slidepager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.ninetripods.mq.slidepager.indicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    public static int item_grid_num = 15;//每一页中GridView中item的数量
    public static int number_columns = 5;//gridview一行展示的数目
    private ViewPager view_pager;
    private ViewPagerAdapter mAdapter;
    private List<DataBean> dataList;
    private List<GridView> gridList = new ArrayList<>();
    private CirclePageIndicator indicator;

    private PackageManager pm;
    private ImageView mIcon;
    private TextView mTitle;
    //private GridView mAllappsGridView;
    private List<ResolveInfo> mResolveInfoList;
    private List<ResolveInfo> mApplicationList = new ArrayList<>();
    private ResolveInfo mResolveInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initDatas();
    }

    private void initViews() {
        //初始化ViewPager
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        mAdapter = new ViewPagerAdapter();
        view_pager.setAdapter(mAdapter);
        dataList = new ArrayList<>();
        //圆点指示器
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setVisibility(View.VISIBLE);
        indicator.setViewPager(view_pager);
    }

    private void initDatas() {
        if (dataList.size() > 0) {
            dataList.clear();
        }
        if (gridList.size() > 0) {
            gridList.clear();
        }
        //初始化数据
        /*for (int i = 0; i < 60; i++) {
            DataBean bean = new DataBean();
            bean.name = "第" + (i + 1) + "条数据";
            bean.resId = R.mipmap.ic_launcher;
            bean.iconDrawable = getResources().getDrawable(R.mipmap.ic_launcher);
            dataList.add(bean);
        }*/
        pm = this.getPackageManager();
        Intent in = new Intent();
        in.setAction(Intent.ACTION_MAIN);
        in.addCategory(Intent.CATEGORY_LAUNCHER);
        mResolveInfoList = pm.queryIntentActivities(in, 0);
        for (int i = 0; i < mResolveInfoList.size(); i++) {
            mResolveInfo = mResolveInfoList.get(i);
            if (shouldIgnore(mResolveInfoList.get(i).toString())) {
                continue;
            }
            mApplicationList.add(mResolveInfo);
            ComponentName cn = new ComponentName(mResolveInfo.activityInfo.packageName,
                    mResolveInfo.activityInfo.name);
            in.setComponent(cn);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            Log.e("====", "=========" + mResolveInfo.loadLabel(pm));
            DataBean bean = new DataBean();
            bean.name = mResolveInfo.loadLabel(pm).toString();
            bean.iconDrawable = mResolveInfo.loadIcon(pm);
            dataList.add(bean);
        }
        //计算viewpager一共显示几页
        int pageSize = dataList.size() % item_grid_num == 0
                ? dataList.size() / item_grid_num
                : dataList.size() / item_grid_num + 1;
        for (int i = 0; i < pageSize; i++) {
            GridView gridView = new GridView(this);
            GridViewAdapter adapter = new GridViewAdapter(dataList, i);
            gridView.setNumColumns(number_columns);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("====", "=====position==" + position);
                    Intent in = new Intent();
                    mResolveInfo = mApplicationList.get(position);
                    ComponentName cn = new ComponentName(
                            mResolveInfo.activityInfo.packageName, mResolveInfo.activityInfo.name);
                    in.setComponent(cn);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    MainActivity.this.startActivity(in);
                }
            });
            gridList.add(gridView);
        }
        mAdapter.add(gridList);
    }

    private boolean shouldIgnore(String packageName) {
        return packageName.contains("com.baidu.BaiduMap")
                || packageName.contains("cn.kuwo.kwmusiccar")
                || packageName.contains("com.erobbing.launcher4")
                || packageName.contains("com.android.settings")
                || packageName.contains("com.autonavi.amapauto")
                //|| packageName.contains("com.erobbing.btdialer")
                || packageName.contains("com.sprd.appbackup")
                || packageName.contains("com.hdsc.edog")
                || packageName.contains("com.android.fmtx")
                || packageName.contains("com.sprd.fileexplorer")
                || packageName.contains("com.androits.gps.test.qcom")
                || packageName.contains("com.erobbing.hotspot")
                || packageName.contains("com.unisound.unicar.gui")
                || packageName.contains("com.spreadst.favorites")
                || packageName.contains("com.ximalaya.ting.android.car")
                || packageName.contains("com.ublox.ucenter")
                || packageName.contains("com.sprd.firewall")
                || packageName.contains("com.cmcc.homepage");
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_t_t:
                //3行3列
                number_columns = 3;
                item_grid_num = 9;
                break;
            case R.id.menu_t_f:
                //3行4列
                number_columns = 4;
                item_grid_num = 12;
                break;
            case R.id.menu_f_f:
                //4行4列
                number_columns = 4;
                item_grid_num = 16;
                break;
            case R.id.menu_f_fi:
                //4行5列
                number_columns = 5;
                item_grid_num = 15;
                break;
            default:
                break;
        }
        initDatas();
        return super.onOptionsItemSelected(item);
    }*/
}
