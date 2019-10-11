import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Ex05 implements Callable<Double>
{
	int[] array;
	int inicio,fim;
	
	Ex05(int[] array,int inicio,int fim){
		this.array = array;
		this.inicio = inicio;
		this.fim = fim;
	}
	
	@Override
	public Double call() throws Exception
	{
		double res = 0.0;
		for(int i = inicio; i < fim; i++) {
			res += array[i];
		}
		return res;
	}
	
	public static void main(String[] args)
	{
		int[] array = new int[1000];
		Arrays.fill(array, 5);
		
		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Future<Double>> list = new ArrayList<Future<Double>>();
		
		Ex05[] ct = new Ex05[5];
		for(int i = 0; i < 5; i++) {
			ct[i] = new Ex05(array,i*200, i*200+200);
		}
		
		for(int i=0; i < 5; i++){
			//submit Callable tasks to be executed by thread pool
			Future<Double> future = executor.submit(ct[i]);
			//add Future to the list, we can get return value using Future
			list.add(future);
		}
		int res=0;
		boolean teste=false;
		while(true){
			try {
				if(teste){
					System.out.println("Done");
					//shut down executor service
					executor.shutdown();
					break;
				}
				teste=true;
				for( Future<Double> fut : list ) {
					res+=fut.get();
					teste = teste && fut.isDone();
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Soma dos saldos: "+res);
	}
}