package org.ricksnrz.apirest.apirestroom.Services.CRUD;


import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.ricksnrz.apirest.apirestroom.Entities.Habitacion;
import org.ricksnrz.apirest.apirestroom.Entities.Inquilino;
import org.ricksnrz.apirest.apirestroom.Entities.Recibo;
import org.ricksnrz.apirest.apirestroom.Repositories.HabitacionRepository;
import org.ricksnrz.apirest.apirestroom.Repositories.InquilinoRepository;
import org.ricksnrz.apirest.apirestroom.Repositories.ReciboRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

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

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // 🧾 TITULO
        document.add(new Paragraph("RECIBO DE ALQUILER")
                .setFont(bold)
                .setFontSize(16)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("\n"));

        // 🔲 BLOQUE SUPERIOR (DIRECCIÓN + REC)
        Table top = new Table(new float[]{3, 0.2f, 1}).useAllAvailableWidth();

        // DIRECCIÓN
        Cell direccionCell = new Cell()
                .add(new Paragraph("Dirección:").setFont(bold))
                .add(new Paragraph("Las Hormigas Mz A Lt 24 - La Molina"))
                .setPadding(10)
                .setBorder(new SolidBorder(1));

        // ESPACIO (columna invisible)
        Cell espacio = new Cell()
                .setBorder(Border.NO_BORDER);

        // REC
        Cell recCell = new Cell()
                .add(new Paragraph("REC-" + recibo.getId())
                        .setFont(bold)
                        .setFontColor(ColorConstants.RED)
                        .setTextAlignment(TextAlignment.CENTER))
                .add(new Paragraph("DNI: 74843955")
                        .setTextAlignment(TextAlignment.CENTER))
                .setPadding(10)
                .setBorder(new SolidBorder(1));

        top.addCell(direccionCell);
        top.addCell(espacio); // 👈 separación real
        top.addCell(recCell);

        document.add(top);

        document.add(new Paragraph("\n"));

        // 🔲 BLOQUE DATOS INQUILINO
        Table datos = new Table(1).useAllAvailableWidth();

        Cell datosCell = new Cell()
                .add(new Paragraph()
                        .add(new Text("Inquilino: ").setFont(bold))
                        .add(recibo.getInquilino().getNombre() + " " + recibo.getInquilino().getApellido()))
                .add(new Paragraph()
                        .add(new Text("DNI: ").setFont(bold))
                        .add(recibo.getInquilino().getDni()))
                .add(new Paragraph()
                        .add(new Text("Fecha: ").setFont(bold))
                        .add(recibo.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                .setPadding(10)
                .setBorder(new SolidBorder(1));

        datos.addCell(datosCell);

        document.add(datos);

        document.add(new Paragraph("\n"));

        // 🔲 TABLA CON BORDE
        Table table = new Table(new float[]{4, 2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell()
                .add(new Paragraph("Descripción").setFont(bold))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));

        table.addHeaderCell(new Cell()
                .add(new Paragraph("Monto").setFont(bold))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));

        table.addCell(recibo.getConcepto());
        table.addCell("S/ " + recibo.getMonto());

        document.add(table);

        document.add(new Paragraph("\n"));

        Table footer = new Table(new float[]{2, 1}).useAllAvailableWidth();

       // FIRMA
        Cell firma = new Cell()
                .add(new Paragraph("\n\n________________________"))
                .add(new Paragraph("Firma"))
                .setTextAlignment(TextAlignment.LEFT)
                .setBorder(Border.NO_BORDER);

        // TOTAL
        Cell total = new Cell()
                .add(new Paragraph("TOTAL: S/ " + recibo.getMonto())
                        .setFont(bold))
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(Border.NO_BORDER);

        footer.addCell(firma);
        footer.addCell(total);

        document.add(footer);

        document.close();

        return out.toByteArray();
    }

}
