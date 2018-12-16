/**
 * Raul Barbosa 2014-11-07
 */
package primes.model;


import dropsrc.src.RMI;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class PrimesBean {
	private int number;
	private String username;

	//Interface RMI
	RMI interf;

	// Configuracoes RMI
	private String hostRMI, nomeRMI;
	private int portoRMI;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public ArrayList<String> getPrimes() {
		ArrayList<String> primes = new ArrayList<String>();
		int candidate = 2;
		for(int count = 0; count < number; candidate++)
			if(isPrime(candidate)) {
				primes.add((new Integer(candidate)).toString());
				count++;
			}
		return primes;
	}

	private boolean isPrime(int number) {
		if((number & 1) == 0)
			return number == 2;
		for(int i = 3; number >= i*i; i += 2)
			if((number % i) == 0)
				return false;
		return true;
	}

	public PrimesBean() throws NotBoundException, MalformedURLException, RemoteException {
		// inicializar configuracoes

		// ler configuracoes RMI
		hostRMI ="localhost";
		portoRMI = 7000;
		nomeRMI = "DropMusic";

		// ligar ao RMI
		interf = (RMI) Naming.lookup("rmi://" + hostRMI + ":" + portoRMI + "/" + nomeRMI);
		System.out.println("Conexao do Bean ao RMI iniciada com sucesso!");

	}
}
