public class Ex02 extends Thread
{
	PessoaEx02 pessoaEx02;
	
	Ex02(PessoaEx02 pessoaEx02){
		this.pessoaEx02 = pessoaEx02;
	}
	@Override
	public void run()
	{
		System.out.println("Nome: "+ pessoaEx02.nome);
		System.out.println("Numero: "+ pessoaEx02.numero);
		System.out.println("Morada: "+ pessoaEx02.morada);
	}
	
	public static void main(String[] args)
	{
		PessoaEx02[] array = new PessoaEx02[5];
		array[0] = new PessoaEx02("Denilson","0","Rua");
		array[1] = new PessoaEx02("Viviane","1","Avenida");
		array[2] = new PessoaEx02("Mariana","2","Apartemento");
		array[3] = new PessoaEx02("Lucas","3","Casa");
		array[4] = new PessoaEx02("Allan","4","Favela");
		
		Ex02 ct = new Ex02(array[0]);
		Ex02 ct2 = new Ex02(array[1]);
		Ex02 ct3 = new Ex02(array[2]);
		Ex02 ct4 = new Ex02(array[3]);
		Ex02 ct5 = new Ex02(array[4]);
		
		ct.start();
		ct2.start();
		ct3.start();
		ct4.start();
		ct5.start();
		
	}
}

class PessoaEx02
{
	String nome;
	String numero;
	String morada;
	
	PessoaEx02(String nome, String numero, String morada){
		this.nome= nome;
		this.numero= numero;
		this.morada = morada;
	}
	
}