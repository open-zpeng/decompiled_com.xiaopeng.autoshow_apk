package com.xiaopeng.lib.bughunter.anr;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.text.TextUtils;
import com.xiaopeng.lib.bughunter.anr.Caton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class BlockHandler {
    private static final String MAIN_THREAD = "main";
    private static final String TAG = "BlockHandler";
    private static final String THREAD_TAG = "-----";
    private static ExecutorService mExecutorService = Executors.newFixedThreadPool(1);
    private Caton.Callback mCallback;
    private Collector mCollector;
    private Context mContext;
    private StringBuilder mStackBuilder = new StringBuilder(4096);
    private List<String> mStackInfoList = new ArrayList();

    public BlockHandler(Context context, Collector collector, Caton.Callback callback) {
        this.mContext = context;
        this.mCollector = collector;
        this.mCallback = callback;
    }

    public void notifyBlockOccurs(boolean z, long... jArr) {
        if (this.mCallback == null || Debug.isDebuggerConnected()) {
            return;
        }
        mExecutorService.execute(getRunnable(this.mCollector.getStackTraceInfo(), this.mCollector.getStackTraceRepeats(), z, jArr));
    }

    private Runnable getRunnable(final StackTraceElement[][] stackTraceElementArr, final int[] iArr, final boolean z, final long... jArr) {
        return new Runnable() { // from class: com.xiaopeng.lib.bughunter.anr.BlockHandler.1
            @Override // java.lang.Runnable
            public void run() {
                boolean z2 = !TextUtils.isEmpty(z ? BlockHandler.this.checkAnr() : "");
                BlockHandler.this.mStackInfoList.clear();
                StackTraceElement[][] stackTraceElementArr2 = stackTraceElementArr;
                if (stackTraceElementArr2 != null && stackTraceElementArr2.length > 0) {
                    int i = 0;
                    for (StackTraceElement[] stackTraceElementArr3 : stackTraceElementArr2) {
                        if (stackTraceElementArr3 != null && stackTraceElementArr3.length > 0) {
                            if (BlockHandler.this.mStackBuilder.length() > 0) {
                                BlockHandler.this.mStackBuilder.delete(0, BlockHandler.this.mStackBuilder.length());
                            }
                            StringBuilder sb = BlockHandler.this.mStackBuilder;
                            sb.append(BlockHandler.THREAD_TAG);
                            sb.append(BlockHandler.MAIN_THREAD);
                            sb.append(" repeat ");
                            sb.append(iArr[i]);
                            sb.append("\n");
                            for (StackTraceElement stackTraceElement : stackTraceElementArr3) {
                                StringBuilder sb2 = BlockHandler.this.mStackBuilder;
                                sb2.append("\tat ");
                                sb2.append(stackTraceElement.toString());
                                sb2.append("\n");
                            }
                        }
                        BlockHandler.this.mStackInfoList.add(BlockHandler.this.mStackBuilder.toString());
                        i++;
                    }
                }
                String[] strArr = (String[]) BlockHandler.this.mStackInfoList.toArray(new String[0]);
                if (strArr.length == 0) {
                    return;
                }
                BlockHandler.this.mCallback.onBlockOccurs(strArr, z2, jArr);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String checkAnr() {
        List<ActivityManager.ProcessErrorStateInfo> processesInErrorState = ((ActivityManager) this.mContext.getSystemService("activity")).getProcessesInErrorState();
        if (processesInErrorState != null) {
            for (ActivityManager.ProcessErrorStateInfo processErrorStateInfo : processesInErrorState) {
                if (processErrorStateInfo.condition == 2) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(processErrorStateInfo.processName);
                    sb.append("\n");
                    sb.append(processErrorStateInfo.shortMsg);
                    sb.append("\n");
                    sb.append(processErrorStateInfo.longMsg);
                    Config.log(TAG, sb.toString());
                    return sb.toString();
                }
            }
            return "";
        }
        return "";
    }

    public void startMonitor() {
        this.mCollector.start();
    }

    public void stopMonitor() {
        this.mCollector.stop();
    }
}
