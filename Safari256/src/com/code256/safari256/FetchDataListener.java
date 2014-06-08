package com.code256.safari256;

/**
 * DON'T REMOVE THIS
 * Created by CODE+256 on 2/10/14.
 * Mr.sentio henry
 * codeuganda@yahoo.com
 */
import java.util.List;

public interface FetchDataListener {
    public void onFetchComplete(List<Application> data);
    public void onFetchFailure(String msg);
}
