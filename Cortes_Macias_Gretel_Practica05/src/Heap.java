import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase abstracta para modelar montículos. Las clases concretas pueden ser un montículo mínimo
 * o máximo.
 */
public abstract class Heap<T extends Comparable<T>> implements Coleccionable<T> {

    /**
     * Clase interna para modelar el iterador
     */
    private class Iterador implements Iterator<T> {

        private int siguiente;

        @Override
        public boolean hasNext() {
	    return siguiente<getTamanio();
        }

        @Override
        public T next(){
            if(!hasNext()) throw new NoSuchElementException();
            //error
	    return arreglo[siguiente++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /**
     * Arreglo donde se almacenarán los elementos del montículo.
     **/
    private T[] arreglo;
    /**
     * Cantidad de elementos almacenados en el montículo.
     **/
    private int tamanio;
    
    
    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] creaArregloGenerico(int n) {
        return (T[])(new Comparable[n]);
    }

    /**
     * Constructor que no recibe parámetros, crea un arreglo de un tamaño arbitrario.
     * Se recomienda que sea de un tamaño que sea una potencia de 2.
     **/
    public Heap() {
	   arreglo = creaArregloGenerico(10);
    }

    /**
     * Constructor que recibe una estructura iterable como parámetro.
     * Agrega todos los elementos en el orden en que se recorre la estructura dada.
     **/
    public Heap(Iterable<T> it) {
	for(T elementos : it)
        this.agregar(elementos);        
    }
    
    /**
     * Método abstracto que se va a usar para comparar dos elementos del heap.
     * Se deja la implementación a las clases concretas, pues dependiendo de éstas el orden es
     * uno o el inverso, según sea el caso.
     * @param elemento1
     * @param elemento2
     * @return true si elemento1 tiene mayor prioridad que elemento2, false en otro caso
     */
    abstract protected boolean comparador(T elemento1,T elemento2);


    /**
     * Método que nos da la posición del padre del índice dado
     **/
    private int padre(int indiceElemento) {
    int padre = 0;
	for(int i=1; i<arreglo.length ; i++){
        if (indiceElemento==i){
                padre = (i-1)/2;
        }
    }
    return padre;
    }

    /**
     * Método que nos da la posición del hijo izquierdo del índice dado
     **/
    private int izquierdo(int indiceElemento) {
	int izquierdo = 0;
    for(int i=0; i<arreglo.length ; i++){
        if (indiceElemento==i){
                izquierdo = ((2*i)+1)<arreglo.length ? (2*i)+1 : -1 ;
        }
    }
    return izquierdo;
    }

    /**
     * Método que nos da la posición del hijo derecho del índice dado
     **/
    private int derecho(int indiceElemento) {
	int derecho = 0;
    for(int i=0; i<arreglo.length ; i++){
        if (indiceElemento==i){
                derecho = ((2*i)+2)<arreglo.length? (2*i)+2 : -1 ;
        }
    }
    return derecho;
    }

    @Override
    public void agregar(T elemento){
    if(estaLleno()){
            T [] arregloNuevo= creaArregloGenerico(getTamanio()*2);
            for(int i = 0 ; i<arreglo.length; i++){
                arregloNuevo[i]=arreglo[i];
            }
            arreglo=arregloNuevo;
        }
    if(esVacia()){
        arreglo[0]=elemento;
    }

	arreglo[tamanio] = elemento;
    rebalanceaHaciaArriba(tamanio);

    tamanio++;
    }
    
    /**
     * Metodo para eliminar el elemento que se encuentra en el tope del heap.
     * El método devuelve el valor eliminado.
     */
    public T eliminarTope() {
        T tope = arreglo[0];
        arreglo[0]=arreglo[tamanio-1]; 
        rebalanceaHaciaAbajo(0);
        tamanio--;
        return tope;
    }

    /**
     * Método para intercambiar dos elementos en los índices i y j.
     * Antes de usarse debemos asegurarnos de que los índices sean válidos.
     **/
    private void intercambia(int i, int j) {
        if(i>arreglo.length && j>arreglo.length)
            return;

        T elementoI=arreglo[i];
        T elementoJ= arreglo[j];

        arreglo[i]=elementoJ;
        arreglo[j]=elementoI;
    }

    /**
     * Metodo que se encarga de hacer el rebalanceo cuando agregamos un elemento.
     * @param indiceElemento
     */
    private void rebalanceaHaciaArriba(int indiceElemento) {
	//Aquí va tu código
        if(comparador(arreglo[indiceElemento],arreglo[padre(indiceElemento)])){
            intercambia(padre(indiceElemento),indiceElemento);
            rebalanceaHaciaArriba(padre(indiceElemento));
        }else
        return;

    }

    /**
     * Metodo que se encarga de hacer el rebalanceo cuando eliminamos un elemento.
     * @param indiceElemento
     */
    
    private void rebalanceaHaciaAbajo(int indiceElemento) {
        if(indiceElemento==-1)
            return;
        //arreglo[indiceElemento]=arreglo[tamanio-1]; 
        int indiceIntercambia = indiceItercambiable(derecho(indiceElemento), izquierdo(indiceElemento));
        if(indiceIntercambia==-1)
            return;
        intercambia(indiceElemento , indiceIntercambia);
        rebalanceaHaciaAbajo(indiceIntercambia);


    }

    /**
     * Método que nos dice cuál es el índice del elemento que tenemos que intercambiar con el padre.
     * Se utiliza en rebalanceaHaciaAbajo.
     * Si no hay que hacer intercambios porque ya no hay hijos, debe devolver -1.
     **/
    private int indiceItercambiable( int i, int j) {
        if(i<tamanio && j<tamanio && i>0 && j>0){
            if(comparador(arreglo[i], arreglo[j])) return  i;
            else return j;
        }

        if(i > 0 && i<tamanio && j>= tamanio)
            return i;
        if(j > 0 && i>=tamanio && j < tamanio)
            return j;

        

        return -1;
    }

    /**
     * Metodo para obtener el elemento que se encuentra en el tope del heap
     * @return
     * @throws NoSuchElementException
     */
    public T obtenerPrioritario() throws NoSuchElementException{
	return arreglo[0];
    }

    @Override
    public void eliminar(T elemento) throws NoSuchElementException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean contiene(T elemento) {
	   for(int i=0; i<arreglo.length;i++){
            if(elemento.equals(arreglo[i]))
                return true;
       }
   return false;
    }

    public boolean estaLleno() {
    for (int i =0 ; i<arreglo.length ; i++){
        if(arreglo[i]==null)
            return false;
    }
    return true;
    }
    @Override
    public boolean esVacia() {
	return tamanio == 0;
    }

    @Override
    public int getTamanio() {
	return tamanio;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterador();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        Heap<T> heap = (Heap<T>) o;
	//Aquí va tu código
        boolean equals = true;
        if (this.getTamanio()!= heap.getTamanio())
            equals= false;


        for(int i =0; i<heap.getTamanio() && i<this.getTamanio() ; i++){
            if(!heap.arreglo[i].equals(this.arreglo[i]))
                equals = false;
        }

        return equals;
    }

    @Override
    public String toString() {
        if (esVacia()) {
            return "[]";
        }
        String s = "[";
        for (int i = 0; i < tamanio - 1; i++) {
            s += arreglo[i] + ", ";
        }
        s += arreglo[tamanio-1] + "]";

        return s;
    }



}