package jp.ac.doshisha.mikilab.huecontroller;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import okhttp3.*;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    int room_index = 0;
    int color_index = 0;
    int startflag = 0;
    String url = "http://172.20.11.99/api/fKod5kAVYn2n0kXjKZaQ-XiP-CD5RvJQlsPdHD9a/groups/1/action";
    String json;
    String hue = "15324";
    String sat = "121";
    private String res = "";
    Button sb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //部屋指定
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setSelection(room_index);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                room_index = position;
                //KC104
                if(room_index == 0) url = "http://172.20.11.99/api/fKod5kAVYn2n0kXjKZaQ-XiP-CD5RvJQlsPdHD9a/groups/1/action";
                //KC111
                else if(room_index == 1) url = "http://172.20.11.100/api/z2YrJsBIMyPZlHWprsFmIjlfI2WaR9kxTHA6XVaI/groups/1/action";
                //KC111 ぼんぼり
                else url = "http://172.20.11.115/api/iYej4EW4N87WlWu-lhOixd4Yls6i7NjA6X4kcDvw/groups/1/action";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //色指定
        RadioGroup group1 = (RadioGroup)findViewById(R.id.radioGroup);
        group1.check(R.id.radioButton);
        group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                color_index = checkedId - R.id.radioGroup;
                RadioButton radio = (RadioButton)findViewById(checkedId);
                if(startflag == 1) {
                    sat = "121";
                    if(color_index == 2)    hue = "15324";  //　標準
                    else if(color_index == -2131492982)   hue = "0";  //　赤
                    else if(color_index == 3)   hue = "60000";   //　ピンク
                    else if(color_index == 4)   hue = "7774";   //　オレンジ
                    else if(color_index == 5)   hue = "24000";   //　黄緑
                    else if(color_index == 6)   hue = "46014";   //　青
                    else if(color_index == 7)   hue = "50000";   //　紫
                    else if(color_index == 8)   sat = "0";   //　白
                    json = "{\"on\":true,\"hue\":" + hue + ",\"bri\":254,\"sat\":" + sat + "}";
                    postTest();
                }
            }
        });




    }

    //ボタン押した時
    public void onButtonClick(View v){

        switch (v.getId()){
            case R.id.button_on:
                startflag = 1;
                json = "{\"on\":true,\"hue\":15324,\"bri\":254,\"sat\":121}";
                postTest();
                break;

            case R.id.button_off:
                startflag = 0;
                json = "{\"on\":false,\"hue\":" + hue + ",\"bri\":254,\"sat\":" + sat + "}";
                postTest();
                break;

        }
    }

    public void postTest() {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);

        Log.w("aaaaaaaaaaaaaaaaa", String.valueOf(color_index));

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failMessage();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                res = response.body().string();
                runOnUiThread(new Runnable() {
                    public void run() {
                        Log.w("onResponse", res);
                    }
                });
            }
        });
    }
    private void failMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                Log.w("failMessage", "fail");
            }
        });
    }
}
