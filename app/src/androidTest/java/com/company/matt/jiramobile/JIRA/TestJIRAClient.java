package com.company.matt.jiramobile.JIRA;

import android.test.AndroidTestCase;
import java.util.List;

public class TestJIRAClient extends AndroidTestCase {
    public static final String LOG_TAG = TestJIRAClient.class.getSimpleName();

    public void testFetchTasks() {
        List<Task> tasks = JIRAClient.FetchTasks();
        assertTrue("JIRAClient.FetchTasks() returned zero tasks", tasks.size() > 0);
    }
}