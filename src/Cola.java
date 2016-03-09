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
	public int checkCajaLibre(){
		int numCaja=NOENCONTRADO;
		boolean cajaLibreEncontrada=false;
		for (int i=0; i<cajasDisponible.length&& !cajaLibreEncontrada;i++){	
			//System.out.println(" Caja " + i + " --> " +cajasDisponible[i] );
			if(cajasDisponible[i]==true){
				numCaja=i;
				cajaLibreEncontrada=true;	
				setDisponibilidad(false, numCaja);
			}
			
		}
//		if(!cajaLibreEncontrada){
//			cajasDisponiblesisFull=true;
//		}else{
//			cajasDisponiblesisFull=false;
//		}
			return numCaja;
		
	}
	
	int indiceCajaLibre;
	synchronized public int esperar(int id_cliente) throws InterruptedException {
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
		boolean encendido=true;
		while (encendido){
			indiceCajaLibre=checkCajaLibre();
			if(indiceCajaLibre==NOENCONTRADO || raiz.cliente== id_cliente){
				wait();
			}else{
				encendido=false;
				return indiceCajaLibre;
			}
			
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

//		cajasDisponible[numCaja]=false;
		//Tiempo atención con la cajera
		Thread.sleep(tiempo_atencion);
		setDisponibilidad(true, numCaja);
		Resultados.ganancias += pago;
		Resultados.clientes_atendidos++;
		
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
