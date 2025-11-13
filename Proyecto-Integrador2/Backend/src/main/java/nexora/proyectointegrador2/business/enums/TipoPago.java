package nexora.proyectointegrador2.business.enums;

/**
 * Enum que representa los diferentes tipos de pago disponibles en el sistema.
 * 
 * Los tipos de pago definen las formas en que un cliente puede realizar
 * el pago de un alquiler o factura:
 * 
 * - EFECTIVO: Pago realizado en efectivo (dinero físico)
 * - TRANSFERENCIA: Pago realizado mediante transferencia bancaria
 * - BILLETERA_VIRTUAL: Pago realizado mediante billetera virtual (como Mercado Pago)
 */
public enum TipoPago {
  /** Pago en efectivo (dinero físico) */
  EFECTIVO,
  
  /** Pago mediante transferencia bancaria */
  TRANSFERENCIA,
  
  /** Pago mediante billetera virtual (ej: Mercado Pago, Ualá, etc.) */
  BILLETERA_VIRTUAL
}
