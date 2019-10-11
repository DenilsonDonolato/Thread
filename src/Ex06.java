import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Ex06 implements Callable<Integer>
{
	int[] array;
	int[] contagem;
	int qtdValores,inicio,fim;
	static boolean[] mutex = new boolean[10];
	
	
	Ex06(int[] array,int inicio,int fim,int[] contagem, int qtdValores){
		this.array = array;
		this.contagem = contagem;
		this.qtdValores = qtdValores;
		this.inicio = inicio;
		this.fim = fim;
	}
	
	@Override
	public Integer call() throws Exception
	{
		boolean teste = false;
		boolean entrada = false;
		for(int i = inicio; i < fim; i++) {
			for(int j = 0; j < qtdValores;j++){
				if(array[i]==j){
					while(true){
						entrada = true;
						for( boolean thread: mutex) {
							entrada = entrada && !thread;
						}
						if(entrada){
							mutex[j]=true;
						}
						if(mutex[j]=true){
							contagem[j]++;
							break;
						}
						mutex[j]=false;
					}
					mutex[j]=false;
				}
			}
		}
		return 0;
	}
	
	public static void main(String[] args)
	{
		int[] array = new int[10000];
		int qtdValores = 10;
		for(int i = 0; i < array.length; i++) {
			array[i] = i%qtdValores;
		}
		int[] contagem = new int[qtdValores];
		Arrays.fill(contagem,0);
		
		for(int i = 0; i < 10; i++) {
			mutex[i] = false;
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		
		Ex06[] ct = new Ex06[10];
		for(int i = 0; i < ct.length; i++) {
			ct[i] = new Ex06(array,i*1000, i*1000+1000,contagem,qtdValores);
		}
		
		for(int i=0; i < ct.length; i++){
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
		for(int i = 0; i < qtdValores; i++) {
			System.out.println("Valor: "+i+", encontrado "+contagem[i]+" vezes.");
		}
	}
}