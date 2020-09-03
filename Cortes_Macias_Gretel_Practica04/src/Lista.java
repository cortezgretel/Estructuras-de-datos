import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p> Clase concreta para modelar la estructura de datos Lista</p>
 * <p>Esta clase implementa una Lista genérica, es decir que es homogénea pero
 * puede tener elementos de cualquier tipo.
 * @author Luis Manuel Martinez Damaso <luismanuel@ciencias.unam.mx>
 * @version 1.0
 * @param <T>
 */
public class Lista<T> implements Listable<T>{

    /* Clase interna para construir la estructura */
    protected class Nodo{
        /* Referencias a los nodos anterior y siguiente */
        public Nodo anterior, siguiente;
        /* El elemento que almacena un nodo */
        public T elemento;

        /* Unico constructor de la clase */
        public Nodo(T elemento){
	    this.elemento = elemento;
        }

        public boolean equals(Nodo n){
         return this.siguiente == n.siguiente && this.anterior == n.anterior ? true :  false;
        }

    }

    
    private class IteradorLista implements Iterator<T>{
        /* La lista a recorrer*/
        /* Elementos del centinela que recorre la lista*/
        private Lista<T>.Nodo siguiente;
        private Lista<T>.Nodo anterior;

        public IteradorLista(){
            this.siguiente = cabeza;
        }

        @Override
        public boolean hasNext() {
            return this.siguiente != null;
        }

        @Override
        public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        else{
            T elemento1 = siguiente.elemento;
            this.anterior = siguiente;
            this.siguiente = siguiente.siguiente;
            return elemento1;
        }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    
    /* Atributos de la lista */
    protected Nodo cabeza, cola;
    protected int longitud;

    /**
     *  Constructor por omisión de la clase, no recibe parámetros.
     *  Crea una nueva lista con longitud 0.
     **/
    public Lista(){
        this.cabeza= this.cola =null;
        this.longitud=0;

    }

    /**
     *  Constructor copia de la clase. Recibe una lista con elementos.
     *  Crea una nueva lista exactamente igual a la que recibimos como parámetro.
     * @param l
     **/
    public Lista(Lista<T> l){
        Nodo nodo = l.cabeza;
        while(nodo != null){
            this.agregarAlFinal(nodo.elemento);
            nodo = nodo.siguiente;
        }
    }

    /**
     *  Constructor de la clase que recibe parámetros.
     *  Crea una nueva lista con los elementos de la estructura iterable que recibe como parámetro.
     * @param iterable
     **/
    public Lista(Iterable<T> iterable){
        for(T elementos : iterable){
            this.agregar(elementos);
        }
    }
    
    /**
     * Método que nos dice si las lista está vacía.
     * @return <code>true</code> si el conjunto está vacío, <code>false</code>
     * en otro caso.
     */
    @Override
    public boolean esVacia(){
        return longitud == 0;
    }
    
    /**
     * Método para eliminar todos los elementos de una lista
     */
    @Override
    public void vaciar(){
    cabeza = cola = null;
    longitud = 0;
    }
    
    /**
     * Método para obtener el tamaño de la lista
     * @return tamanio Número de elementos de la lista.
     **/
    @Override
    public int getTamanio(){
        return longitud;
    }
    
    /**
     * Método para agregar un elemento a la lista.
     * @param elemento Objeto que se agregará a la lista.
     */
    @Override
    public void agregar(T elemento){
    if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
    Nodo nodo = new Nodo(elemento);
    if(esVacia()){
        cabeza = cola = nodo;
    }
    else{
        cabeza.anterior = nodo;
        nodo.siguiente = cabeza;
        cabeza = nodo;
    }
      longitud++;
    }
    
    /**
     * Método para agregar al final un elemento a la lista.
     * @param elemento Objeto que se agregará al inicio de la lista.
     */
    public void agregarAlFinal(T elemento) throws IllegalArgumentException {
    if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
    Nodo nodo = new Nodo(elemento);
    if(esVacia()){
        cabeza = nodo;
        cola = cabeza;
        cabeza.anterior = null;
        cabeza.siguiente = null;
    }else{
        nodo.anterior = cola;
        cola.siguiente = nodo;
        cola = nodo;
    }
    longitud++;
    }

    /**
     * Método para obtener el primer elemento.
     */
    @Override
    public T getPrimero() throws NoSuchElementException {
        if(esVacia()) throw new NoSuchElementException();
        else{
           T elem = cabeza.elemento;
           return elem;
        }
    }

    /**
     * Método para obtener el último elemento.
     */
    public T getUltimo() throws NoSuchElementException {
        if(esVacia()) throw new NoSuchElementException();
        else{
        T elem = cola.elemento;
        return elem;
        }
    }
    
    /**
     * Método para verificar si un elemento pertenece a la lista.
     * @param elemento Objeto que se va a buscar en la lista.
     * @return <code>true</code> si el elemento esta en el lista y false en otro caso.
     */
    @Override
    public boolean contiene(T elemento) throws NoSuchElementException {
        return indiceDe(elemento) != -1;
    }

     /**
     * Elimina el nodo que se pasa
     * @param nodo a elminiar de la lista
     */
    private void eliminaNodo(Nodo nodo) {
    if(nodo == null) return;
    if(longitud == 1 && nodo.equals(cabeza)) vaciar();
    else if(nodo.equals(cabeza)) eliminarPrimero();
    else if(nodo.equals(cola)) eliminarUltimo();
        else{
            nodo.anterior.siguiente = nodo.siguiente;
            nodo.siguiente.anterior = nodo.anterior;
            longitud--;
        }
    }

    /**
     * Método para eliminar un elemento de la lista.
     * @param elemento Objeto que se eliminara de la lista.
     * todo
     */
    @Override
    public void eliminar(T elemento) throws NoSuchElementException {
    Nodo nodo = getNodo(elemento);
    eliminaNodo(nodo);
    }

    /**
     * Método para eliminar el primer elemento de la lista.
     */
    @Override
    public void eliminarPrimero() throws NoSuchElementException {
    if(esVacia()) throw new NoSuchElementException();
    if(longitud == 1) vaciar();
        else{
            cabeza = cabeza.siguiente;
            cabeza.anterior = null;
            longitud--;
        }
    }

    /**
     * Método para eliminar el ultimo elemento de la lista.
     */
    public void eliminarUltimo() throws NoSuchElementException {
    if(esVacia()) throw new NoSuchElementException();
    if(longitud == 1) vaciar();
        else{
            cola = cola.anterior;
            cola.siguiente = null;
            longitud--;
        }
    }

    /**
     * Método que devuelve la posición en la lista que tiene la primera
     * aparición del <code> elemento</code>.
     * @param elemento El elemnto del cuál queremos saber su posición.
     * @return i la posición del elemento en la lista, -1, si no se encuentra en ésta.
     */
    @Override
    public int indiceDe(T elemento) throws NoSuchElementException {
        if(esVacia()) return -1;
        int iterador = 0;
        Nodo nodo = cabeza;
        while(nodo != null){
            if(nodo.elemento.equals(elemento)) return iterador;
                nodo = nodo.siguiente;
                iterador++;
        }
      return -1;
    }

    /**
     * Método que nos devuelve el elemento que esta en la posición i
     * @param i La posición cuyo elemento deseamos conocer.
     * @return <code> elemento </code> El elemento que contiene la lista,
     * <code>null</code> si no se encuentra
     * @throws IndexOutOfBoundsException Si el índice es < 0 o >longitud()
     */
    @Override
    public T getElemento(int i)throws IndexOutOfBoundsException{
        if(i < 0 || i >= longitud) throw new IndexOutOfBoundsException();
        Nodo nodo = cabeza;
        while(i-- > 0){
            nodo = nodo.siguiente;
        }
        return nodo.elemento;
    }

    /**
     * Método que devuelve una copia de la lista, pero en orden inverso
     * @return Una copia con la lista l revés.
     */
    @Override
    public Lista<T> reversa(){
    Lista<T> reversa = new Lista<T>();
    if(esVacia()) return reversa;
    Nodo nodo = cola;
    while(nodo != null){
        reversa.agregarAlFinal(nodo.elemento);
        nodo = nodo.anterior;
    }
    return reversa;
    }

    /**
     * Método que devuelve una copia exacta de la lista
     * @return la copia de la lista.
     */
    @Override
    public Lista<T> copia(){
    Lista<T> lista = new Lista<>();
    if(esVacia()) return lista;
    Nodo nodo = cabeza;
    while(nodo != null){
        lista.agregarAlFinal(nodo.elemento);
        nodo = nodo.siguiente;
    }
    return lista;
    }

    /**
     * Método que nos dice si una lista es igual que otra.
     * @param o objeto a comparar con la lista.
     * @return <code>true</code> si son iguales, <code>false</code> en otro caso.
     */
    @Override
    public boolean equals(Object o){
    if (o == null || getClass() != o.getClass())
        return false;
    @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)o;
    if(lista.getTamanio() != longitud) return false;
    if(lista.esVacia() && this.esVacia()) return true;
        Nodo nodo = cabeza;
        int i = 0;
        while(nodo!= null){
            if(!lista.getElemento(i++).equals(nodo.elemento)) return false;
                nodo = nodo.siguiente;
        }
    return true; 
    }

    /**
     * Método que devuelve un iterador sobre la lista
     * @return java.util.Iterador -- iterador sobre la lista
     */
    @Override
    public java.util.Iterator<T> iterator(){
        return new IteradorLista();
    }


    @Override
    public String toString() {
        if (esVacia()) {
            return "[]";
        }
        Nodo nodo = cabeza;
        String cad = "[" + nodo.elemento;
        while (nodo.siguiente != null) {
            nodo = nodo.siguiente;
            cad += ", " + nodo.elemento;
        }
        return cad + "]";
    }
      
    
    /* Método auxiliar para obtener una referencia a un nodo con un elemento
    específico. Si no existe tal nodo, devuelve <code> null </code> */
    private Nodo getNodo(T elem) throws NoSuchElementException {
    Nodo nodo = cabeza;
    while(!elem.equals(nodo.elemento)){
        nodo = nodo.siguiente;
    }
    return nodo;
    }

}