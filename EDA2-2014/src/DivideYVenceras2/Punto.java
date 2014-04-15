package DivideYVenceras2;

public class Punto implements Comparable<Object> {		//Los puntos seran un objeto que usaremos para nuestro algoritmo, y contendrán los valores X e Y
														//Implementa Comparable para ahcer posible las comparaciones entre ellos y permitir ordenar los puntos
	private int x;
	private int y;

	public Punto(){
		this.x = 0;
		this.y = 0;
	}
	
	public Punto(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}
	
	public int setY(int y) {
		return y;
	}

	public int setX(int x) {
		return x;
	}

	@Override
	public String toString() {
		return x + ", "+ y;
	}
	
	public boolean Equals(Punto punto){
		if(this.x==punto.x&&this.y==punto.y){
			return true;
		}else{
			return false;
		}
	}
	
	public int compareY(Punto punto){
		if(this.y>punto.y){
			return 1;
		}else if(this.y<punto.y){
			return -1;
		}else if(this.y==punto.y){
			return 0;
		}
		return 0;
	}

	@Override
	public int compareTo(Object punto) {
		Punto punto2= (Punto) punto;
		if(this.x>(punto2.x)){
			return 1;
		}else if(this.x<punto2.x){
			return -1;
		}else if(this.x==punto2.x){			
			return this.compareY(punto2);
		}
		return 0;
	}
	
	
}

