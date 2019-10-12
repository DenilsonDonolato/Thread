public class Ex09 extends Thread
{
	int[] array;
	int inicio, fim;
	final ResourceLock lock;
	
	Ex09(int[] array, int inicio, int fim, ResourceLock lock)
	{
		this.array = array;
		this.inicio = inicio;
		this.fim = fim;
		this.lock = lock;
	}
	
	@Override
	public void run()
	{
		for(int i = inicio; i < fim; i++) {
			array[i] = 2 * array[i] + 10;
//			System.out.println("resultado["+i+"] = "+array[i]+"!!!!!!!!!!!!!!");
		}
		try {
			synchronized(lock) {
				while(lock.flag != inicio/200){
					lock.wait();
				}
				for(int i = inicio; i < fim; i++) {
					System.out.println("resultado[" + i + "] = " + array[i]);
				}
				lock.flag = lock.flag+1;
				lock.notifyAll();
			}
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		int[] array = new int[1000];
		ResourceLock lock = new ResourceLock();
		for(int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		
		Ex09[] ct = new Ex09[5];
		for(int i = 0; i < ct.length; i++) {
			ct[i] = new Ex09(array, i * 200, i * 200 + 200,lock);
		}
		
		for(Ex09 thread : ct) {
			thread.start();
		}
		System.out.println("Threads esperam para fazer na sequencia:");
		
	}
}
class ResourceLock{
	public volatile int flag = 0;
}