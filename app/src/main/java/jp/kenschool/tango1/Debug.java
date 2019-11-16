package jp.kenschool.tango1;

import android.app.Activity;
import android.view.Gravity;
import android.widget.Toast;


//デバッグ用　とりあえずここに

public class Debug {

    //トーストで表示　引数２にエラーメッセージ
    public static void toast(Activity activity, String msg){
        Toast toast = Toast.makeText(activity,msg,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM,0,20);
        toast.show();
    }

}
