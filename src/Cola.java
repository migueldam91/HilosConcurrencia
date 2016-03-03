import java.util.Random;


public class Cola {
	private static final int MAX_TIME = 1000;
	private Caja[] cajas;
	class Nodo {
		int cliente;
		Nodo sig;
	}

	Nodo raiz, fondo;
	public Cola(){
		
	}
	
	private boolean vacia() {
		if (raiz == null)
			return true;
		else
			return false;
	}
	
	synchronized public void esperar(int id_cliente) throws InterruptedException {
		Nodo nuevo;
		nuevo = new Nodo();
		nuevo.cliente = id_cliente;
		nuevo.sig = null;
		if (vacia()) {
			raiz = nuevo;
			fondo = nuevo;
		} else {
			fondo.sig = nuevo;
			fondo = nuevo;
		}
		while (raiz.cliente != id_cliente) {
			// Me bloqueo hasta que sea mi turno
			wait();
		}
	}

	synchronized public void atender(int pago) throws InterruptedException {

		if (raiz == fondo) {
			raiz = null;
			fondo = null;
		} else {
			// La forma de sacar nodos de la cola es, haciéndoles pasar a ser
			// raiz.
			raiz = raiz.sig;
		}
		int tiempo_atencion = new Random().nextInt(MAX_TIME);
		Thread.sleep(tiempo_atencion);
		Resultados.ganancias += pago;
		Resultados.clientes_atendidos++;
		// "Despierta" a un cliente de la misma caja que haya salido del while
		// (raiz.cliente != id_cliente), es decir al que haya salido de la caja.
		notify();
	}

	synchronized public void imprimir() {
		Nodo reco = raiz;
		// Hasta que no llegue al final de la caja.
		// for viewing what's going on in the Caja/Cola
		while (reco != null) {
			System.out.print(reco.cliente + "-");
			reco = reco.sig;
		}
		System.out.println();
	}
}
