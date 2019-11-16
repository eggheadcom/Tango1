package jp.kenschool.tango1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapProgressBar;
import com.beardedhen.androidbootstrap.BootstrapProgressBarGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static jp.kenschool.tango1.MyOpenHelper.TABLE_WORDS;

public class QuizActivity extends AppCompatActivity {

    ArrayList<Word> data = null;
    int type = 0;       //type(1=JPN,2=ENG)
    ManageDB mdb = null;

    RadioButton[] rbList = null;
    RadioGroup rGroup = null;
    TextView tvQuestion = null;
    TextView tvCount = null;
    TextView tvDebug = null;
    BootstrapButton btnCheck = null;
    BootstrapButton btnQuit = null;
    BootstrapProgressBar bar = null;
    BootstrapProgressBarGroup barGroup = null;

    Word word = null;           //１単語

    int allCount = 0;   //総問題数（変化しない）
    int now = 1;        //何問目か
    int good = 0;       //正解数（このテストにおいての）
    int cnt = 0;        //DBのcnt （過去のこの問題を挑戦した回数)
    int correct = 0;    //DBのcorrect (過去にこの問題を正解した回数)
    int prev = 0;       //DBのprevious（前回正解した 0:不正解 1:正解）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //**findView**
        rbList = new RadioButton[]{
              findViewById(R.id.rb_select1_quiz),
              findViewById(R.id.rb_select2_quiz),
              findViewById(R.id.rb_select3_quiz),
              findViewById(R.id.rb_select4_quiz)
        };
        rGroup = findViewById(R.id.rbgroup_quiz);
        bar = findViewById(R.id.prgress_quiz);
        barGroup = findViewById(R.id.prgress_grp_quiz);
        tvQuestion = findViewById(R.id.tv_question_quiz);
        tvCount = findViewById(R.id.tv_ques_count_quiz);
        tvDebug = findViewById(R.id.tv_debug_quiz);
        btnCheck = findViewById(R.id.btn_check_next_quiz);
        btnQuit = findViewById(R.id.btn_quit_quiz);


        //全画面からデータセットと言語タイプを受け取る
        Intent intent = getIntent();
        data = (ArrayList<Word>) intent.getSerializableExtra("data");
        type = intent.getIntExtra("lang", 1);

        mdb = new ManageDB(this);


        //問題数を代入
        allCount = data.size();
        barGroup.setMaxProgress(allCount);

        //現在の問題のWordデータを取得
        word = data.get(now-1);

        //問題を表示
        tvQuestion.setText(word.getLang(type)); //type(1=JPN,2=ENG)の値を持ってくる

        //問題のカウントを表示
        String str = allCount + "問中 "+ now +"問目";
        tvCount.setText(str);

        //4択生成
        createAnserChoice();


        //解答ボタン――――――――――――――――
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ボタンを使用できなくする
                btnCheck.setEnabled(false);

                //入力された答え
                int checkedID = rGroup.getCheckedRadioButtonId(); //チェックされたID取得
                RadioButton checkedRb = findViewById(checkedID);
                String userAns = checkedRb.getText().toString();

                //実際の答え
                String ans = word.getLang(type +1);

                //正解判定と結果表示
                if(userAns.equals(ans)){
                    tvDebug.setText("正解!!");
                    //updateなどの処理
                    cnt = word.getCnt();
                    correct = word.getCorrect();
                    prev = word.getPrevious();
                    correct++;
                    good++;
                    prev = 1;
                } else {
                    tvDebug.setText("不正解...");
                    prev = 0;
                }
                cnt++;

                //UPDATE文の生成
                String sql = "UPDATE " + TABLE_WORDS + " SET "
                        + "cnt = " + cnt
                        + ", correct = " + correct
                        + ", previous = " + prev
                        + ", rate = " + (100 * correct / cnt)
                        + " WHERE word_id = " + word.getWordID();

                //tvDebug.setText(sql);
                if(mdb.write(sql)){
                    //tvDebug.setText("Yes!!");
                }else{
                    //tvDebug.setText("Noooo");
                }

                now++;

                //最後の問題かチェック
                if(now <= allCount){

                    //次の問題を取得
                    word = data.get(now-1);

                    //問題を表示
                    tvQuestion.setText(word.getLang(type));

                    //回答リストを表示
                    createAnserChoice();

                    //問題カウント表示
                    String str = allCount + "問中 "+ now +"問目";
                    bar.setProgress(now);
                    tvCount.setText(str);

                    rGroup.clearCheck();
                    //ボタンの状態変更
                    btnCheck.setEnabled(false);


                }else{
                    //終了処理
                    finishTest();
                }
            }
        });

        //終了ボタン――――――――――――――――
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishTest();
            }
        });

        //ラジオボタンに変化があった時に
        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                btnCheck.setEnabled(true);
            }
        });
    }


    //終了メソッド（正解率をダイアログで表示。OKでアクティビティ終了）
    private void finishTest(){

        int rate = 0;
        if(now > 1){            //1問も解いてないときは何もしない
            rate = 100 * good / (now-1);
        }
        String msg = (now-1) + "問中" + good + "問正解しました！\n"
                + "正解率は" + rate + "%です。";

        AlertDialog.Builder alBuilder = new AlertDialog.Builder(QuizActivity.this);
        alBuilder.setTitle("結果");
        alBuilder.setMessage(msg);
        alBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){

                Intent intent = new Intent(QuizActivity.this, TestActivity.class);
                startActivity(intent);
                finish();

            }
        });
        AlertDialog al = alBuilder.create();
        al.show();


    }

    //4択生成メソッド
    private void createAnserChoice(){

        //正解文字列と単語リストからランダムに取り出した３つの文字列をListに入れる
        List<String> anserList = new ArrayList<String>();
        anserList.add(word.getLang(type +1));        //実際の答え
        for(int i = 1; i< 4; i++){                   //ランダム３つ
            int num = (int) (Math.random() * data.size());
            String s = data.get(num).getLang(type+1);
            if(s != anserList.get(0)){
                anserList.add(s);
            }else{
                i--;
            }
        }

        //シャッフル
        Collections.shuffle(anserList);

        //ラジオボタンのテキストにセット
        for(int i = 0; i< rbList.length; i++){
            rbList[i].setText(anserList.get(i));
        }

    }


}
