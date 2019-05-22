package com.dming.testplugin;

import android.content.Intent;
import android.os.Bundle;

public interface IDealDexActivity {

    boolean startActivityForResult(Intent intent, int requestCode, Bundle options);

}
