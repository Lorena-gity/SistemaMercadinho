package br.edu.ufersa.sistemaMercado.util;

import br.edu.ufersa.sistemaMercado.model.entities.FormaDeVenda;
import br.edu.ufersa.sistemaMercado.model.entities.ItemNota;
import br.edu.ufersa.sistemaMercado.model.entities.NotaCompra;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

// Gera o comprovante (nota) de uma venda em PDF, salvo na pasta "notas".
public final class GeradorNotaPDF {

    private GeradorNotaPDF() {
    }

    public static File gerar(NotaCompra nota, double valorPago, double troco, String operador) throws Exception {
        File pasta = new File("notas");
        if (!pasta.exists()) {
            pasta.mkdirs();
        }
        File arquivo = new File(pasta, "nota-" + nota.getNumeroNota() + ".pdf");

        Font fTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font fCabecalho = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font fNormal = FontFactory.getFont(FontFactory.HELVETICA, 11);

        Document doc = new Document();
        try (FileOutputStream fos = new FileOutputStream(arquivo)) {
            PdfWriter.getInstance(doc, fos);
            doc.open();

            Paragraph titulo = new Paragraph("Mercadinho do Seu Pedrinho", fTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            Paragraph subtitulo = new Paragraph("Comprovante de Venda", fNormal);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(15);
            doc.add(subtitulo);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            doc.add(new Paragraph("Nota nº: " + nota.getNumeroNota(), fNormal));
            doc.add(new Paragraph("Data: " + nota.getDataHora().format(fmt), fNormal));
            doc.add(new Paragraph("Operador: " + operador, fNormal));
            doc.add(new Paragraph(" "));

            PdfPTable tabela = new PdfPTable(new float[]{4, 3, 2, 2, 2});
            tabela.setWidthPercentage(100);
            for (String cabecalho : new String[]{"Produto", "Marca", "Qtd", "Preço un.", "Subtotal"}) {
                tabela.addCell(new PdfPCell(new Phrase(cabecalho, fCabecalho)));
            }
            for (ItemNota item : nota.getListaItens()) {
                boolean porPeso = item.getProduto().getFormaDeVenda() == FormaDeVenda.QUILO;
                String qtd = porPeso
                        ? String.format("%.3f kg", item.getQuantidade())
                        : String.valueOf((int) item.getQuantidade());
                String marca = (item.getProduto().getMarca() != null && !item.getProduto().getMarca().isEmpty())
                        ? item.getProduto().getMarca() : "-";

                tabela.addCell(new Phrase(item.getProduto().getNome(), fNormal));
                tabela.addCell(new Phrase(marca, fNormal));
                tabela.addCell(new Phrase(qtd, fNormal));
                tabela.addCell(new Phrase(moeda(item.getPrecoUnitario()), fNormal));
                tabela.addCell(new Phrase(moeda(item.calcularSubTotal()), fNormal));
            }
            doc.add(tabela);
            doc.add(new Paragraph(" "));

            doc.add(alinhadoDireita("Total: " + moeda(nota.getValorTotal()), fCabecalho));
            doc.add(alinhadoDireita("Recebido: " + moeda(valorPago), fNormal));
            doc.add(alinhadoDireita("Troco: " + moeda(troco), fNormal));
        } finally {
            if (doc.isOpen()) {
                doc.close();
            }
        }
        return arquivo;
    }

    private static Paragraph alinhadoDireita(String texto, Font fonte) {
        Paragraph p = new Paragraph(texto, fonte);
        p.setAlignment(Element.ALIGN_RIGHT);
        return p;
    }

    private static String moeda(double valor) {
        return "R$ " + String.format("%.2f", valor);
    }
}
