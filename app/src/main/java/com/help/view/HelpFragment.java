package com.help.view;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.github.clans.fab.CircleImageView;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.help.R;
import com.help.api.API;
import com.help.app.APP;
import com.help.config.IGetMapLocation;
import com.help.model.bean.HelpContact;
import com.help.service.LocationService;
import com.help.util.Util;
import com.help.widge.CircleButtonWithProgerss;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by gan on 2016/6/3.
 */
public class HelpFragment extends BaseFragment implements View.OnClickListener {

    private FloatingActionMenu mFbMenu;
    private FloatingActionButton mFbAdd;
    private View view;
    private ServiceConnection sc;
    private TextView mTvPosition;
    private ImageView mIvRefresh;
    private String currentPosition;
    static private boolean firstLocation = true;
    private CallListAdapter mAdapter;
    private List<HashMap<String, Object>> mData;
    private CircleButtonWithProgerss mcbp;
    private List<HelpContact> mList;
   /* public List<HashMap<String, Object>> getmData() {
        return mData;
    }*/

    public HelpFragment() {

    }



    public List<HelpContact> getmList() {
        return mList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        firstLocation = true;
        initData();
        initView();
        initListener();
        initService();
        initAdapter();
        return view;
    }

    private void initData() {
        Bundle args = getArguments();
        mData = new ArrayList<HashMap<String, Object>>();
        mList = new ArrayList<>();
        try {
            TestContact();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private void initAdapter() {
        mAdapter = new CallListAdapter(getContext());
    }


    @Override
    public void onResume() {
        super.onResume();
        initContacts();
    }

    private void initContacts() {
        RealmResults<HelpContact> contacts = APP.getAllContact();
        mFbMenu.removeAllMenuButtons(R.id.fb_add);
        for (HelpContact contact : contacts) {
            addContect(contact);
            mList.add(contact);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_help;
    }

    private void initService() {
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LocationService locationService = ((LocationService.LocationBinder) service).getService();
                locationService.initLocation(new IGetMapLocation() {
                    @Override
                    public void getLocationSuccess(AMapLocation amapLocation) {
                        currentPosition = amapLocation.getAddress();
                        if (firstLocation) {
                            mTvPosition.setText(currentPosition);
                            firstLocation = false;
                        }
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent service = new Intent(getActivity(), LocationService.class);
        getActivity().bindService(service, sc, Context.BIND_AUTO_CREATE);
    }

    private void initListener() {
        mFbAdd.setOnClickListener(this);
        mIvRefresh.setOnClickListener(this);
        mcbp.setOnClickListener(this);
    }

    private void initView() {
        mcbp = (CircleButtonWithProgerss) view.findViewById(R.id.cbp);
        mFbMenu = (FloatingActionMenu) view.findViewById(R.id.fb_menu);
        mFbAdd = (FloatingActionButton) view.findViewById(R.id.fb_add);
        mTvPosition = (TextView) view.findViewById(R.id.tv_positon);
        mIvRefresh = (ImageView) view.findViewById(R.id.iv_refresh);
    }

    private void addContect(HelpContact contact) {
        CircleImageView circleImageView = new CircleImageView(getActivity());
        circleImageView.setOnClickListener(this);
        if (!contact.head.isEmpty())
            Picasso.with(getActivity()).load(new File(contact.getHead())).into(circleImageView);
        else
            Picasso.with(getActivity()).load(R.mipmap.dl_example_head).into(circleImageView);
        mFbMenu.addMenuButton(circleImageView);
        circleImageView.setTag(contact.getContactNo());
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(sc);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh:
                mTvPosition.setText(currentPosition);
                break;
            case R.id.fb_add:
                initData();
                onCreateDialog(mAdapter);

                break;
            case R.id.cbp:
                Toast.makeText(getContext(), "开始呼救", Toast.LENGTH_SHORT).show();
                ((HelpActivity)getActivity()).SOS();
                for (int i = 0; i < getmList().size(); i++) {
                    ((HelpActivity)getActivity()).sendSms(getmList().get(i).getTel(), getmList().get(i).getName() + getmList().get(i).getSmsText() + "我的IMEI码是" + Util.getIMEI(getContext()) + "下载云互助app可以查询帮助我！" + "     " + getmList().get(i).getContactNo());
                }
                break;
            default:
                getActivity().startActivity(new Intent(getActivity(), ContactActivity.class).putExtra(API.KEY_CONTACT_NO, (long) v.getTag()));
                break;
        }
    }


    public void TestContact() throws Exception {
        HashMap<String, Object> map = null;
        Uri uri = Uri.parse("content://com.android.contacts/contacts");
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
        while (cursor.moveToNext()) {
            map = new HashMap<String, Object>();
            int contractID = cursor.getInt(0);
            StringBuilder sb = new StringBuilder("contractID=");
            sb.append(contractID);
            uri = Uri.parse("content://com.android.contacts/contacts/" + contractID + "/data");
            Cursor cursor1 = resolver.query(uri, new String[]{"mimetype", "data1", "data2"}, null, null, null);
            while (cursor1.moveToNext()) {
                String data1 = cursor1.getString(cursor1.getColumnIndex("data1"));
                String mimeType = cursor1.getString(cursor1.getColumnIndex("mimetype"));
                if ("vnd.android.cursor.item/name".equals(mimeType)) { //是姓名
                    map.put("name", data1);
                } else if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) { //手机
                    map.put("number", data1);
                }

            }
            cursor1.close();
            mData.add(map);
        }
        cursor.close();

    }


  /*  public Bitmap getHeadImg(String strPhoneNumber) {
        Bitmap btContactImage = null;
        Uri uriNumber2Contacts = Uri
                .parse("content://com.android.contacts/"
                        + "data/phones/filter/" + strPhoneNumber);
        Cursor cursorCantacts = getContext()
                .getContentResolver().query(uriNumber2Contacts, null, null,
                        null, null);
        if (cursorCantacts.getCount() > 0) {    //若游标不为0则说明有头像,游标指向第一条记录
            cursorCantacts.moveToFirst();
            Long contactID = cursorCantacts.getLong(cursorCantacts
                    .getColumnIndex("contact_id"));
            Uri uri = ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI, contactID);
            InputStream input = ContactsContract.Contacts
                    .openContactPhotoInputStream(getContext().getContentResolver(), uri);
            btContactImage = BitmapFactory.decodeStream(input);
            Log.i("info", "bt======" + btContactImage);

        }
        return btContactImage;
    }
*/

    public void onCreateDialog(BaseAdapter adapter) {
        DialogPlus dialog = DialogPlus.newDialog(getContext())
                .setAdapter(adapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        //getHeadImg(mData.get(position).get("number").toString())
                        if (APP.getAllContact().size() < 4) {
                            HelpContact contact = new HelpContact(getActivity(), mData.get(position).get("number").toString(), mData.get(position).get("name").toString());
                            addContect(contact);
                            APP.add(contact);
                            mList.add(contact);
                        } else {
                            Util.Toast(getActivity(), "人数已满");
                            Log.e(TAG, "人数已经满了");
                        }
                        dialog.dismiss();
                    }
                })
                .setExpanded(true)// This will enable the expand feature, (similar to android L share dialog)
                .create();
        dialog.show();
    }


    class CallListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;// 动态布局映射

        public CallListAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        // 决定ListView有几行可见
        @Override
        public int getCount() {
            return mData.size();// ListView的条目数
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.friend_list_item, null);//根据布局文件实例化view
            TextView title = (TextView) convertView.findViewById(R.id.tv_name);//找某个控件
            title.setText(mData.get(position).get("name").toString());//给该控件设置数据(数据从集合类中来)
            TextView time = (TextView) convertView.findViewById(R.id.tv_number);//找某个控件
            if (mData.get(position).get("number").toString()!=null){
                time.setText(mData.get(position).get("number").toString());//给该控件设置数据(数据从集合类中来)
            }
            return convertView;
        }
    }


}
