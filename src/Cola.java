import java.util.Arrays;
import java.util.Random;


public class Cola {
	private static final int MAX_TIME = 1000;
	private static final int NOENCONTRADO=-1;
	static boolean []cajasDisponible;
	static boolean cajasDisponiblesisFull=false;
	class Nodo {
		int cliente;
		Nodo sig;
	}

	Nodo raiz, fondo;
	public Cola(int numCajas){
		cajasDisponible = new boolean[numCajas];
		Arrays.fill(cajasDisponible, Boolean.TRUE);
	}
	public void setDisponibilidad(boolean disponible, int numCaja){
		cajasDisponible[numCaja]= disponible;
	}
	public void setDisponibilidadCajas(boolean disponible){
		cajasDisponiblesisFull= disponible;
	}
	private boolean vacia() {
		if (raiz == null)
			return true;
		else
			return false;
	}
	//Itera por el array de disponibilidad de las cajas
	private int checkCajaLibre(){
		int numCaja=NOENCONTRADO;
		boolean cajaLibreEncontrada=false;
		for (int i=0; i<cajasDisponible.length&& !cajaLibreEncontrada;i++){	
			if(cajasDisponible[i]==true){
				numCaja=i;
				cajaLibreEncontrada=true;
			}
			System.out.println(" Posicion cajas libres  " + i + " --> " +cajasDisponible[i] );
		}
		if(!cajaLibreEncontrada){
			cajasDisponiblesisFull=true;
		}else
			cajasDisponiblesisFull=false;
		return numCaja;
		
	}
	
	int indiceCajaLibre;
	synchronized public int esperar(int id_cliente) throws InterruptedException {
		Nodo nuevo;
		nuevo = new Nodo();
		nuevo.cliente = id_cliente;
		nuevo.sig = null;
		
		indiceCajaLibre=-123213123;
		if (vacia()) {
			raiz = nuevo;
			fondo = nuevo;
		} else {
			fondo.sig = nuevo;
			fondo = nuevo;
		}
		indiceCajaLibre=checkCajaLibre();

		while (raiz.cliente != id_cliente && cajasDisponiblesisFull) {
			// Me bloqueo hasta que sea mi turno
			wait();
			indiceCajaLibre=checkCajaLibre();
			
		}
		return indiceCajaLibre;
	}
	
	synchronized public void atender(int pago,int numCaja) throws InterruptedException {

		if (raiz == fondo) {
			raiz = null;
			fondo = null;
		} else {
			// La forma de sacar nodos de la cola es, haciéndoles pasar a ser
			// raiz.
			raiz = raiz.sig;
		}
		int tiempo_atencion = new Random().nextInt(MAX_TIME);
		
		cajasDisponible[numCaja]=false;
		//Tiempo atención con la cajera
		Thread.currentThread().sleep(tiempo_atencion);
		//cajasDisponible[numCaja]=true;
		System.out.println("Soy "+pago+"Acabo de salir de la caja, el estado de esta caja "+numCaja +" es "+ cajasDisponible[numCaja]);
		//cajasDisponiblesisFull = false;
		Resultados.ganancias += pago;
		Resultados.clientes_atendidos++;
		// "Despierta" a un cliente de la misma caja que haya salido del while
		// (raiz.cliente != id_cliente), es decir al que haya salido de la caja.
		notifyAll();
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
