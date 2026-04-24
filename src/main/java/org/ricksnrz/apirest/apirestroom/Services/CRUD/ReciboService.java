package org.ricksnrz.apirest.apirestroom.Services.CRUD;


import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.ricksnrz.apirest.apirestroom.Entities.Habitacion;
import org.ricksnrz.apirest.apirestroom.Entities.Inquilino;
import org.ricksnrz.apirest.apirestroom.Entities.Recibo;
import org.ricksnrz.apirest.apirestroom.Repositories.HabitacionRepository;
import org.ricksnrz.apirest.apirestroom.Repositories.InquilinoRepository;
import org.ricksnrz.apirest.apirestroom.Repositories.ReciboRepository;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReciboService {

    private final ReciboRepository reciboRepository;
    private final InquilinoRepository inquilinoRepository;
    private final HabitacionRepository habitacionRepository;

    public ReciboService(ReciboRepository reciboRepository,
                         InquilinoRepository inquilinoRepository,
                         HabitacionRepository habitacionRepository) {
        this.reciboRepository = reciboRepository;
        this.inquilinoRepository = inquilinoRepository;
        this.habitacionRepository = habitacionRepository;
    }

    public Recibo crearRecibo(Long inquilinoId, Long habitacionId, Recibo recibo) {
        // Buscar al inquilino
        Inquilino inquilino = inquilinoRepository.findById(inquilinoId)
                .orElseThrow(() -> new RuntimeException("Inquilino no encontrado"));

        // Buscar la habitación
        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new RuntimeException("Habitación no encontrada"));

        // Asignar las relaciones al recibo
        recibo.setInquilino(inquilino);
        recibo.setHabitacion(habitacion);

        // Guardar el recibo
        return reciboRepository.save(recibo);
    }

    public Recibo obtenerReciboPorId(Long id) {
        return reciboRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recibo no encontrado"));
    }

    public List<Recibo> listarRecibos() {
        return reciboRepository.findAll();
    }

    public byte[] generarPdf(Long id) throws Exception {
        Recibo recibo = obtenerReciboPorId(id);



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("RECIBO DE PAGO"));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Inquilino: " + recibo.getInquilino().getNombre() + " " + recibo.getInquilino().getApellido()));
        document.add(new Paragraph("Habitación: " + recibo.getHabitacion().getNumero()));
        document.add(new Paragraph("Monto: S/ " + recibo.getMonto()));
        document.add(new Paragraph("Concepto: " + recibo.getConcepto()));
        document.add(new Paragraph("Fecha: " + recibo.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        document.close();

        return baos.toByteArray();
    }
}
