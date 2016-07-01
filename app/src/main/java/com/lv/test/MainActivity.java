package com.lv.test;

import android.Manifest;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.wx.goodview.GoodView;

import java.util.LinkedHashMap;
import java.util.Map;

import de.greenrobot.dao.query.QueryBuilder;
import greendao.DaoMaster;
import greendao.DaoSession;
import greendao.Patient;
import greendao.TestBean2;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity {
    protected static final String NAMESPACE = "http://tempuri.org/";
    protected static String URL = "http://app.wumart.com/Mobile/WMHQAppWebService.asmx";
    private static final String METHOD_NAME = "WCFService";
    private Realm realm;
    private DaoSession daoSession;
    private static final int REQUEST_CODE_CAMERA =101 ;
    Button button;
    PermissionManager helper;
    private int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GoodView goodView = new GoodView(this);
         button = (Button) findViewById(R.id.test);
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setStorage(HawkBuilder.newSharedPrefStorage(this))
                .build();
        realm = Realm.getInstance(new RealmConfiguration.Builder(this)
                .name("myOtherRealm.realm")
                .build());
        setupDatabase();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        TestBean testBean;
                        long l = System.currentTimeMillis();
                        for (int i = 0; i <10 ; i++) {
                            testBean=new TestBean();
                            testBean.setId(0);
                            realm.copyToRealmOrUpdate(testBean);
                        }
                        Log.d("MainActivity", ((System.currentTimeMillis() - l) + "   realm"));
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "sdafasd", Toast.LENGTH_SHORT).show();
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TestBean2 testBean;
                        long l = System.currentTimeMillis();
                        for (int i = 0; i <10 ; i++) {
                            testBean=new TestBean2();
                            testBean.setId(i);
                            Hawk.put("wode"+i,testBean);
                        }
                        Log.d("MainActivity", ((System.currentTimeMillis() - l) + "   Hawk"));
                    }
                }).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TestBeanX testBean;
                        long l = System.currentTimeMillis();
                        for (int i = 0; i <10 ; i++) {
                            testBean=new TestBeanX();
                            testBean.save();
                        }

                        Log.d("MainActivity", ((System.currentTimeMillis() - l) + "   litapal"));
                    }
                }).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long l = System.currentTimeMillis();
                        for (int i = index; i <index+10 ; i++) {
                            daoSession.getPatientDao().insert(new Patient(""+i, "asdf", "asdf"));
                        }

                        Log.d("MainActivity", ((System.currentTimeMillis() - l) + "   daoSession"));
                        index+=10;

                    }
                }).start();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String,String> map= new LinkedHashMap<>();
                        map.put("funcCode","104");
                        map.put("xmlStr","pwd");

                                WebServiceUtil webServiceUtil = new WebServiceUtil();
                        String s = webServiceUtil.getString(URL, NAMESPACE, METHOD_NAME, map);
                        if(s!=null)
                            Log.d("MainActivity", s);
                        String getList = webServiceUtil.getString(URL, NAMESPACE, "WCFService");
                        if(s!=null)
                            Log.d("MainActivity", getList);
                    }
                }).start();
            }
        });

    }

    private void test() {

        helper = PermissionManager.with(MainActivity.this)
                //添加权限请求码
                .addRequestCode(MainActivity.REQUEST_CODE_CAMERA)
                //设置权限，可以添加多个权限
                .permissions(Manifest.permission.CALL_PHONE)
                //设置权限监听器
                .setPermissionsListener(new PermissionListener() {

                    @Override
                    public void onGranted() {
                        //当权限被授予时调用
                        Toast.makeText(MainActivity.this, "Camera Permission granted",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + "15202842963");
                        intent.setData(data);
                    }


                    @Override
                    public void onShowRationale(String[] permissions) {
                        //当用户拒绝某权限时并点击`不再提醒`的按钮时，下次应用再请求该权限时，需要给出合适的响应（比如,给个展示对话框来解释应用为什么需要该权限）
                        Snackbar.make(button, "需要相机权限去拍照", Snackbar.LENGTH_INDEFINITE)
                                .setAction("ok", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //必须调用该`setIsPositive(true)`方法
                                        helper.setIsPositive(true);
                                        helper.request();
                                    }
                                }).show();
                    }
                })
                //请求权限
                .request();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "sdf", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public void find(View view) {
    }
}
