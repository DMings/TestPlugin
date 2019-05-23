package com.dming.simple.plugin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public interface IPitActEvent {

    boolean startActivityForResult(Context context, Intent intent, int requestCode, Bundle options);

}
