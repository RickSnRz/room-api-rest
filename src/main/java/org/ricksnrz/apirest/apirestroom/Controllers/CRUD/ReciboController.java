package org.ricksnrz.apirest.apirestroom.Controllers.CRUD;


import org.ricksnrz.apirest.apirestroom.Entities.Recibo;
import org.ricksnrz.apirest.apirestroom.Services.CRUD.ReciboService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recibos")
public class ReciboController {

    private final ReciboService reciboService;

    public ReciboController(ReciboService reciboService) {
        this.reciboService = reciboService;
    }

    @PostMapping
    public ResponseEntity<Recibo> crearRecibo(@RequestParam Long inquilinoId,
                                              @RequestParam Long habitacionId,
                                              @RequestBody Recibo recibo) {
        Recibo nuevoRecibo = reciboService.crearRecibo(inquilinoId, habitacionId, recibo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRecibo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recibo> obtenerReciboPorId(@PathVariable Long id) {
        Recibo recibo = reciboService.obtenerReciboPorId(id);
        return ResponseEntity.ok(recibo);
    }

    @GetMapping
    public ResponseEntity<List<Recibo>> listarRecibos() {
        List<Recibo> recibos = reciboService.listarRecibos();
        return ResponseEntity.ok(recibos);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generarPdf(@PathVariable Long id) {
        try {
            byte[] pdf = reciboService.generarPdf(id);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=recibo.pdf")
                    .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
