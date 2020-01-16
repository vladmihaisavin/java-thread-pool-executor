package vladmihaisavin;

public class MyThread extends Thread {
    private MyTask _task;
    private boolean _isNormalThread;
    private MyThreadPoolExecutor _threadPoolExecutor;

    public MyThread(MyTask task, MyThreadPoolExecutor threadPoolExecutor, boolean isNormalThread) {
        this._task = task;
        this._isNormalThread = isNormalThread;
        this._threadPoolExecutor = threadPoolExecutor;
    }

    private MyTask getNewTask() {
        return (_task != null) ? _task : _threadPoolExecutor.getTaskQueue().get();
    }

    private boolean isValid(MyTask task) {
        return task != null;
    }

    private void runTask(MyTask task) {
        task.run();
        this._task = null;
    }

    private boolean isEmptyTaskQueue() {
        return _threadPoolExecutor.getTaskQueue().isEmpty();
    }

    public void run() {
        while (_threadPoolExecutor.isActive()) {
            MyTask task = getNewTask();
            if (isValid(task)) {
                runTask(task);
            } else {
                try {
                    synchronized (_threadPoolExecutor) {
                        _threadPoolExecutor.wait(_threadPoolExecutor.getKeepAliveTime());
                        if (isEmptyTaskQueue()) {
                            _threadPoolExecutor.removeFromThreadPool(this);
                            return;
                        } else {
                            _threadPoolExecutor.getTaskQueue().get().run();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }
}
