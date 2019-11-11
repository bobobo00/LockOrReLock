package others;
/**
 * ���������������������ظ�����
 * @author dell
 *
 */
public class LockTest {
	Lock lock=new Lock();
	ReLock relock=new ReLock();
	public void a() throws InterruptedException {
		relock.lock();
		System.out.println(relock.getThreadCount());
		b();
		System.out.println(relock.getThreadCount());
		relock.unLock();
	}
	public void b() throws InterruptedException {
		relock.lock();
		System.out.println(relock.getThreadCount());
		relock.unLock();
	}
	public static void main(String[] args) throws InterruptedException {
		LockTest test=new LockTest();
		test.a();
		Thread.sleep(1000);
		System.out.println(test.relock.getThreadCount());
	}

}

class Lock{
	private boolean isLocked=false;
	//ʹ����
	public synchronized void lock() throws InterruptedException {
		while(isLocked) {
			wait();
		}
		isLocked=true;
	}
	//�ͷ���
	public synchronized void unLock() {
		isLocked=false;
		notify();
	}
}
class ReLock{
	private boolean isLocked=false;
	private Thread threadBy=null;
	private int threadCount=0;
	//ʹ����
	public synchronized void lock() throws InterruptedException {
		Thread t=Thread.currentThread();
		while(isLocked&&t!=threadBy) {
			wait();
		}
		isLocked=true;
		threadBy=t;
		threadCount++;
	}
	public int getThreadCount() {
		return threadCount;
	}
	//�ͷ���
	public synchronized void unLock() {
		Thread t=Thread.currentThread();
		if(t==threadBy) {
			threadCount--;
			if(threadCount==0) {
				isLocked=false;
				notify();
			}
		}
		isLocked=false;
		notify();
	}
}
