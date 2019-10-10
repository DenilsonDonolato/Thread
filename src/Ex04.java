import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

public class Ex04 implements Callable<Integer>
{
	int[] array;
	int busca,inicio,fim;
	
	Ex04(int busca, int[] array,int inicio,int fim){
		this.array = array;
		this.inicio = inicio;
		this.fim = fim;
		this.busca = busca;
	}
	
	@Override
	public Integer call() throws Exception
	{
		for(int i = inicio; i < fim; i++) {
			if(busca == array[i]) {
				System.out.println("Econtrado valor: "+busca+", na posicao: "+i);
				String s = Thread.currentThread().getName();
				//System.out.println(s);
				try {
					return Integer.parseInt(s.substring(14));
				} catch(Exception e) {
					return 0;
				}
			}
		}
		return 0;
	}
	
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		int busca;
		int[] array = new int[1000];
		for(int i = 0; i < array.length; i++) {
			array[i]=i+1;
		}

		ExecutorService executor = Executors.newFixedThreadPool(5);
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		
		System.out.print("Digite o valor a ser buscado: ");
		busca = scanner.nextInt();
		
		Ex04[] ct = new Ex04[5];
		for(int i = 0; i < 5; i++) {
			ct[i] = new Ex04(busca,array,i*200, i*200+200);
		}
		
		for(int i=0; i < 5; i++){
			//submit Callable tasks to be executed by thread pool
			Future<Integer> future = executor.submit(ct[i]);
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
				for( Future<Integer> fut : list ) {
					res+=fut.get();
					teste = teste && fut.isDone();
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Encontrado na thread: "+res);
	}
}