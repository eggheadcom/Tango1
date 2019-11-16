package jp.kenschool.tango1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import static jp.kenschool.tango1.MyOpenHelper.*;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity implements MyConst {

    // フィールド――――――――――――――――
    TextView tvError = null;
    TextView tvDebug = null;
    EditText edCnt = null;
    EditText edRate = null;
    EditText edDays = null;
    CheckBox chPrevious = null;
    BootstrapButton btnStartQuestion = null;
    BootstrapButton btnBack = null;
    BootstrapButton btnReset = null;
    BootstrapButton btnStartQuiz = null;
    RadioButton rbJpn = null;
    RadioButton rbAllCnt = null;
    RadioButton rbCnt = null;
    RadioGroup rgpCate = null;
    RadioGroup rgpLang = null;
    RadioGroup rgpCtn = null;

    ManageDB mdb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tvError = findViewById(R.id.tv_err_test);
        tvDebug = findViewById(R.id.tv_debug_test);
        edCnt = findViewById(R.id.edit_cnt_test);
        edRate = findViewById(R.id.edit_rate_test);
        edDays = findViewById(R.id.edit_days_test);
        chPrevious = findViewById(R.id.check_previous_test);
        rbJpn = findViewById(R.id.rg_jpn_test);
        rbAllCnt = findViewById(R.id.rg_cnt_all_test);
        rbCnt = findViewById(R.id.rg_cnt_order_test);
        rgpCate = findViewById(R.id.rbgroup_search);
        rgpLang = findViewById(R.id.rbgroup_lang_test);
        rgpCtn = findViewById(R.id.rbgroup_cnt_test);
        btnStartQuestion = findViewById(R.id.btn_start_test);
        btnStartQuiz = findViewById(R.id.btn_start_quiz);
        btnReset = findViewById(R.id.btn_reset_test);
        btnBack = findViewById(R.id.btn_back_test);

        mdb = new ManageDB(this);

        //カテゴリに合わせてラジオボタン生成
        ArrayList<String> categories = mdb.getCateList();
        for(int i = 0; i < categories.size(); i++){
            RadioButton rb = new RadioButton(this);
            rb.setText(categories.get(i));
            rb.setTag(i+1);
            rgpCate.addView(rb);
        }

        //問題数EditViewにフォーカスされたらラジオボタンを変更
        edCnt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) rbCnt.setChecked(true);
            }
        });

       /*―――――――――――――――――――――――――――――――――――――――――――――
           記述ボタン
        ―――――――――――――――――――――――――――――――――――――――――――――――*/
       btnStartQuestion.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               start(1);
           }
       });

       /*―――――――――――――――――――――――――――――――――――――――――――――
           四択ボタン
        ―――――――――――――――――――――――――――――――――――――――――――――――*/
        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(2);
            }
        });

        /*―――――――――――――――――――――――――――――――――――――――――――――
           リセットボタン
        ―――――――――――――――――――――――――――――――――――――――――――――――*/
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbJpn.setChecked(true);
                rbAllCnt.setChecked(true);
                rgpCate.clearCheck();
                edCnt.setText("");
                edRate.setText("");
                edDays.setText("");
                chPrevious.setChecked(false);
            }
        });

        /*―――――――――――――――――――――――――――――――――――――――――――――
           戻るボタン
        ―――――――――――――――――――――――――――――――――――――――――――――――*/
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    /*―――――――――――――――――――――――――――――――――――――――――――――
       メイン処理（条件に合わせて単語セットを作り画面遷移）
    ―――――――――――――――――――――――――――――――――――――――――――――――*/
    private void start(int mode){

            boolean inputCheck = true;   //入力チェックboole
            StringBuilder errorMsg = new StringBuilder(); //エラー用msg
            tvError.setText("");

            //言語タイプ設定
            int lang = MyConst.JPN;
            int checkedLangId = rgpLang.getCheckedRadioButtonId(); //チェックされたID取得
            if(checkedLangId == R.id.rg_jpn_test){
                lang = MyConst.JPN;
            } else if(checkedLangId == R.id.rg_eng_test){
                lang = MyConst.ENG;
            }

            //SQL文生成のための各条件用のString―――――――――――――――
            String sql = "";
            String limit = "";
            String cate = "";
            String rate = "";
            String prev = "";
            String day = "";

            //問題数の設定
            int checkedCntId = rgpCtn.getCheckedRadioButtonId();
            if(checkedCntId == R.id.rg_cnt_all_test){             //全問の場合
                limit = "";

            } else if(checkedCntId == R.id.rg_cnt_order_test){    //問題数指定の場合
                String order = edCnt.getText().toString();
                if(order.equals("")){                             //入力がない場合
                    inputCheck = false;
                    errorMsg.append("問題数を入力してください\n");
                }else{                                            //SLQ作成
                    if(Check.isNumber(order, 1, 100)){
                        limit = " LIMIT " + order;                //行数指定sql
                    }else{
                        inputCheck = false;
                        errorMsg.append("問題数は1-100の範囲で指定してください\n");
                    }
                }
            }

            //カテゴリの設定
            int checkedCateId = rgpCate.getCheckedRadioButtonId();   //チェックされたID取得
            if(checkedCateId == -1){             //CHECKされたラジオボタンが無ければ
                cate = "cate_id > 0";           //WHERE句かAND句か処理面倒なので全部の場合も書いておく
            } else{
                int cateID = (int) findViewById(checkedCateId).getTag();
                cate = "cate_id = " + cateID;
            }

            //正答率の設定
            String order = edRate.getText().toString();
            if(!(order.equals(""))){                          //空じゃなければ
                if(Check.isNumber(order, 1, 100)){
                    rate = " AND rate <= " + order;    //order以下で指定するsql
                }else{
                    inputCheck = false;
                    errorMsg.append("正答率は1-100の範囲で指定してください\n");
                }
            }


            //前回間違い
            if(chPrevious.isChecked()){
                prev = " AND (previous = 0 AND cnt > 0)"; //1回以上挑戦したもので
            }

            //登録日
            order = edDays.getText().toString();
            if(!(order.equals(""))){                          //入力がない場合
                if(Check.isNumber(order, 1, 365)){
                    day = " AND created_by > date('now', '-"+order+" days')"; //order日前より最近
                }else{
                    inputCheck = false;
                    errorMsg.append("日数は1-365の範囲で指定してください");
                }
            }

            if(inputCheck){         //入力チェックでfalseになっていなければ実行

                //SQL文の組み立て
                sql = "SELECT * FROM " + TABLE_WORDS
                        +  " WHERE "
                        + cate
                        + rate
                        + prev
                        + day
                        + " ORDER BY RANDOM()" + limit;

                //DBアクセスしてデータセット取得
                ArrayList<Word> data = mdb.read(sql);
                if(data.size() != 0) {

                    //debug表示
                    String res = "結果:\n";
                    res += data.get(0).toString();
                    res += "\n";
                    res += "件数:" + data.size() + "\n";
                    res += sql;
                    tvDebug.setText(res);


                    // 画面遷移
                    Intent intent = null;
                    if(mode == 1){      //記述モード
                        intent = new Intent(TestActivity.this, QuestionActivity.class);
                    }else if(mode ==2){ //四択モード
                        intent = new Intent(TestActivity.this, QuizActivity.class);
                    }
                    intent.putExtra("data", data);
                    intent.putExtra("lang", lang);
                    startActivity(intent);
                    finish();

                }else{
                    errorMsg.append("該当する単語がありません");
                }
            }
            tvError.setText(errorMsg);  //エラーメッセージの表示
    }

}
