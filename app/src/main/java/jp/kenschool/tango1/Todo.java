package jp.kenschool.tango1;

public class Todo {

/*

 【追加機能】
    *通知機能
      必要知識：BroadCastRecever, Notification, TimePicker

    *バーやメニュー、歯車
      必要知識：ActionBar, ToolBar, NavigationVar, OptionMenu,

    *共有機能
      必要知識：ShareProvider, SNS連携のSPI?

    *カテゴリー登録機能　（最大値を10とかにする）
    *パスワードの隠すチェックボックス
    *ユーザーが選べるカラータイプ
    *評価して下さい★メニューの中に

 【修正・改善】
      ArrayList型にしているやつを可能なら、左辺だけListに変更
      カードスワイプで消したときのずれ
      メインActivityのボタンナンバリングを整える
      INVISIBLE系のボタンを対処
      クラス名のリファクタリング　（特にテストとクエスチョン、カード３種）
      ユーザー名とパスに使用可能な文字列が現在英字のみなので、あとで修正

 【検討】
     削除メニューをどうするか
     リサイクラービューを利用した単語編集画面（長押しで並べ替え、スワイプで削除）
     検索窓を虫眼鏡アイコン（他もアイコン利用してすっきりと）

 【確認】
      各入力チェックにおける空白の扱い

 【余裕あればやる】
      バインドに変更（初期レコード挿入、削除レコードfor文）
      拡張性などを考えたときに、何か所も書き換えなければいけない状態からもっと良いものも考える
      トランザクションについて検討
      入力チェックをもう少し共通化してまとめられるならやる


  【補足】
      try catch は検査例外ではないから付けなくてもOK　
      登録の外部キーの有効化　INSERT前に入れるとかいうやつ　PRAGMA foreign_keys=true;




    /*
       デバッグ用として利用  日時指定データ挿入
          String insert =
                  "INSERT INTO " + TABLE_WORDS +
                          " (jpn, eng, created_by ,cate_id, user_id)" + " VALUES" +
                          " ('z', 'm', '2019-10-15', 1, 1)";
          boolean b = mdb.write(insert);
          if(b) tvError.setText("Yes");
          else tvError.setText("Noooo");
     */


}
