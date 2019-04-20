package mx.edu.ittepic.ricardojimenez.tpdm_u3_practica1_ricardojimenez;

public class Vehiculo {
    public String id,modelo, serie, marca, fechaLanzamiento;

    public Vehiculo(String id,String modelo, String serie, String marca, String fechaLanzamiento) {
        this.id=id;
        this.modelo = modelo;
        this.serie = serie;
        this.marca = marca;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Vehiculo() {
    }
}
