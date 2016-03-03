import java.util.Random;

class Cliente extends Thread {
	// Tiempo m�ximo que tarda el cliente en hacer la compra.
private static final int MAX_DELAY = 2000;
	private static final int MAX_COST = 100;
	private int id;
	private Caja caja;
	private Cola cola;
	//Param caja: for sake of synchronization
	Cliente(int id, Caja caja,Cola cola) {
		this.id = id;
		this.caja = caja;
		this.cola=cola;
	}
	public void run() {
		try {
			System.out.println("Cliente " + id + " realizando compra");
			//Tiempo que est� haciendo la compra
			Thread.sleep(new Random().nextInt(MAX_DELAY));
			long s = System.currentTimeMillis();
			//Caja act�a como nexo de sincronizaci�n entre todos los objetos que lo poseen.
			caja.esperar(id);
			System.out.print("Cliente " + id + " en cola con ");
			
			caja.imprimir();
			
			caja.atender(new Random().nextInt(MAX_COST));
			System.out.println("Cliente " + id + " atendido");
			long espera = System.currentTimeMillis() - s;
			Resultados.tiempo_espera += espera;
			System.out.println("Cliente " + id + " saliendo despu�s de esperar " + espera);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

//En el modern,  hay una unica cola que es el objeto que comparten todos los clientes . Esta cola contiene un puntero cola y un array de cajas.