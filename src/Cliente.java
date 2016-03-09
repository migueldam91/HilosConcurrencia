import java.util.Random;

class Cliente extends Thread {
	// Tiempo máximo que tarda el cliente en hacer la compra.
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
			//System.out.println("Cliente " + id + " realizando compra");
			//Tiempo que está haciendo la compra
			Thread.sleep(new Random().nextInt(MAX_DELAY));
			long s = System.currentTimeMillis();
			//Caja actúa como nexo de sincronización entre todos los objetos que lo poseen.
//			Modern
			//caja.esperar(id);
			int numCaja= cola.esperar(id);
			System.out.println("VALOR CAJA: " + numCaja);
			//System.out.print("Cliente " + id + " en cola con ");
//			Modern
//			caja.imprimir();
			cola.imprimir();
//			caja.atender(new Random().nextInt(MAX_COST));
			
			cola.atender(new Random().nextInt(MAX_COST), numCaja);
			//cola.setDisponibilidadCajas(true);
			System.out.println("Cliente " + id + " atendido en " + numCaja);
			long espera = System.currentTimeMillis() - s;
			Resultados.tiempo_espera += espera;
			
			
			//System.out.println("Cliente " + id + " saliendo después de esperar " + espera);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

//En el modern,  hay una unica cola que es el objeto que comparten todos los clientes . Esta cola contiene un puntero cola y un array de cajas.
