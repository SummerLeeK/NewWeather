package com.example.lee.newweather.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lee.newweather.DaoMaster;
import com.example.lee.newweather.DaoSession;
import com.example.lee.newweather.MyCity;
import com.example.lee.newweather.MyCityDao;
import com.example.lee.newweather.R;
import com.example.lee.newweather.adapter.MyCityAdapter;
import com.example.lee.newweather.adapter.MyCityRecycleViewAdapter;
import com.example.lee.newweather.greendao.DBManager;

import java.util.List;

/**
 * Created by lee on 17-5-25.
 */

public class MyCityActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView back;
    private TextView title;
    private RecyclerView mycitylistview;
    private LinearLayout linearLayout;
    private MyCityRecycleViewAdapter adapter;
    private Context mContext;
    private List<MyCity> myCities;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.mycity_activity);
        DBManager.initDBWithMyCity(mContext);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        mycitylistview = (RecyclerView) findViewById(R.id.mycity_list);
        linearLayout = (LinearLayout) findViewById(R.id.linear);

        mycitylistview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mycitylistview.setItemAnimator(new DefaultItemAnimator());

        mycitylistview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                outRect.set(0, 5, 0, 5);
            }
        });


        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, 1 << 2 | 1 << 3) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {

                    final int postion = viewHolder.getAdapterPosition();
                    final MyCity myCity = myCities.get(postion);
                    DBManager.deleteFromMyCity(myCity,mContext);
                    myCities.remove(postion);
                    adapter.notifyItemRemoved(postion);
                    Snackbar.make(linearLayout, "删除成功,下次启动时更改", Snackbar.LENGTH_LONG).setAction("撤销", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            myCities.add(postion, myCity);
                            DBManager.insertOrReplaceInMyCity(myCity,mContext);
                            adapter.notifyDataSetChanged();

                        }
                    }).show();


                }


            }

            //处理动画
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    //滑动时改变 Item 的透明度，以实现滑动过程中实现渐变效果
                    if (dX < 0) {
                        final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                        viewHolder.itemView.setAlpha(alpha);
                        viewHolder.itemView.setTranslationX(dX);
                    }
                }

            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

            }
        });

        touchHelper.attachToRecyclerView(mycitylistview);

        title.setText(R.string.mycity);
        back.setOnClickListener(this);

        myCities = DBManager.getAllMycity(mContext);
        adapter = new MyCityRecycleViewAdapter(myCities, this);
        mycitylistview.setAdapter(adapter);

    }



    @Override
    public void onClick(View v) {
        onBackPressed();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
