public class Ex03 extends Thread
{
	int[] array;
	int[] resultado;
	int inicio,fim;
	
	Ex03(int[] array,int inicio,int fim,int[] resultado){
		this.array = array;
		this.inicio = inicio;
		this.fim = fim;
		this.resultado= resultado;
	}
	
	@Override
	public void run()
	{
		for(int i = inicio; i < fim; i++) {
			resultado[i] = 4*array[i]+20;
			System.out.println("resultado["+i+"] = "+array[i]);
		}
	}
	
	public static void main(String[] args)
	{
		int[] array = new int[1200];
		for(int i = 0; i < array.length; i++) {
			array[i] = i+1;
		}
		
		Ex03 ct = new Ex03(array,0,400,array);
		Ex03 ct2 = new Ex03(array,400,800,array);
		
		ct.start();
		ct2.start();
		for(int i = 800; i < array.length; i++) {
			array[i] = 4*array[i]+20;
			System.out.println("resultado["+i+"] = "+array[i]);
		}
		
		for(int i = 0; i < array.length; i++) {
			System.out.println("resultado["+i+"] = "+array[i]);
		}
	}
}