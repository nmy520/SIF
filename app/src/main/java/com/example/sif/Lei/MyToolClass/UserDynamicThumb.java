package com.example.sif.Lei.MyToolClass;

import android.os.Handler;
import android.os.Message;

import com.example.sif.Lei.LianJie.HttpUtil;
import com.example.sif.NeiBuLei.MyThumb;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import okhttp3.Call;
import okhttp3.Response;

public class UserDynamicThumb {

    private Collection<String> sum;
    public void rightThumb() {
        sum = new ArrayList<String>();
        Collection<MyThumb> M = LitePal.findAll(MyThumb.class);
        for (MyThumb m:M){
            sum.add(m.getDynamic_id());
        }
    }

    public UserDynamicThumb(){
        rightThumb();
    }

    public boolean noYesThumb(String thumbNumber){
        if (sum.contains(thumbNumber)){
            return true;
        }else {
            return false;
        }
    }

    final String path = "http://nmy1206.natapp1.cc/gcThumb.php";
    final String path1 = "http://nmy1206.natapp1.cc/myThumb.php";
    public void userThumb(String dynamicid,String myxuehao,String xuehao, Handler thumbHandler){
        if (sum.contains(dynamicid)){
            sum.remove(dynamicid);
            startThumb(path,1,dynamicid,xuehao,thumbHandler);
            depositThumb(path1,1,dynamicid,myxuehao,thumbHandler);
        }else {
            sum.add(dynamicid);
            startThumb(path,0,dynamicid,xuehao,thumbHandler);
            depositThumb(path1,0,dynamicid,myxuehao,thumbHandler);
        }
    }

    public void startThumb(String path,int id,String udynamicid, String xuehao, final Handler handler){
        HttpUtil.dianThumb(path,id,udynamicid,xuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
                Message message = new Message();
                message.what = 1;
                message.obj = a;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    public void depositThumb(String path, int i, String udynamicid, String xuehao,Handler handler) {
        if (i == 0){
            MyThumb myThumb = new MyThumb();
            myThumb.setDynamic_id(udynamicid);
            myThumb.setThumb(1);
            myThumb.save();
        }
        if (i == 1){
            LitePal.deleteAll(MyThumb.class,"dynamic_id = ?",udynamicid);
        }
        HttpUtil.cunThumb(path,i,udynamicid,xuehao, new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String a = response.body().string();
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }


}
