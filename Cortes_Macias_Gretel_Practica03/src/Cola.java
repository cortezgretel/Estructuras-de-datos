import java.util.NoSuchElementException;
import java.util.Arrays;

/**
 * <p> Clase concreta para modelar la estructura de datos Cola</p>
 * <p>Esta clase implementa una Cola genérica, es decir que es homogénea pero
 * puede tener elementos de cualquier tipo.
 * @author Luis Manuel Martinez Damaso <luismanuel@ciencias.unam.mx>
 * @version 1.0
 * @param <T> Tipo que tienen los objetos que guarda esta cola.
 */
public class Cola<T> extends Lista<T> implements Encolable<T> {

    
    /**
     * Constructor por omisión de la clase.
     */
    public Cola() {
        //Aqui no hay que hacer nada,
        //ya que los valores por default nos sirven al crear un objeto.
    }

    /**
     * Constructor de la clase que recibe parámetros.
     * Crea una nueva cola con los elementos de la estructura iterable que recibe como parámetro.
     * @param iterable El objeto que se recibe como parámetro.
     */
    public Cola(Iterable<T> iterable) {
        for(T elementos : iterable){
            this.queue(elementos);
        }
    }

    /**
     * Constructor de la clase que recibe parámetros.
     * Crea una nueva cola con los elementos de la estructura iterable que recibe como parámetro.
     */
    public Cola(Cola<T> c) {
        Nodo nodo = c.cabeza;
        while(nodo != null){
            this.queue(nodo.elemento);
            nodo = nodo.siguiente;
        }
    }



    /**
     * Agrega un elemento en el rabo de la Cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     * <code>null</code>.
     */
    @Override
    public void queue(T elemento) throws IllegalArgumentException{
    if(elemento == null)throw new IllegalArgumentException(" Elemento inválido ");
        
        Nodo nodo = new Nodo(elemento);
        if(cola ==  null)
          cabeza = cola = nodo;
        else{
          cola.siguiente = nodo;
          cola = nodo;
        } 
        longitud++; 
    }

    /**
     * Elimina el elemento del inicio de la Cola y lo regresa.
     * @throws NoSuchElementException si la cola es vacía
     * @return el elemento en el inicio de la Cola.
     */
    @Override
    public T dequeue() throws NoSuchElementException{
        if(cabeza == null )throw new NoSuchElementException(" Es vacía ");
        T elemento = cabeza.elemento;
        if (cabeza.siguiente == null) cabeza = cola = null;
        else cabeza = cabeza.siguiente;
        longitud--;
    return elemento;
    }

    /**
     * Nos permite ver el elemento en el inicio de la Cola
     *
     * @return el siguiente elemento a desencolar.
     */
    @Override
    public T peek() throws NoSuchElementException{
        if(cabeza == null)throw new NoSuchElementException(" Es vacía ");
    return  cabeza.elemento;
    }
    
    /**
     * Método para agregar un elemento a la Cola.
     * @param elemento Objeto que se agregará a la Cola.
     */
    @Override
    public void agregar(T elemento){
        throw new UnsupportedOperationException("No se puede hacer esta operación. Para agregar elementos a una cola usa el método queue(elemento)");
    }
    
    /**
     * Método para agregar al final un elemento a la Cola. Este método
     * no debería ser llamado. Por eso va a devolver una excepción.
     * @param elemento Objeto que se agregará al inicio de la Cola.
     */
    @Override
    public void agregarAlFinal(T elemento) {
        throw new UnsupportedOperationException("No se puede hacer esta operación. Para agregar elementos a una cola usa el método queue(elemento)");
    }
    
    /**
     * Método para obtener el primer elemento. Este método
     * no debería ser llamado. Por eso va a devolver una excepción.
     * @return 
     */
    @Override
    public T getPrimero() {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Método para obtener el último elemento. Este método
     * no debería ser llamado. Por eso va a devolver una excepción.
     * @return 
     */
    @Override
    public T getUltimo() {
        throw new UnsupportedOperationException();
    }
    
    
    /**
     * Método para eliminar especifico de la Cola. Este método
     * no debería ser llamado. Por eso va a devolver una excepción.
     * @param elemento Objeto que se eliminara de la Cola.
     * todo
     */
    @Override
    public void eliminar(T elemento) {
        throw new UnsupportedOperationException("No se puede hacer esta operación. Para agregar elementos a una cola usa el método dequeue()");
    }
    
    /**
     * Método para eliminar el primer elemento de la Cola. Este método
     * no debería ser llamado. Por eso va a devolver una excepción.
     */
    @Override
    public void eliminarPrimero() {
        throw new UnsupportedOperationException("No se puede hacer esta operación. Para agregar elementos a una cola usa el método dequeue()");
    }
    
    /**
     * Método para eliminar el primer elemento de la Cola. Este método
     * no debería ser llamado. Por eso va a devolver una excepción.
     */
    @Override
    public void eliminarUltimo() {
        throw new UnsupportedOperationException("No se puede hacer esta operación. Para agregar elementos a una cola usa el método dequeue()");
    }


    @Override
    public String toString() {
        String s = "[";
        Nodo n = this.cabeza;
        while (n != null) {
            if (n.siguiente == null) {
                s += n.elemento;
            } else {
                s += n.elemento + ",";
            }
            n = n.siguiente;
        }
        s += "]";
        return s;

    }


}
