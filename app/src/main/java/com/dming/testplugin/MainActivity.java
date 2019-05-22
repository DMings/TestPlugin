package com.dming.testplugin;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.dming.testplugin.utils.DLog;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.testBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            PMF.checkInit();
            Intent intent = new Intent();
            ComponentName cn = new ComponentName(getPackageName(), "com.dming.testndk.NDKActivity");
            intent.setComponent(cn);
            PMF.iDealDexActivity.startActivityForResult(intent,-1,null);
            DLog.i("pkgName>"+cn.getPackageName()+" >>> name-> "+cn.getClassName());
            startActivity(intent);

//            PMF.getPluginInfo()
            }
        });
    }


}

