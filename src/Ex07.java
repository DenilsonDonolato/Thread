import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Ex07 implements Callable<Integer>
{
	int[] array;
	int[] contagem;
	int qtdValores, inicio, fim;
	
	//static Semaphore semaphore = new Semaphore(1);
	static ReentrantLock lock = new ReentrantLock();
	
	
	Ex07(int[] array, int inicio, int fim, int[] contagem, int qtdValores)
	{
		this.array = array;
		this.contagem = contagem;
		this.qtdValores = qtdValores;
		this.inicio = inicio;
		this.fim = fim;
	}
	
	@Override
	public Integer call() throws Exception
	{
		for(int i = inicio; i < fim; i++) {
			for(int j = 0; j < qtdValores; j++) {
				if(array[i] == j) {
					lock.lock();
					try {
						//semaphore.acquire();
						contagem[j]++;
						//semaphore.release();
					} finally {
						lock.unlock();
					}
				}
			}
		}
		return 0;
	}
	
	public static void main(String[] args)
	{
		int[] array = new int[10000];
		int qtdValores = 4;
		for(int i = 0; i < array.length; i++) {
			array[i] = i % qtdValores;
		}
		int[] contagem = new int[qtdValores];
		Arrays.fill(contagem, 0);
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		
		Ex06[] ct = new Ex06[10];
		for(int i = 0; i < ct.length; i++) {
			ct[i] = new Ex06(array, i * 1000, i * 1000 + 1000, contagem, qtdValores);
		}
		
		for(int i = 0; i < ct.length; i++) {
			//submit Callable tasks to be executed by thread pool
			Future<Integer> future = executor.submit(ct[i]);
			//add Future to the list, we can get return value using Future
			list.add(future);
		}
		int res = 0;
		boolean teste = false;
		while(true) {
			try {
				if(teste) {
					System.out.println("Done");
					//shut down executor service
					executor.shutdown();
					break;
				}
				teste = true;
				for(Future<Integer> fut : list) {
					res += fut.get();
					teste = teste && fut.isDone();
				}
			} catch(InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < qtdValores; i++) {
			System.out.println("Valor: " + i + ", encontrado " + contagem[i] + " vezes.");
		}
	}
}