public class Ex01 extends Thread
{
	String texto;
	
	Ex01(String texto){
		this.texto = texto;
	}
	@Override
	public void run()
	{
		System.out.println(texto);
	}
	
	public static void main(String[] args)
	{
		String nome = "Denilson";
		String sobrenome = "Donolato";
		String nome2 = "Mariana";
		String sobrenome2 = "Garcia";
		String nome3 = "Viviane";
		String sobrenome3 = "Ogata";
		Ex01 ct = new Ex01(nome);
		Ex01 ct2 = new Ex01(sobrenome);
		Ex01 ct3 = new Ex01(nome2);
		Ex01 ct4 = new Ex01(sobrenome2);
		Ex01 ct5 = new Ex01(nome3);
		Ex01 ct6 = new Ex01(sobrenome3);
		ct.start();
		ct2.start();
		ct3.start();
		ct4.start();
		ct5.start();
		ct6.start();
		
	}
}