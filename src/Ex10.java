import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Ex10 implements Callable<Integer>
{
	int[] array;
	int[] contagem;
	int qtdValores, inicio, fim;
	static Contador[] contadores = new Contador[23];
	static Ex10[] ct = new Ex10[10];
	
	//	static boolean[] semaforos = new boolean[23];
	static ReentrantLock lock = new ReentrantLock();
	
	
	Ex10(int[] array, int inicio, int fim, int[] contagem, int qtdValores)
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
				if(array[i] == j + 1) {
					lock.lock();
					try {
						contagem[j]++;
						if(contagem[j] % 10 == 0) {
							synchronized(contadores[j]) {
								contadores[j].pausado = false;
								contadores[j].wait();
							}
						}
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
		int qtdValores = 23;
		for(int i = 0; i < array.length; i++) {
			array[i] = i % qtdValores + 1;
		}
		int[] contagem = new int[qtdValores];
		Arrays.fill(contagem, 0);

//		for(int i = 0; i < semaforos.length; i++) {
//			semaforos[i] = false;
//		}
		
		ExecutorService executor = Executors.newFixedThreadPool(33);
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		
		
		for(int i = 0; i < ct.length; i++) {
			ct[i] = new Ex10(array, i * 1000, i * 1000 + 1000, contagem, qtdValores);
		}
		for(int i = 0; i < contadores.length; i++) {
			contadores[i] = new Contador(i + 1, contagem);
		}
		
		for(int i = 0; i < ct.length; i++) {
			//submit Callable tasks to be executed by thread pool
			Future<Integer> future = executor.submit(ct[i]);
			//add Future to the list, we can get return value using Future
			list.add(future);
		}
		for(int i = 0; i < contadores.length; i++) {
			//submit Callable tasks to be executed by thread pool
			Future<Integer> future = executor.submit(contadores[i]);
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
			System.out.println("Jogador: " + (i + 1) + ", favoritado " + contagem[i] + " vezes.");
		}
	}
	
	private static class Contador implements Callable<Integer>
	{
		int busca;
		boolean pausado;
		int[] contagem;
		
		Contador(int busca, int[] contagem)
		{
			this.busca = busca;
			this.contagem = contagem;
			pausado = true;
		}
		
		@Override
		public Integer call() throws Exception
		{
			synchronized(ct[busca - 1]) {
				while(true) {
					while(pausado){
						wait();
					}
					System.out.println("Jogador: " + busca + ", Encontrado: " + contagem[busca - 1]);
					ct[busca-1].notifyAll();
					pausado = true;
				}
			}
		}
	}
}