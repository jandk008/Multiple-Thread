package zidane.multiThread;

/*
* In a restaurant, there are two staff who are cook and waiter. And waiter accept the order to cook, then cook make the meal
* then waiter take the meal to customer.
* These two thread is to provide meal service with cooperation 
*/
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Restaurant {
	ExecutorService executorService = Executors.newCachedThreadPool();
	public static void main(String[] args) {
		Meal meal = new Meal(0, "ordered");

		Restaurant restaurant = new Restaurant();
		restaurant.executorService.execute(restaurant.new Cook(meal));
		restaurant.executorService.execute(new Waiter(meal));
	}

class Cook implements Runnable{
	Meal meal;
	boolean isDone = false;
	public Cook(Meal meal) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.meal = meal;
	}
public void run(){
		cookMeal();
	}
	void cookMeal() {
	    // use synchronized to lock the object 
			synchronized (meal) {
				try {
					while (!Thread.interrupted()) {
					//here must use while to get rid of unexpected change to condition(here is status)
					while (meal.getStatus().equals("cooked"))
					  //the meal is done but not delivered by waiter, so cook wait
						meal.wait();
					if(meal.getNumber()==5){
						System.out.println("厨師の仕事が完了");
						executorService.shutdownNow();
						return;
					}
					meal.setNumber(meal.getNumber()+1);
					TimeUnit.SECONDS.sleep(2);
					System.out.println("厨師作出第"+meal.getNumber()+"道菜");
					meal.setStatus("cooked");
					meal.notifyAll();
					 }
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
			}
	}
 }
}
class Waiter implements Runnable{
	Meal meal;
	Waiter(Meal meal) {
		this.meal = meal;
	}
public void run(){
		order();
}
	void order() {
			synchronized (meal) {
				try {
					while (!Thread.interrupted()) {
					while (meal.getStatus().equals("ordered"))
						meal.wait();
					System.out.println("スタッフ上第"+meal.getNumber()+"道菜");
					meal.setStatus("ordered");
					meal.notifyAll();
					}
				} catch (InterruptedException e) {
					// TODO: handle exception
				}
		}
	}
}
class Meal{
	private int number;
	private String status;
	Meal(){
		number = 0;
		status = "Null";
	}
	Meal(int n,String status){
		number=n;
		this.status= status;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String name) {
		this.status = name;
	}

}
