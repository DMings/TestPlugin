package com.dming.testndk;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.dming.simple.plugin.activity.BasePitActivity;
import com.dming.testndk.one.OneActivity;

import java.io.*;

public class NDKActivity extends BasePitActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        Button tv = (Button) findViewById(R.id.sample_text);
//        tv.setText(test2() +"---"+ test3());
//        tv.setText(stringFromJNI());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},666);
        }

        findViewById(R.id.testBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NDKActivity.this, OneActivity.class));
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCpp();
//                File srcFile = new File(Environment.getExternalStorageDirectory(),"1"+File.separator + "首页_slices (1).zip");
//                File outDir = new File(Environment.getExternalStorageDirectory(),"1"+File.separator + "outC");
//                outDir.mkdirs();
//                if(srcFile.exists()){
//                    Log.i("DMUI","文件存在");
//                    try {
//                        ZipUtils.unzipFile(srcFile,outDir);
//                    } catch (IOException e) {
////                        e.printStackTrace();
//                        Log.e("DMUI","IOException>"+e.getMessage());
//                    }
//                }else {
//                    Log.e("DMUI","文件不存在");
//                }

//                long time = System.currentTimeMillis();
//                String str = Encoding.getEncoding("我诶见弗兰克斯几多福利快结束了可敬的发生考虑到发送卡两地分居"+0);
//                Log.e("DMUI","str>>>"+str);
//                String str1 = Encoding.getEncoding("sdfdf21313f65d5ddddf.';["+0);
//                Log.e("DMUI","str1>>>"+str1);
//                for(int i = 0;i < 1000;i++){
//                    Encoding.getEncoding("我诶见弗兰克斯几多福利快结束了可敬的发生考虑到发送卡两地分居"+i);
//                }
//                Log.e("DMUI","time>>>"+(System.currentTimeMillis() - time));

//                File srcFile = new File(Environment.getExternalStorageDirectory(),"1/ccc.txt");
//                if(srcFile.exists()){
//                    Log.i("DMUI","文件存在c");
//                    String str = readToString(srcFile.toString());
//                    Log.i("DMUI","str->"+str);
//                }else {
//                    Log.e("DMUI","文件不存在");
//                }
//                File srcFile = new File(Environment.getExternalStorageDirectory(),"360"+File.separator + "首页_slices (1)");
//                File outFile = new File(Environment.getExternalStorageDirectory(),"360"+File.separator + "out"+File.separator+"首页_slices (1).zip");
//                outFile.getParentFile().mkdirs();
//                if(srcFile.exists()){
//                    Log.i("DMUI","文件存在");
//                    try {
//                        ZipUtils.zipFile(srcFile,outFile);
//                    } catch (IOException e) {
////                        e.printStackTrace();
//                        Log.e("DMUI","IOException>"+e.getMessage());
//                    }
//                }else {
//                    Log.e("DMUI","文件不存在");
//                }

//                BitmapFactory.Options options = new BitmapFactory.Options();
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round,options);
//                int res = native_blur(bitmap);
//                if(res == 0){
//                    Log.i("DMUI","sssss");
//                }else {
//                    Log.i("DMUI","eeeee");
//                }
            }
        });
    }

    public String readToString(String fileName) {
        String encoding = "GBK";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

//    public native String stringFromJNI();

//    public native int native_blur(Bitmap bitmap,int i);
    public native void testCpp();
}
