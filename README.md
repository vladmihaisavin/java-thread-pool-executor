## A custom ThreadPoolExecutor implemented in Java ##
### Source contents ###
- MyThreadPoolExecutor

This is the class responsible for handling the __tasks execution__ and __threads management__.

- MyThread

A class that extends the Java Thread class. These are the threads that __are pooled and execute the tasks__.

- MyTask

A class that implements the Java Runnable interface. These are the tasks that are executed by the threads.
An override for the `run` method is present, as well as a `success` handler.

- MyTaskQueue

Basically this is an ArrayList of the tasks that do not have any available threads.
Common actions such as `get`, `put`, `isEmpty` and `isFull` are implemented.

### Walkthrough ###

A MyThreadPoolExecutor instance is created, with given CORE_POOL_SIZE,
MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME and QUEUE_SIZE. Then, a NUMBER_OF_TASKS are passed to
the executor, one by one. If the executor refuses task, then the task is passed to the executor,
until the executor accepts it.

Given the time when tasks are passed to the executor, the threadPool gains in size. When it reaches
the CORE_POOL_SIZE number of threads, the new tasks are put in the task queue. If the task queue is full,
any new task forces a creation of a new thread, until the MAXIMUM_POOL_SIZE number of threads is reached.
At that point, MyThreadPoolExecutor will reject the task.

The internals of MyThread are pretty self explanatory. While the thread pool executor is active,
the thread will keep on getting new tasks. The thread starts with the task given to it at the initialization.
After it finished that task, it tries to get and __run__ tasks from the task queue. If the thread does not have any 
tasks to __run__, it will wait for KEEP_ALIVE_TIME milliseconds, until the thread pool executor notifies it 
that a new task is in the queue. If the waiting time expires, the thread checks again if there are any tasks left in the task queue.
If the task queue is empty, the thread is removed from the thread pool.

When there are no more threads in the thread pool, a `destroy` signal is sent to the thread pool executor and 
the application finishes.
