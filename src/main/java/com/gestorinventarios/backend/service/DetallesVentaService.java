package com.gestorinventarios.backend.service;

import com.gestorinventarios.backend.model.DetalleVenta;
import com.gestorinventarios.backend.model.Venta;
import com.gestorinventarios.backend.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetallesVentaService {
    private final DetalleVentaRepository detalleVentaRepository;

    public DetallesVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    public List<DetalleVenta> obtenerDetallesVenta(Long idVenta) {
        return detalleVentaRepository.findByVentaId(idVenta);
    }

    public int obtenerCantidadPorIdVentaYProducto(Long idVenta,Long idProducto) {
        return detalleVentaRepository.getCantidadByVentaIDandProductoId(idVenta,idProducto);
    }

    @Transactional
    public void actualizarDetallesVenta(Venta venta, List<DetalleVenta> detalles) {
        detalleVentaRepository.eliminarDetallesPorVenta(venta);

        for (DetalleVenta detalle : detalles) {
            DetalleVenta nuevoDetalle = new DetalleVenta();
            nuevoDetalle.setVenta(venta);
            nuevoDetalle.setProducto(detalle.getProducto());
            nuevoDetalle.setCantidad(detalle.getCantidad());
            nuevoDetalle.setPrecioUnitario(detalle.getPrecioUnitario());
            detalleVentaRepository.save(nuevoDetalle);
        }
    }
}
