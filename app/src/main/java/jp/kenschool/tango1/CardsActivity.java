package jp.kenschool.tango1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class CardsActivity extends AppCompatActivity {

    //フィールド―――――――――――――――――――――――――――――
    RecyclerView recyclerView = null;
    RecyclerView.Adapter rAdapter;
    BootstrapButton btnBack = null;
    BootstrapButton btnReset = null;
    ManageDB mdb = null;
    List<Word> wordsList = null;  //単語リスト
    int langMode = 0;                  //言語タイプ

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        recyclerView = findViewById(R.id.my_recycler_view);
        btnBack = findViewById(R.id.btn_back_card);
        btnReset = findViewById(R.id.btn_reset_card);
        mdb = new ManageDB(this);

        //前画面から単語セット受け取り――――――――――――――――――――――――――
        Intent intent = getIntent();
        wordsList = (ArrayList<Word>) intent.getSerializableExtra("data");
        langMode = intent.getIntExtra("lang", 0);

        //リサイクラービュー開始メソッドの呼び出し
        startRecyclerView();

        //戻るボタン――――――――――――――――――――――――――
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardsActivity.this, CardsSettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //リセットボタン――――――――――――――――――――――――――
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecyclerView();
            }
        });

    }

    //リサイクラービューの生成メソッド――――――――――――――――――――――――――
    private void startRecyclerView(){
        recyclerView.setHasFixedSize(true);

        //レイアウト生成
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);
        //リサイクラービューにレイアウトをセット
        recyclerView.setLayoutManager(rLayoutManager);
        //アダプタにデータをセット
        rAdapter = new CardsAdapter(wordsList, langMode);
        //リサイクラービューにアダプタをセット
        recyclerView.setAdapter(rAdapter);

        //タッチヘルパーを生成
        ItemTouchHelper touchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT) {

                    //ムーブ（アイテムを上下に移動）
                    public boolean onMove(RecyclerView recyclerview,
                                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos =target.getAdapterPosition();
                        rAdapter.notifyItemMoved(fromPos,toPos);
                        return true;
                    }
                    //スワイプ（データリストから削除）
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction){
                        final int fromPos = viewHolder.getAdapterPosition();
                        wordsList.remove(fromPos);
                        rAdapter.notifyItemRemoved(fromPos);
                    }
                });
        //タッチヘルパーをセット
        touchHelper.attachToRecyclerView(recyclerView);
    }
}
