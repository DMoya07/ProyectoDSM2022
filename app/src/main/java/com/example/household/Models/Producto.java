package com.example.household.Models;

public class Producto {
    private String idProducto;
    private String nombreProducto;
    private String cantProducto;
    private String fechaVencimiento;
    private long timestamp;

    public String getidProducto() {
        return idProducto;
    }

    public void setidProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getnombreProducto() {
        return nombreProducto;
    }

    public void setnombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public String getCantProducto() {
        return cantProducto;
    }

    public void setCantProducto(String cantProducto) {
        this.cantProducto = cantProducto;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    @Override
    public String toString() {
        return nombreProducto;
    }
}
