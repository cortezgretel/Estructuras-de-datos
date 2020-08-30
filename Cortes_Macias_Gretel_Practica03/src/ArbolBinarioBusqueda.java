import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * Clase para modelar árboles binarios de búsqueda genéricos.</p>
 *
 * <p>
 * Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 * <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 * descendientes por la izquierda.</li>
 * <li>Cualquier elemento en el árbol es menor o igual que todos sus
 * descendientes por la derecha.</li>
 * </ul>
 *
 * @param <T>
 */
public class ArbolBinarioBusqueda<T extends Comparable<T>> extends ArbolBinario<T> {

    /* Clase privada para iteradores de árboles binarios ordenados. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los nodos por profundidad (DFS). */
        private Pila<Nodo> pila;

        /* Construye un iterador con el nodo recibido. */
        public Iterador() {
	    pila = new Pila<Nodo>();
        Nodo nodo = raiz;
          while(nodo != null){
            pila.push(nodo);
            nodo = nodo.izquierdo;
          }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override
        public boolean hasNext() {
            return !pila.esVacia();
       	}

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override
        public T next() {
	    if(pila.esVacia()) throw new NoSuchElementException("Pila vacía");
          Nodo nodo = pila.pop();
          T elemento = nodo.elemento;
          nodo = nodo.derecho;
          while(nodo != null){
            pila.push(nodo);
            nodo = nodo.izquierdo;
          }
          return elemento;
        }
    }

    /**
     * Constructor que no recibe parámeteros. {@link ArbolBinario}.
     */
    public ArbolBinarioBusqueda() {

    }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     *
     * @param coleccion la colección a partir de la cual creamos el árbol
     * binario ordenado.
     */
    public ArbolBinarioBusqueda(Coleccionable<T> coleccion) {
        super(coleccion);
    }

    /**
     * Método recursivo auxiliar que agrega un elemento contenido en el nodo nuevo.
     * Comienza las comparaciones desde el nodo n.
     *
     **/
    protected void agregaNodo(Nodo n, Nodo nuevo) {
	  if(n.elemento.compareTo(nuevo.elemento) > 0){
        if(n.hayIzquierdo())
          agregaNodo(n.izquierdo, nuevo);
        else{
          n.izquierdo = nuevo;
          nuevo.padre = n;
        }
      }else{
        if(n.hayDerecho())
          agregaNodo(n.derecho, nuevo);
        else{
          n.derecho = nuevo;
          nuevo.padre = n;
        }
      }

    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     *
     * @param elemento el elemento a agregar.
     */
    @Override
    public void agregar(T elemento) {
        Nodo nuevo = nuevoNodo(elemento);
        if(raiz == null){
        raiz = nuevo;
        }
        else
        agregaNodo(raiz, nuevo);

        this.tamanio++;
    }

    /**
     * Método auxiliar que elimina el nodo n. Notemos que en este punto
     * ya tenemos una referencia al nodo que queremos eliminar.
     **/
    protected Nodo eliminaNodo(Nodo n) {
    Nodo eliminado = n;
	if(n == this.raiz && !n.hayDerecho() && !n.hayIzquierdo())
        this.raiz = null;
      else if(n == this.raiz && n.hayDerecho()){
        this.raiz = n.derecho;
        n.derecho.padre = null;
      }
      else if(n == this.raiz && n.hayIzquierdo()){
        this.raiz = n.izquierdo;
        n.izquierdo.padre = null;
      }
      // Caso en donde el vértice no tiene hijos y NO es la raíz
      else if(!n.hayDerecho() && !n.hayIzquierdo()){
        if(n.padre.hayIzquierdo() && n.padre.izquierdo == n)
          n.padre.izquierdo = null;
        else
          n.padre.derecho = null;
      }
      // Caso en donde el vértice tiene un hijo derecho
      else if(n.hayDerecho()){
        if(n.padre.hayIzquierdo() && n.padre.izquierdo == n){
          n.padre.izquierdo = n.derecho;
          n.derecho.padre = n.padre;
        }else{
          n.padre.derecho = n.derecho;
          n.derecho.padre = n.padre;
        }
      }
      // Caso en donde el vértice tiene un hijo izquierdo
      else if(n.hayIzquierdo()){
        if(n.padre.hayIzquierdo() && n.padre.izquierdo == n){
          n.padre.izquierdo = n.izquierdo;
          n.izquierdo.padre = n.padre;
        }else{
          n.padre.derecho = n.izquierdo;
          n.izquierdo.padre = n.padre;
        }
      }
      return eliminado;
    }

    private Nodo intercambia(Nodo nodo) {
      Nodo nodoMaximo = maximoEnSubarbolIzquierdo(nodo.izquierdo);
      T elementoMaximo = nodoMaximo.elemento;
      nodo.elemento = elementoMaximo;
      return nodoMaximo;
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     *
     * @param elemento el elemento a eliminar.
     */
    @Override
    public void eliminar(T elemento) {
    Nodo n = buscaNodo(raiz, elemento);
    Nodo padre;
    if(n == null) return;

    if(n.hayIzquierdo() && n.hayDerecho()){
        Nodo eliminable = intercambia(n);
        eliminaNodo(eliminable);
    }
    else
    eliminaNodo(n);
    
    this.tamanio--;
    }

    /**
     * Método que encuentra el elemento máximo en el subárbol izquierdo
     **/
    private Nodo maximoEnSubarbolIzquierdo(Nodo n) {
	if(!n.hayDerecho()) return n;
      return maximoEnSubarbolIzquierdo(n.derecho);
    }

    /**
     * Nos dice si un elemento está contenido en el arbol.
     *
     * @param elemento el elemento que queremos verificar si está contenido en
     * la arbol.
     * @return <code>true</code> si el elemento está contenido en el arbol,
     * <code>false</code> en otro caso.
     */
    @Override
    public boolean contiene(T elemento) {
        return buscaNodo(raiz, elemento) != null;
    }

    /**
     * Método que busca un a elemento en el árbol desde el nodo n
     **/
    protected Nodo buscaNodo(Nodo n, T elemento) {
	if (n == null || elemento == null)
        return null;
    if (elemento.compareTo(n.elemento) == 0)
        return n;
    if (elemento.compareTo(n.elemento) < 0)
        return buscaNodo(n.izquierdo, elemento);
    return buscaNodo(n.derecho, elemento);
    }

    /**
     * Rota el árbol a la derecha sobre el nodo recibido. Si el nodo no tiene
     * hijo izquierdo, el método no hace nada.
     *
     * @param nodo el nodo sobre el que vamos a rotar.
     */
    protected void rotacionDerecha(Nodo nodo) {
	 if (nodo == null || !nodo.hayIzquierdo())
            return;
        Nodo q = nodo;
        Nodo p = q.izquierdo;
        p.padre = q.padre;
        if(q != this.raiz)
            if(q.padre.izquierdo == q )
                q.padre.izquierdo = p;
            else
                q.padre.derecho = p;
        else
            raiz = p;
        q.izquierdo = p.derecho;
        if(p.hayDerecho())
            p.derecho.padre = q;
        p.derecho = q;
        q.padre = p;
    }

    /**
     * Rota el árbol a la izquierda sobre el nodo recibido. Si el nodo no tiene
     * hijo derecho, el método no hace nada.
     *
     * @param nodo el nodo sobre el que vamos a rotar.
     */
    protected void rotacionIzquierda(Nodo nodo) {
	if(nodo == null || !nodo.hayDerecho())
          return;
        Nodo p = nodo;
        Nodo q = p.derecho;
        q.padre = p.padre;
        if(this.raiz != p){
          if(p.padre.izquierdo == p)
            p.padre.izquierdo = q;
          else
            p.padre.derecho = q;
        }
        else
          this.raiz = q;
        p.derecho = q.izquierdo;
        if(q.hayIzquierdo())
          q.izquierdo.padre = p;
        q.izquierdo = p;
        p.padre = q;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     *
     * @return un iterador para iterar el árbol.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterador();
    }

    public void pruebaRotacionIzquierda(T elemento){
        Nodo nodo = this.buscaNodo(raiz, elemento);
        this.rotacionIzquierda(nodo);
    }
    
    public void pruebaRotacionDerecha(T elemento){
        Nodo nodo = this.buscaNodo(raiz, elemento);
        this.rotacionDerecha(nodo);
    }

}