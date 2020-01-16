package vladmihaisavin;

import java.util.ArrayList;

public class MyThreadPoolExecutor {
    private int _corePoolSize;
    private int _maximumPoolSize;
    private int _keepAliveTime;
    private MyTaskQueue _taskQueue;
    private ArrayList<MyThread> _threadPool;
    private boolean _isActive = true;

    public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, int keepAliveTime, int queueSize) {
        this._corePoolSize = corePoolSize;
        this._maximumPoolSize = maximumPoolSize;
        this._keepAliveTime = keepAliveTime;
        this._taskQueue = new MyTaskQueue(queueSize);
        this._threadPool = new ArrayList<>();
    }

    synchronized boolean execute(MyTask task) {
        if (canCreateThreadInCorePool()) {
            return createThreadInCorePool(task);
        } else {
            if (_taskQueue.isFull()) {
                if (canCreateThreadInExtendedPool()){
                    return createThreadInExtendedPool(task);
                }
            } else {
                return putTaskInQueue(task);
            }
        }
        return false;
    }

    private boolean canCreateThreadInCorePool() {
        return _threadPool.size() < _corePoolSize;
    }

    private boolean createThreadInCorePool(MyTask task) {
        _threadPool.add(new MyThread(task, this, true));
        _threadPool.get(_threadPool.size() - 1).start();
        return true;
    }

    private boolean canCreateThreadInExtendedPool() {
        return _threadPool.size() < _maximumPoolSize;
    }

    private boolean createThreadInExtendedPool(MyTask task) {
        MyTask taskFromQueue = _taskQueue.get();
        if (taskFromQueue != null) {
            MyThread thread = new MyThread(taskFromQueue, this, false);
            _threadPool.add(thread);
            thread.start();
            return putTaskInQueue(task);
        }
        return true;
    }

    private boolean putTaskInQueue(MyTask task) {
        boolean status = _taskQueue.put(task);
        if (status) {
            notify();
        }
        return status;
    }

    public void destroy() {
        _isActive = false;
        synchronized (this) {
            notifyAll();
        }
    }

    public boolean isActive() {
        return _isActive;
    }

    public MyTaskQueue getTaskQueue() {
        return _taskQueue;
    }

    public int getKeepAliveTime() {
        return _keepAliveTime;
    }

    synchronized void removeFromThreadPool(MyThread thread) {
        _threadPool.remove(thread);
        System.out.println("REMAINING THREADS: " + _threadPool);
    }

    public int getThreadPoolSize() {
        return _threadPool.size();
    }
}
