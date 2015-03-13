package zidane.multiThread;
/*
* Use condition and lock to implement that thread accesses target object exclusively
* There are two thread that one is waxing on the car, and the other one is buffing off the wax from the car.
* But buffing out must be started after waxed, and waxing is after buffed out. 
* As a result, the thread waxing is waiting for notice when buffing out thread is working.
*/


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaxAndBuff {
	public static void main(String[] args) throws Exception{
		Car car= new Car();
		ExecutorService exce = Executors.newCachedThreadPool();
		exce.execute(new WaxOn(car));
		exce.execute(new Buff(car));
		TimeUnit.SECONDS.sleep(5);
		exce.shutdownNow();
	}
}
class Car{
	private Lock lock= new ReentrantLock();
	private Condition condition=lock.newCondition();
	private  boolean isWaxed = false;
	public Car() {
		// TODO 自動生成されたコンストラクター・スタブ
	}
	  void waitForWax() throws InterruptedException{
		  lock.lock();
		  try {
			  while(isWaxed == true){
				  condition.await();
			  }
		} catch (InterruptedException e) {
			// TODO: handle exception
		}finally{
			lock.unlock();
		}
	}
	  void waitForBuff() throws InterruptedException{
			lock.lock();
		  while(isWaxed == false){
				condition.await();
			}
		}
	  void waxOn(){
		 lock.lock();
		 try{
		  System.out.println("wax On");
		 isWaxed=true;
		 //job done, notice all the other threads 
		 condition.signalAll();
		 }catch (Exception e) {
			// TODO: handle exception
		}finally{
			lock.unlock();
		}
	 }
	  void Buff(){
	   // try to lock the object conditon 
		  lock.lock();
		 System.out.println("buff On");
		 isWaxed=false;
		 condition.signalAll();
	 }
}

class WaxOn implements Runnable {
	Car c;
	public WaxOn(Car car) {
		// TODO 自動生成されたコンストラクター・スタブ
		c = car;
	}
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		try {
		// end condition should be implemented by while() and inner of try block
		// so as to break when interrupted
			while (!Thread.interrupted()) {
				c.waitForWax();
				c.waxOn();
				TimeUnit.SECONDS.sleep(2);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
class Buff implements Runnable {
	Car c;
	public Buff(Car car) {
		// TODO 自動生成されたコンストラクター・スタブ
		c = car;
	}
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		try {
			while (!Thread.interrupted()) {
				c.waitForBuff();
				c.Buff();
				TimeUnit.SECONDS.sleep(2);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}

