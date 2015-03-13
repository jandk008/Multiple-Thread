package zidane.multiThread;

/*
*
*
*/
 class Account {
	private  int balance = 0;
	Account (int balance){
		this.setBalance(balance);
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	public int getBalance() {
		return balance;
	}
	public synchronized void withdraw(int money){
		if (balance >= money){
			setBalance(balance-money);
			System.out.println("                 引き　　　　　残高");
			System.out.println("               "+money+"          "+getBalance());
		}
		else {
			System.out.println("                 引き　　　　　残高");
			System.out.println("                  "+money+"              残高不足");
		}
	}
	public void checkBalance() {
		System.out.println("残高     "+ getBalance());
	}
}


/*
* class to withdraw the money from the account.
* A and B individulely withdraw money from the same account in different thread
*/
public class Withdrawer  extends Thread{
	private Account account ;
	public Withdrawer (Account account){
		this.account = account;
	}
	public void run(){
		account.withdraw(80);
	}

	 public static void main(String[] args){
		 Account account = new Account(100);
		 Withdrawer a = new Withdrawer(account);
		 Withdrawer b = new Withdrawer(account);
		 a.account.checkBalance();

		 a.start();
		 b.start();
	 }
}
