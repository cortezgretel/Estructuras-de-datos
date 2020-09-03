/**
 * <p>
 * Clase para árboles AVL.</p>
 *
 * <p>
 * Un árbol AVL cumple que para cada uno de sus nodos, la diferencia entre la
 * áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 *
 * @param <T>
 */
public class ArbolAVL<T extends Comparable<T>> extends ArbolBinarioBusqueda<T> {

    /**
     * Clase interna protegida para nodos de árboles AVL. La única diferencia
     * con los nodos de árbol binario, es que tienen una variable de clase para
     * la altura del nodo.
     */
    protected class NodoAVL extends ArbolBinario.Nodo {

        /**
         * La altura del nodo.
         */
        protected int altura;

        /**
         * Constructor único que recibe un elemento.
         *
         * @param elemento el elemento del nodo.
         */
        public NodoAVL(T elemento) {
            super(elemento);
            this.altura = 0;
        }
	
	/**
	 * Recomendamos usar este método auxiliar para que en el método público
	 * hagas el cast del objeto o, a NodoAVL y dejar el trabajo a este método.
  	 * Si no quieres usarlo, siéntete libre de eliminar esta firma.
	 */
        private boolean equals(NodoAVL v, NodoAVL v2) {
	    if(v == null && v2 != null || v != null && v2 == null) return false;
        if(v.hayIzquierdo() && v2.hayIzquierdo() && v.hayDerecho() && v2.hayDerecho()){
            if(v.elemento.equals(v2.elemento))
              return equals(nodoAVL(v.izquierdo), nodoAVL(v2.izquierdo)) && equals(nodoAVL(v.derecho), nodoAVL(v2.derecho));
            else
              return false;
        }
        else if(v.hayIzquierdo() && v2.hayIzquierdo() && !v.hayDerecho() && !v2.hayDerecho())
            if(v.elemento.equals(v2.elemento))
              return equals(nodoAVL(v.izquierdo), nodoAVL(v2.izquierdo));
            else
              return false;

        else if(v.hayDerecho() && v2.hayDerecho() && !v.hayIzquierdo() && !v2.hayIzquierdo())
            if(v.elemento.equals(v2.elemento))
              return equals(nodoAVL(v.derecho), nodoAVL(v2.derecho));
            else
              return false;

        else if(!v.hayDerecho() && !v2.hayDerecho() && !v.hayIzquierdo() && !v2.hayIzquierdo()){
            T ev = (T)v.elemento;
            T ev2 = (T)v2.elemento;
            return ev.equals(ev2);
          }
        return false; 
        }

        /**
         * Compara el nodo con otro objeto. La comparación es
         * <em>recursiva</em>.
         *
         * @param o el objeto con el cual se comparará el nodo.
         * @return <code>true</code> si el objeto es instancia de la clase
         * {@link NodoAVL}, su elemento es igual al elemento de éste nodo, los
         * descendientes de ambos son recursivamente iguales, y las alturas son
         * iguales; <code>false</code> en otro caso.
         */
        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (getClass() != o.getClass()) {
                return false;
            }
            @SuppressWarnings("unchecked")
            NodoAVL nodo = (NodoAVL) o;
            if(nodo == null) return false;
            return equals(this, nodo);
        }

        @Override
        public String toString() {
            String s = super.toString();
            return s += " alt=" + altura;
        }
    }

    public ArbolAVL() {

    }

    public ArbolAVL(Coleccionable<T> coleccion) {
        super(coleccion);
    }

    private void actualizaAltura(NodoAVL v) {
        v.altura = 1 + Math.max(this.getAltura(nodoAVL(v.izquierdo)), this.getAltura(nodoAVL(v.derecho)));
    }


    protected int balance(NodoAVL n){
      return this.getAltura(nodoAVL(n.izquierdo)) - this.getAltura(nodoAVL(n.derecho));
    }

    private void rebalancea(NodoAVL nodo) {
    if(nodo == null) return;
    this.actualizaAltura(nodo);
    int balanceo = this.balance(nodo);

    if(balanceo == -2){
    NodoAVL aux = nodoAVL(nodo.derecho);
        if(this.balance(aux) == 1)
            this.rotacionDerechaAVL(aux);
            this.rotacionIzquierdaAVL(nodo);
    }

    else if(balanceo == 2){
        NodoAVL aux2 = nodoAVL(nodo.izquierdo);
        if(this.balance(aux2) == -1)
            this.rotacionIzquierdaAVL(aux2);
            this.rotacionDerechaAVL(nodo);
    }

    rebalancea(nodoAVL(nodo.padre));
    }

    private void rotacionIzquierdaAVL(NodoAVL p){ // Aquí suponemos que p tiene hijo derecho q
      super.rotacionIzquierda(p);
      actualizaAltura(p);
      actualizaAltura(nodoAVL(p.padre));
    }

    private void rotacionDerechaAVL(NodoAVL p){
      super.rotacionDerecha(p);
      actualizaAltura(p);
      actualizaAltura(nodoAVL(p.padre));
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioBusqueda#agrega}, y después balancea el árbol girándolo como
     * sea necesario. La complejidad en tiempo del método es <i>O</i>(log
     * <i>n</i>) garantizado.
     *
     * @param elemento el elemento a agregar.
     */
    @Override
    public void agregar(T elemento) {
	super.agregar(elemento);
    rebalancea(nodoAVL(ultimoElemento));
    }


    private void eliminaHoja(NodoAVL nodo){
      // Si no hay padre significa que el nodo a eliminar es la raíz
      if(!nodo.hayPadre())
        raiz = null;
      else if(nodo.padre.izquierdo == nodo)
        nodo.padre.izquierdo = null;
      else
        nodo.padre.derecho = null;
    }


    private void subeHijo(NodoAVL nodo){
      // Esto significa que el nodo tiene un hijo izquierdo
      if(nodo.hayIzquierdo()){
        if(nodo == raiz){
          raiz = nodo.izquierdo;
          nodo.izquierdo.padre = null;
        }
        // El nodo no es la raíz y por ende tiene un padre
        else{
          nodo.izquierdo.padre = nodo.padre;
          if(nodo.padre.izquierdo == nodo)
            nodo.padre.izquierdo = nodo.izquierdo;
          else
            nodo.padre.derecho = nodo.izquierdo;
        }
      }
      // Esto significa que el nodo tiene un hijo derecho
      else if(nodo.hayDerecho()){
        if(nodo == raiz){
          raiz = nodo.derecho;
          nodo.derecho.padre = null;
        }
        // El nodo no es la raíz y por ende tiene un padre
        else{
          nodo.derecho.padre = nodo.padre;
          if(nodo.padre.izquierdo == nodo)
            nodo.padre.izquierdo = nodo.derecho;
          else
            nodo.padre.derecho = nodo.derecho;
        }
      }
    }

    /**
     * Elimina un elemento del árbol. El método elimina el nodo que contiene el
     * elemento, y gira el árbol como sea necesario para rebalancearlo. La
     * complejidad en tiempo del método es <i>O</i>(log <i>n</i>) garantizado.
     *
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override
    public void eliminar(T elemento) {
	NodoAVL nodo = nodoAVL(this.buscaNodo(raiz,elemento));
        if(nodo == null) return;
        NodoAVL auxiliar;
        if(nodo.hayIzquierdo()){
          auxiliar = nodo;
          // Obtenemos el nodo mayor del subarbol izquierdo
          nodo = nodoAVL(this.maximoEnSubarbolIzquierdo(nodo.izquierdo));
          auxiliar.elemento = nodo.elemento;
        }
        // Si sucede esta condición es porque el nodo es una hoja sin hijos
        if(!nodo.hayIzquierdo() && !nodo.hayDerecho())
          eliminaHoja(nodo);
        else
          subeHijo(nodo);
        // Rebalanceamos sobre el padre del nodo
        rebalancea(nodoAVL(nodo.padre));
        tamanio--;
    }

    private int getAltura(Nodo nodo) {
       return super.altura();
    }

    /**
     * Convierte el nodo (visto como instancia de {@link
     * Nodo}) en nodo (visto como instancia de {@link
     * NodoAVL}). Método auxililar para hacer este cast en un único lugar.
     *
     * @param nodo el nodo de árbol binario que queremos como nodo AVL.
     * @return el nodo recibido visto como nodo AVL.
     */
    protected NodoAVL nodoAVL(Nodo nodo) {
        return (NodoAVL) nodo;
    }
}