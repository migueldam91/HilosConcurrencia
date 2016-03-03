import java.util.Random;

public class SuperMarket {
	public static void main(String[] args) throws InterruptedException {
		Cola cola = new Cola();
//		int N = Integer.parseInt(args[0]);
		int N=10;
		Caja cajas[] = new Caja[N];
		for (int i = 0; i < N; i++) {
			cajas[i] = new Caja();
		}
//		int M = Integer.parseInt(args[1]);
		int M = 100;
		Cliente clientes[] = new Cliente[M];
		for (int j, i = 0; i < M; i++) {
			// Seleccionamos ya en qué caja se situara
			j = new Random().nextInt(N);
			clientes[i] = new Cliente(i, cajas[j],cola);
			clientes[i].start();
		}
		try {
			for (int i = 0; i < M; i++) {
				clientes[i].join();
			}
		} catch (InterruptedException ex) {
			System.out.println("Hilo principal interrumpido.");
		}
		System.out.println("Supermercado cerrado.");
		System.out.println("Ganancias: " + Resultados.ganancias);
		System.out.println("Tiempo medio de espera: "
				+ (Resultados.tiempo_espera / Resultados.clientes_atendidos));
	}
}
