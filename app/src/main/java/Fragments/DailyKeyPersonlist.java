package Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import DBUtils.DBOpenHelper;
import Utils.CommonAdapter;
import Utils.ViewHolder;
import zj.com.mc.AddRemoveKeyperson;
import zj.com.mc.R;
import zj.com.mc.UtilisClass;

/**
 * Created by dell on 2016/8/3.
 */
public class DailyKeyPersonlist extends BaseFragment {

    private DBOpenHelper dbOpenHelper;
    private ListView addlv;
    private List<Map> list;
    private CommonAdapter<Map> adapter;
    private Intent intentkeyadd;
    private Bundle bundlekeyadd;
    private String personId;
    private String personChnagedate;
    private TextView daily_addremovekp_keyperson,daily_addremovekp_keystation,daily_addremovekp_address,daily_addremovekp_data,dodata1;


    @Override
    View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tianchengdetail,container,false);
        addlv= (ListView) view.findViewById(R.id.daily_tiancheng_list);
        view.findViewById(R.id.dodata1).setOnClickListener(this);
        personId=getActivity().getSharedPreferences("PersonInfo",Context.MODE_PRIVATE).getString("PersonId",null);
        title.setText("关键人历史记录");
        settitleviewdata(view);

        personChnagedate=UtilisClass.getStringDate2();
        addlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intentkeyadd = new Intent(getActivity(), AddRemoveKeyperson.class);
                bundlekeyadd = new Bundle();
                String id=list.get(i).get("Id")+"";
                bundlekeyadd.putInt("listitemId", Integer.parseInt(id));

                intentkeyadd.putExtra("InstructorKeyPerson", bundlekeyadd);
                startActivity(intentkeyadd);
            }
        });
        return view;
    }

    private void settitleviewdata(View view){
        daily_addremovekp_keyperson      = (TextView) view.findViewById(R.id.daily_addremovekp_keyperson);
        daily_addremovekp_keystation     = (TextView) view.findViewById(R.id.daily_addremovekp_keystation);
        daily_addremovekp_address         = (TextView) view.findViewById(R.id.daily_addremovekp_address);
        daily_addremovekp_data              = (TextView) view.findViewById(R.id.daily_addremovekp_data);
        dodata1                             = (TextView) view.findViewById(R.id.dodata1);

        daily_addremovekp_keyperson.setText("关键人");
                daily_addremovekp_keystation.setText("关键点");
        daily_addremovekp_address.setText("关键点确定原因");
                daily_addremovekp_data.setText("确定日期");
        dodata1.setText("解除时间");

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){

            case R.id.tainjia_shuju:

                break;
            case R.id.daily_tiancheng_spmonth:
                break;

        }

    }

    @Override
    protected void setDateChanged(String date) {
        personChnagedate=date;
        list=dbOpenHelper.queryListMap("select * from InstructorKeyPerson where ConfirmDate like ? and InstructorId=? and IsRemoved=?",

                new String[]{date+"%",personId,"true"});
        if (list.size()!=0) {
            Collections.reverse(list);
            adapter = new PersonAdapter(getActivity(), list, R.layout.classtiems);
            addlv.setAdapter(adapter);
        }else {
            UtilisClass.showToast(getActivity(),"搜索结果为空!");
        }

    }

    @Override
    protected void setTestButton() {
    }

    @Override
    protected void setDataButton() {
        //添加记录
    }



    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();


        dbOpenHelper= DBOpenHelper.getInstance(getActivity().getApplicationContext());
        list=dbOpenHelper.queryListMap("select * from InstructorKeyPerson where ConfirmDate like ? and InstructorId=? and IsRemoved=?",
                new String[]{personChnagedate+"%",personId,"true"});
        adapter = new PersonAdapter(getActivity(), list, R.layout.classtiems);
        addlv.setAdapter(adapter);



    }

    class PersonAdapter extends CommonAdapter<Map> {

        public PersonAdapter(Context context, List<Map> datas, int item_id) {
            super(context, datas, item_id);
        }
        @Override
        protected void convertlistener(final ViewHolder holder, final Map map) {
        }

        @Override
        public void convert(ViewHolder holder, Map map) {
            String keypersonid=map.get("KeyPersonId")+"";
            String keyName=UtilisClass.getName(dbOpenHelper,keypersonid);
            holder.setText(R.id.daily_Listing_itemmessage1, keyName);
            holder.setText(R.id.daily_Listing_itemtype1,String.valueOf(map.get("KeyLocation")));
            holder.setText(R.id.daily_Listing_itemnumber1, map.get("LocationConfirmReason") + "");
            holder.setText(R.id.daily_Listing_itemtime1, map.get("ConfirmDate") + "");
            holder.setText(R.id.daily_Listing_itemdo1,map.get("ActualRemoveTime") + "");

        }
    }
}
