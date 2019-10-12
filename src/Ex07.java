import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class Ex07 implements Callable<Integer>
{
	static String[] cidades = {"CidadeA", "CidadeB", "CidadeC", "CidadeD"};
	
	String cidadeOrigem;
	String cidadeDestino;
	String cidadeAtual;
	String partida;
	String chegada;
	int numComboio;
	
	static Semaphore[] semaforos = new Semaphore[3];
	static ReentrantLock lock = new ReentrantLock();
	
	
	Ex07(String cidadeOrigem, String cidadeDestino, int numComboio)
	{
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		partida = "Partida da " + cidadeOrigem + " do comboio número " + numComboio + ": " + formatter.format(date);
		this.cidadeOrigem = cidadeOrigem;
		this.cidadeAtual = cidadeOrigem;
		this.cidadeDestino = cidadeDestino;
		this.numComboio = numComboio;
	}
	
	@Override
	public Integer call() throws Exception
	{
		while(!cidadeAtual.equals(cidadeDestino)) {
//			lock.lock();
			comboioAnda(cidadeAtual, cidadeDestino);
			lock.lock();
			if(cidadeAtual.equals(cidadeDestino)) {
				System.out.println("-------Comboio "+numComboio+"--------Chegou no destino--------");
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				chegada = "Chegada em " + cidadeDestino + " do comboio número " + numComboio + ": " + formatter.format(date);
				System.out.println(partida);
				System.out.println(chegada);
			}
			lock.unlock();
		}
		return 0;
	}
	
	void comboioAnda(String cidadeAtual, String cidadeDestino) throws Exception
	{
		if(cidadeAtual.equals(cidades[0])) {
			semaforos[0].acquire();
			this.cidadeAtual = cidades[1];
			lock.lock();
			System.out.println("Comboio número: " + numComboio);
			System.out.println("Rota: A - B");
			System.out.println("Origem: " + cidadeOrigem + ", Atual: " + this.cidadeAtual + ", Destino: " + cidadeDestino);
			lock.unlock();
			semaforos[0].release();
		} else if(cidadeAtual.equals(cidades[2])) {
			semaforos[1].acquire();
			this.cidadeAtual = cidades[1];
			lock.lock();
			System.out.println("Comboio número: " + numComboio);
			System.out.println("Rota: C - B");
			System.out.println("Origem: " + cidadeOrigem + ", Atual: " + this.cidadeAtual + ", Destino: " + cidadeDestino);
			lock.unlock();
			semaforos[1].release();
		} else if(cidadeAtual.equals(cidades[3])) {
			semaforos[2].acquire();
			this.cidadeAtual = cidades[1];
			lock.lock();
			System.out.println("Comboio número: " + numComboio);
			System.out.println("Rota: D - B");
			System.out.println("Origem: " + cidadeOrigem + ", Atual: " + this.cidadeAtual + ", Destino: " + cidadeDestino);
			lock.unlock();
			semaforos[2].release();
		} else {
			if(cidadeDestino.equals(cidades[0])) {
				semaforos[0].acquire();
				this.cidadeAtual = cidades[0];
				lock.lock();
				System.out.println("Comboio número: " + numComboio);
				System.out.println("Rota: B - A");
				System.out.println("Origem: " + cidadeOrigem + ", Atual: " + this.cidadeAtual + ", Destino: " + cidadeDestino);
				lock.unlock();
				semaforos[0].release();
			} else if(cidadeDestino.equals(cidades[2])) {
				semaforos[1].acquire();
				this.cidadeAtual = cidades[2];
				lock.lock();
				System.out.println("Comboio número: " + numComboio);
				System.out.println("Rota: B - C");
				System.out.println("Origem: " + cidadeOrigem + ", Atual: " + this.cidadeAtual + ", Destino: " + cidadeDestino);
				lock.unlock();
				semaforos[1].release();
			} else {
				semaforos[2].acquire();
				this.cidadeAtual = cidades[3];
				lock.lock();
				System.out.println("Comboio número: " + numComboio);
				System.out.println("Rota: B - D");
				System.out.println("Origem: " + cidadeOrigem + ", Atual: " + this.cidadeAtual + ", Destino: " + cidadeDestino);
				lock.unlock();
				semaforos[2].release();
			}
		}
	}
	
	public static void main(String[] args)
	{
		/*	Semaforo 0: Rota entre A - B
			Semaforo 1: Rota entre B - C
			Semaforo 2: Rota entre B - D
		 */
		for(int i = 0; i < semaforos.length; i++) {
			semaforos[i] = new Semaphore(1);
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(17);
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		
		Ex07[] ct = new Ex07[17];
		for(int i = 0; i < ct.length; i++) {
			if(i < 10) {
				ct[i] = new Ex07(cidades[0], cidades[3], i + 1);
			} else {
				ct[i] = new Ex07(cidades[2], cidades[0], i + 1);
			}
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
	}
}