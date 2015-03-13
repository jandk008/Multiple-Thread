package zidane.multiThread;
/*
* Thread A is waiting a notice, and print a word when noticed 
* Thread B send notice in a random time inerval
* Thread A is like a listener , B is a commander 
* If this one is combined with that message transmission one, we can build a simple chat application 
*/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceSample {
	public static void main(String[] args) {
		ExecutorService service = null;
		A a = new A();
		service = Executors.newCachedThreadPool();
		service.execute(a);
		service.execute(new B(a));
		// TimeUnit.SECONDS.sleep(5);
		// service.shutdownNow();
		/*
		 * service = Executors.newFixedThreadPool(5); // service =
		 * Executors.newCachedThreadPool(); ArrayList<MyRunnable> list = new
		 * ArrayList<MyRunnable>(); for (int n = 0; n < 15; n++) { list.add(new
		 * MyRunnable()); } for (MyRunnable runnable : list) {
		 * service.execute(runnable); }
		 */

		try {
			TimeUnit.SECONDS.sleep(8);
			service.shutdownNow();
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}

class A implements Runnable {
	public  void run() {
		// TODO 自動生成されたメソッド・スタブ
			display();
			System.out.println("A end");
	}

	synchronized void display() {
		try{
		while(!Thread.interrupted()){
		wait();
		System.out.println("notified");
		}
		}catch (InterruptedException e) {
			// TODO: handle exception
			System.out.println("display exception");
		}
	}

	synchronized void notifyForDisplay(){
				notify();
	}
}

class B implements Runnable {
	A a;
	B(A a) {
		this.a = a;
	}
	public  void run() {
		// TODO 自動生成されたメソッド・スタブ
		try{
				while (!Thread.interrupted()) {
				//
					synchronized(a){
					TimeUnit.SECONDS.sleep(2);
					a.notifyForDisplay();
					}
				}
			}catch (InterruptedException e) {
				// TODO: handle exception
				System.out.println("notify exception");
			}
			System.out.println("B end");
	}

}
