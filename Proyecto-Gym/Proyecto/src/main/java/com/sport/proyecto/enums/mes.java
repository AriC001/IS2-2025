package com.sport.proyecto.enums;

public enum mes {
    ENERO,
    FEBRERO,
    MARZO,
    ABRIL,
    MAYO,
    JUNIO,
    JULIO,
    AGOSTO,
    SEPTIEMBRE,
    OCTUBRE,
    NOVIEMBRE,
    DICIEMBRE;

    private static final mes[] vals = values();

    public mes siguiente(){
        return vals[(this.ordinal() + 1) % vals.length];
    }
    //this.ordinal() → devuelve la posición del valor en el enum, empezando en 0.
    //% vals.length → el operador módulo se usa para que, cuando llegues a diciembre (índice 11), no se rompa:

}
