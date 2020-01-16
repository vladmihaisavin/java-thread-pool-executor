package vladmihaisavin;

public class MyTask implements Runnable {
    private int _id;

    public MyTask(int id) {
        this._id = id;
    }

    @Override
    public void run() {
        double start = System.currentTimeMillis() - Main.startTime;
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double end = System.currentTimeMillis() - Main.startTime;
        success(_id, start, end);
    }

    public static void success(int id, double start, double end) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + "Task no. " + id + ": start = " + start + ", end = " + end);
    }

    public int getId() {
        return _id;
    }
}
