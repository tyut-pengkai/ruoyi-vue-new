package com.ruoyi.update.download;

import java.io.IOException;

public interface Callback {

    void onProgress(long progress);

    void onFinish();

    void onError(IOException ex);

    void onSpeed(double speed);
}
