package at.technikum.tourplanner.service.reports;

import at.technikum.tourplanner.persistence.entity.TourEntity;
import at.technikum.tourplanner.service.TourService;
import at.technikum.tourplanner.service.dto.TourDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfGenerator
{

    private String parseThymeleafTemplateTourDetails(TourEntity tour) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("header", tour.getName() + " TourReport");
        context.setVariable("tour", tour);
        context.setVariable("tourLogs", tour.getTourLogs());
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        context.setVariable("currentDate", currentDate);

        return templateEngine.process("thymeleaf/tour_report", context);
    }

    private String parseThymeleafTemplateToursSummaryDetails(List<TourEntity> tours) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("header", "Tours Summary Report");
        context.setVariable("tours", tours);
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        context.setVariable("currentDate", currentDate);

        return templateEngine.process("thymeleaf/tours_summary_report", context);
    }

    private void generatePdfFromHtml(String html, OutputStream outputStream) throws Exception {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    public void generateTourReport(TourEntity tour, OutputStream outputStream) throws Exception {
        generatePdfFromHtml(parseThymeleafTemplateTourDetails(tour), outputStream);
    }

    public void generateToursSummaryReport(List<TourEntity> tours, OutputStream outputStream) throws Exception {
        generatePdfFromHtml(parseThymeleafTemplateToursSummaryDetails(tours), outputStream);
    }
}
