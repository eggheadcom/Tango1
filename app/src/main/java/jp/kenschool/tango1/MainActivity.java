package jp.kenschool.tango1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // フィールド――――――――――――――――
    public static final double VERSION = 2.8;
    private TextView tvLogin = null;
    private ManageDB mdb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ツールバー
        ViewGroup root = (ViewGroup) findViewById(R.id.lineralayout_root).getParent();
        // false=rootをmy_toolbarのルートにしない.
        android.support.v7.widget.Toolbar toolbar
                = (android.support.v7.widget.Toolbar)LayoutInflater.from(this).inflate(R.layout.my_toolbar, root, false);
        root.addView(toolbar, 0);
        setSupportActionBar(toolbar);


        // 起動後の初回はログイン画面に飛ぶ――――――――――――――――――――――――
        if(!UserData.init){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        // ******* FindView 群 ********
        tvLogin = findViewById(R.id.tv_login_main);
        findViewById(R.id.btn1_main).setOnClickListener(this);
        findViewById(R.id.btn2_main).setOnClickListener(this);
        findViewById(R.id.btn3_main).setOnClickListener(this);
        findViewById(R.id.btn4_main).setOnClickListener(this);
        findViewById(R.id.btn5_main).setOnClickListener(this);
        findViewById(R.id.btn6_main).setOnClickListener(this);
        findViewById(R.id.btn7_main).setOnClickListener(this);

        // データベースマネージャーオブジェクトを生成
        mdb = new ManageDB(this);
    }

    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
        ログイン画面から戻った際の処理
    ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    @Override
    protected void onResume() {
        super.onResume();
        String msg = "Wellcome " + UserData.name + "!!";
        tvLogin.setText(msg);
    }

    /*――――――――――――――――――――――――――――――――――――――――――――――――――――――――
        各アクティビティへ遷移
    ――――――――――――――――――――――――――――――――――――――――――――――――――――――――*/
    @Override
    public void onClick(View v){

        Intent intent = null;

        switch (v.getId()){

            case R.id.btn1_main:
                intent = new Intent(MainActivity.this, TestActivity.class);
                break;

            case R.id.btn2_main:
                intent = new Intent(MainActivity.this, RegisterActivity.class);
                break;

            case R.id.btn3_main:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                break;

            case R.id.btn4_main:
                intent = new Intent(MainActivity.this, ShowActivity.class);
                break;

            case R.id.btn5_main:
                intent = new Intent(MainActivity.this, InfoActivity.class);
                break;

            case R.id.btn6_main:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                break;

            case R.id.btn7_main:
                intent = new Intent(MainActivity.this, CardsSettingActivity.class);
                break;

            default:
                break;

        }
        startActivity(intent); //上で設定したActivityへ遷移
    }

    //ツールバーメニュー
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //ツールバーセレクト
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}