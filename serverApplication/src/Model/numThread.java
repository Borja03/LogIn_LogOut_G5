package Model;

/**
 * Esta clase se utiliza para llevar un conteo seguro de hilos (threads)
 * activos en un entorno multihilo.
 *
 * <p>La clase <code>numThread</code> proporciona métodos sincronizados para incrementar,
 * decrementar y obtener el número actual de hilos activos. Esto es útil en
 * escenarios donde se requiere un seguimiento del número de conexiones o tareas
 * concurrentes que se están ejecutando, asegurando que el conteo se mantenga
 * consistente en un entorno multihilo.</p>
 *
 * @author Alder
 */
public class numThread {
    private Integer threadCount = 0;

    /**
     * Incrementa el contador de hilos activos en uno.
     * Este método es sincronizado para garantizar que solo un hilo a la vez
     * pueda modificar el valor del contador.
     */
    public synchronized void increment() {
        threadCount++;
    }

    /**
     * Decrementa el contador de hilos activos en uno.
     * Este método es sincronizado para asegurar que solo un hilo a la vez
     * pueda modificar el valor del contador.
     */
    public synchronized void decrement() {
        threadCount--;
    }

    /**
     * Devuelve el número actual de hilos activos.
     * Este método es sincronizado para garantizar la consistencia del valor devuelto.
     *
     * @return El número actual de hilos activos.
     */
    public synchronized int value() {
        return threadCount;
    }
}
