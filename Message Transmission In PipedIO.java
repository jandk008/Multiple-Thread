package zidane.multiThread;
/*
* This file implement transmission of data between two threads 
* One thread send message in an interval of random time, the other thread receive the message and print out 
* Pipe is used to implement the transmission
*/

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PipedIO {
	public static void  main(String[] args) throws InterruptedException{
		Sender sender = new Sender();
		Receiver receiver = new Receiver(sender.getPipedWriter());
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(sender);
		executorService.execute(receiver);
		TimeUnit.SECONDS.sleep(4);
		executorService.shutdownNow();
	}
}

class Sender implements Runnable {
	PipedWriter out = new PipedWriter();

	public Sender() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	PipedWriter getPipedWriter() {
		return out;
	}

	public void run() {
		char a;
		for (a = 'a'; a <= 'z'; a++) {
			try {
				out.write(a);
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (IOException e) {
				// TODO: handle exception
				System.out.println("Sender writing is failed");
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("Sender writing is interrupted");
			}
		}
	}
}

class Receiver implements Runnable{
	PipedReader in;
	public Receiver(PipedWriter out) {
		// TODO 自動生成されたコンストラクター・スタブ
		try {
			in = new PipedReader(out);
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("Reveiver reader creation is failed");
		}
	}
	public void run(){
		try{
		while(true){
			System.out.println("receive "+(char)in.read());
		}
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("Recevier reading is interrupted");
		}
	}
}
