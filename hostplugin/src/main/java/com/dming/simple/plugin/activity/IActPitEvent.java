package com.dming.simple.plugin.activity;

import android.content.Intent;
import android.os.Bundle;

public interface IActPitEvent {

    boolean startActivityForResult(Intent intent, int requestCode, Bundle options);

}
